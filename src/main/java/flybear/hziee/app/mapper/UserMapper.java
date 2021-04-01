package flybear.hziee.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import flybear.hziee.app.entity.User;
import flybear.hziee.app.entity.dto.UserInfo;

/**
 * @author flybear
 * @since 2020/1/6 11:04
 */
public interface UserMapper extends BaseMapper<User> {
    UserInfo getUserInfo(Long id);
}
