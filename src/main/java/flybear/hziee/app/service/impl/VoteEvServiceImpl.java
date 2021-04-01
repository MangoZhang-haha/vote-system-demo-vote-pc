package flybear.hziee.app.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import flybear.hziee.app.entity.VoteEv;
import flybear.hziee.app.mapper.VoteEvMapper;
import flybear.hziee.app.service.VoteEvService;
@Service
public class VoteEvServiceImpl extends ServiceImpl<VoteEvMapper, VoteEv> implements VoteEvService{

}
