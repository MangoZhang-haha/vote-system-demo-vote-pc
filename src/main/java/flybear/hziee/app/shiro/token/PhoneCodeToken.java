package flybear.hziee.app.shiro.token;

import lombok.AllArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author flybear
 * @since 2019/12/19 23:34:24
 */
@AllArgsConstructor
public class PhoneCodeToken implements AuthenticationToken {

    private String phone;
    private String code;

    @Override
    public Object getPrincipal() {
        return this.phone;
    }

    @Override
    public Object getCredentials() {
        return this.code;
    }
}
