package flybear.hziee.app.config.mp;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 字段自动填充功能
 *
 * @author flybear
 * @since 2020/1/7 10:35
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    private static final String GMT_CRATE = "gmtCreate";
    private static final String GMT_MODIFIED = "gmtModified";

    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.hasGetter(GMT_CRATE) && metaObject.hasGetter(GMT_MODIFIED)) {
            Date now = new Date();
            setFieldValByName(GMT_CRATE, now, metaObject);
            setFieldValByName(GMT_MODIFIED, now, metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasGetter(GMT_MODIFIED)) {
            setFieldValByName(GMT_MODIFIED, new Date(), metaObject);
        }
    }
}
