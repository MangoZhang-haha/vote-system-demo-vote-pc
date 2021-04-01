package flybear.hziee.app.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import flybear.hziee.app.mapper.VoteMapper;
import flybear.hziee.app.entity.Vote;
import flybear.hziee.app.service.VoteService;
@Service
public class VoteServiceImpl extends ServiceImpl<VoteMapper, Vote> implements VoteService{

}
