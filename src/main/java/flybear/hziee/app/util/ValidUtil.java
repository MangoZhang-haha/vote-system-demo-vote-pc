package flybear.hziee.app.util;

import flybear.hziee.app.enums.PatternEnum;
import flybear.hziee.app.exception.ValidException;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * 正则验证常用类，用于Controller层
 *
 * @author flybear
 * @since 2019/12/19 23:34:24
 */
public class ValidUtil {

    public static void match(String str, PatternEnum patternEnum) {
        if(!Pattern.compile(patternEnum.getRegEx()).matcher(str).matches()){
            throw new ValidException(patternEnum.getMsg());
        }
    }

    public static void isEmpty(Object obj, String msg) {
        if (!StringUtils.isEmpty(obj)) {
            throw new ValidException(msg);
        }
    }

    public static void isNotEmpty(Object obj, String msg) {
        if (StringUtils.isEmpty(obj)) {
            throw new ValidException(msg);
        }
    }

}
