package flybear.hziee.app.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import flybear.hziee.app.entity.VoteLimit;
import flybear.hziee.app.mapper.VoteLimitMapper;
import flybear.hziee.app.service.VoteLimitService;
@Service
public class VoteLimitServiceImpl extends ServiceImpl<VoteLimitMapper, VoteLimit> implements VoteLimitService{

}
