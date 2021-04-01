package flybear.hziee.app.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import flybear.hziee.app.mapper.VoteRecordsMapper;
import flybear.hziee.app.entity.VoteRecords;
import flybear.hziee.app.service.VoteRecordsService;
@Service
public class VoteRecordsServiceImpl extends ServiceImpl<VoteRecordsMapper, VoteRecords> implements VoteRecordsService{

}
