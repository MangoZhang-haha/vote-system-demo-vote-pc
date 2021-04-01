package flybear.hziee.app.service;

import flybear.hziee.app.entity.VoteCandidate;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

public interface VoteCandidateService extends IService<VoteCandidate>{

    @Transactional(rollbackFor = Exception.class)
    Integer delCan(Long candidateTableID);
}
