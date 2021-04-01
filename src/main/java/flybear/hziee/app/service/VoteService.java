package flybear.hziee.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import flybear.hziee.app.entity.Vote;
import flybear.hziee.app.entity.VoteCandidate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface VoteService extends IService<Vote> {

    String getNoticedIDs(String json);

    @Transactional(rollbackFor = Exception.class)
    Integer createVote(Vote vote, List<VoteCandidate> candidateList);

    @Transactional(rollbackFor = Exception.class)
    Integer deleteVote(Long voteID);
}