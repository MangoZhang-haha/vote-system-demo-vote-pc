package flybear.hziee.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import flybear.hziee.app.entity.VoteRecords;

public interface VoteRecordsService extends IService<VoteRecords>{

    Integer voteForCandidate(Long userID, Long candidateTableID);
}
