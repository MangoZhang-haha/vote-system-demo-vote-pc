package flybear.hziee.app.pojo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import flybear.hziee.app.pojo.OwnerInfo;
import org.apache.ibatis.annotations.Param;

/**
 * @author Mango
 * @Date: 2021/3/20 19:59:19
        */
public interface OwnerInfoMapper extends BaseMapper<OwnerInfo> {

    /**
     * 获取业主详情
     * @param ownerID
     * @return
     */
    OwnerInfo queryOwnerInfoByOwnerID(@Param("ownerID") Long ownerID);
}
