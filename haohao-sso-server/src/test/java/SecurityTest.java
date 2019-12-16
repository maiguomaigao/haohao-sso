import xyz.haohao.sso.dao.domain.SysUser;
import xyz.haohao.sso.server.security.PasswordHelper;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: zaishu.ye
 * @Date: 2019/11/19 16:39
 */
@Log4j2
public class SecurityTest {
    @Autowired
    PasswordHelper passwordHelper;

    @Test
    public void testPassword(){
        SysUser u = new SysUser();
        u.setCredential("test001");
        new PasswordHelper().encryptPassword(u);
    }


}
