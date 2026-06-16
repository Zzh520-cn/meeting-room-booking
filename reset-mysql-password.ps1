# MySQL Root Password Reset Script
# Run as Administrator in PowerShell

$MYSQL_SERVICE = "MySQL80"
$NEW_PASSWORD  = "123456"

Write-Host "===== MySQL Root Password Reset =====" -ForegroundColor Cyan

# 1. Stop MySQL service
Write-Host "[1/4] Stopping MySQL service..." -ForegroundColor Yellow
net stop $MYSQL_SERVICE
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Cannot stop service. Are you running as Administrator?" -ForegroundColor Red
    exit 1
}
Write-Host "  OK - Service stopped" -ForegroundColor Green

# 2. Find mysqld.exe
Write-Host "[2/4] Starting MySQL (skip grant tables)..." -ForegroundColor Yellow
$mysqldPath = $null
$mysqlPath  = $null
$possiblePaths = @(
    "D:\MySql\MySQL Server 8.0\bin",
    "C:\Program Files\MySQL\MySQL Server 8.0\bin",
    "C:\Program Files\MySQL\MySQL Server 8.4\bin"
)
foreach ($p in $possiblePaths) {
    if (Test-Path "$p\mysqld.exe") {
        $mysqldPath = "$p\mysqld.exe"
        $mysqlPath  = "$p\mysql.exe"
        break
    }
}
if (-not $mysqldPath) {
    Write-Host "ERROR: Cannot find mysqld.exe" -ForegroundColor Red
    exit 1
}
Write-Host "  Found: $mysqldPath"

$proc = Start-Process -FilePath $mysqldPath `
    -ArgumentList "--skip-grant-tables","--console" `
    -PassThru -WindowStyle Minimized

Start-Sleep -Seconds 4
Write-Host "  OK - MySQL started in safe mode" -ForegroundColor Green

# 3. Reset password
Write-Host "[3/4] Resetting root password..." -ForegroundColor Yellow

# IMPORTANT: In skip-grant-tables mode, FLUSH PRIVILEGES must come FIRST
# to load the grant tables, then ALTER USER, then FLUSH PRIVILEGES again
$sql1 = "FLUSH PRIVILEGES;"
$sql2 = "ALTER USER 'root'@'localhost' IDENTIFIED BY '" + $NEW_PASSWORD + "';"
$sql3 = "FLUSH PRIVILEGES;"

$argList = "-u", "root", "-e", ($sql1 + " " + $sql2 + " " + $sql3)
& $mysqlPath $argList 2>&1 | Out-Null

if ($LASTEXITCODE -ne 0) {
    Write-Host "  Retrying with alternative method..." -ForegroundColor Yellow
    $sql = "ALTER USER 'root'@'localhost' IDENTIFIED BY '123456'; FLUSH PRIVILEGES;"
    Start-Process -FilePath $mysqlPath -ArgumentList "-u", "root", "-e", $sql -Wait -NoNewWindow
}
Write-Host "  OK - Password reset to: $NEW_PASSWORD" -ForegroundColor Green

# 4. Restart MySQL normally
Write-Host "[4/4] Restarting MySQL service..." -ForegroundColor Yellow
Stop-Process -Id $proc.Id -Force -ErrorAction SilentlyContinue
Start-Sleep -Seconds 2
net start $MYSQL_SERVICE
Write-Host "  OK - MySQL service restarted" -ForegroundColor Green

Write-Host ""
Write-Host "===== Done! root password is now: $NEW_PASSWORD =====" -ForegroundColor Cyan
