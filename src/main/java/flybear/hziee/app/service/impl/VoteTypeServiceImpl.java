package flybear.hziee.app.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import flybear.hziee.app.mapper.VoteTypeMapper;
import flybear.hziee.app.entity.VoteType;
import flybear.hziee.app.service.VoteTypeService;
@Service
public class VoteTypeServiceImpl extends ServiceImpl<VoteTypeMapper, VoteType> implements VoteTypeService{

}
