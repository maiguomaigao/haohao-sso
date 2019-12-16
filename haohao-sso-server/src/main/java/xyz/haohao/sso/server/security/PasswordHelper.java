package xyz.haohao.sso.server.security;

import xyz.haohao.sso.dao.domain.SysUser;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

/**
 * 密码工具
 *
 * @Author: yezaishu@qq.com
 * @Date: 2019/12/12 12:12
 */
@Component
@Log4j2
public class PasswordHelper {

    private RandomNumberGenerator generator = new SecureRandomNumberGenerator();

    public static final String HASH_ALGORITHM_NAME = Sha512Hash.ALGORITHM_NAME;
    public static final int HASH_INTERATIONS = 2;

    /**
     * 生成salt
     * 加密password产生credential
     */
    public void encryptPassword(SysUser user) {
        user.setSalt(generator.nextBytes().toHex());
        Hash passwordHash = new SimpleHash(
                HASH_ALGORITHM_NAME,
                user.getCredential(),
                ByteSource.Util.bytes(user.getSalt()),
                HASH_INTERATIONS);
        user.setCredential(passwordHash.toHex());
    }

    /**
     * 将用户输入的秘密加密后与credential比较
     * @param password
     * @param user
     * @return
     */
    public boolean matchPassword(String password, SysUser user) {
        Hash passwordHash = new SimpleHash(
                HASH_ALGORITHM_NAME,
                password,
                ByteSource.Util.bytes(user.getSalt()),
                HASH_INTERATIONS);

        return passwordHash.toHex().equals(user.getCredential());
    }
}