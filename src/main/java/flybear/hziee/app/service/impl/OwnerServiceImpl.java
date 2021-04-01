package flybear.hziee.app.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import flybear.hziee.app.entity.Owner;
import flybear.hziee.app.mapper.OwnerMapper;
import flybear.hziee.app.service.OwnerService;
@Service
public class OwnerServiceImpl extends ServiceImpl<OwnerMapper, Owner> implements OwnerService{

}
