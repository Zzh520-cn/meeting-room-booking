package com.booking.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * 密码工具类 —— BCrypt 哈希与验证
 * 兼容旧明文密码：匹配后自动升级为 BCrypt 哈希
 */
public class PasswordUtil {

    /** 哈希轮数（10 = 2^10 次迭代，约 100ms） */
    private static final int ROUNDS = 10;

    /**
     * 判断一个存储的密码是否是 BCrypt 哈希
     */
    public static boolean isHashed(String stored) {
        return stored != null && stored.startsWith("$2");
    }

    /**
     * 对明文密码进行哈希
     */
    public static String hash(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(ROUNDS));
    }

    /**
     * 验证明文密码与存储值是否匹配（兼容旧明文）
     * @param plainPassword 用户输入的明文密码
     * @param stored        数据库中存储的值（可能为明文或 BCrypt 哈希）
     * @return VerifyResult（匹配+是否需要升级）
     */
    public static VerifyResult verify(String plainPassword, String stored) {
        if (plainPassword == null || stored == null) {
            return new VerifyResult(false, false);
        }

        if (isHashed(stored)) {
            // BCrypt 哈希 → 正常验证
            boolean ok = BCrypt.checkpw(plainPassword, stored);
            return new VerifyResult(ok, false);
        } else {
            // 旧明文密码 → 直接比较，匹配则标记需要升级
            boolean ok = plainPassword.equals(stored);
            return new VerifyResult(ok, ok); // matched && needsUpgrade
        }
    }

    /**
     * 验证结果
     */
    public static class VerifyResult {
        public final boolean matched;
        public final boolean needsUpgrade; // 旧明文密码匹配成功，需升级为哈希

        public VerifyResult(boolean matched, boolean needsUpgrade) {
            this.matched = matched;
            this.needsUpgrade = needsUpgrade;
        }
    }
}
