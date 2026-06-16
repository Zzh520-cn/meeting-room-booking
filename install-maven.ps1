# Auto-install Maven script
# Run in PowerShell (no admin needed)

$MAVEN_VERSION = "3.9.9"
$MAVEN_URL = "https://dlcdn.apache.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.zip"
$INSTALL_DIR = "C:\apache-maven-$MAVEN_VERSION"
$ZIP_FILE = "$env:TEMP\maven.zip"

Write-Host "===== Installing Maven $MAVEN_VERSION =====" -ForegroundColor Cyan

# Download
Write-Host "[1/3] Downloading Maven..." -ForegroundColor Yellow
try {
    Invoke-WebRequest -Uri $MAVEN_URL -OutFile $ZIP_FILE -ErrorAction Stop
    Write-Host "  OK - Downloaded" -ForegroundColor Green
} catch {
    Write-Host "  Download failed, please install Maven manually:" -ForegroundColor Red
    Write-Host "  https://maven.apache.org/download.cgi" -ForegroundColor Yellow
    Write-Host "  1. Download binary zip" -ForegroundColor Yellow
    Write-Host "  2. Extract to C:\apache-maven-3.9.9" -ForegroundColor Yellow
    Write-Host "  3. Add C:\apache-maven-3.9.9\bin to system PATH" -ForegroundColor Yellow
    exit 1
}

# Extract
Write-Host "[2/3] Extracting..." -ForegroundColor Yellow
Expand-Archive -Path $ZIP_FILE -DestinationPath "C:\" -Force
Write-Host "  OK - Extracted to $INSTALL_DIR" -ForegroundColor Green

# Clean up
Remove-Item $ZIP_FILE -Force

# Add to PATH for current session
Write-Host "[3/3] Setting PATH..." -ForegroundColor Yellow
$env:PATH = "$INSTALL_DIR\bin;$env:PATH"

# Verify
Write-Host ""
Write-Host "===== Maven version =====" -ForegroundColor Cyan
mvn -version

Write-Host ""
Write-Host "===== Installation complete! =====" -ForegroundColor Cyan
Write-Host "Maven installed at: $INSTALL_DIR" -ForegroundColor Green
Write-Host ""
Write-Host "NOTE: PATH is set for this session only." -ForegroundColor Yellow
Write-Host "To add permanently, run this command in Admin PowerShell:" -ForegroundColor Yellow
Write-Host "  [Environment]::SetEnvironmentVariable('Path', [Environment]::GetEnvironmentVariable('Path','Machine') + ';$INSTALL_DIR\bin', 'Machine')" -ForegroundColor White
