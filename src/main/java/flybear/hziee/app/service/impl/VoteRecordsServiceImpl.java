package flybear.hziee.app.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import flybear.hziee.app.entity.VoteCandidate;
import flybear.hziee.app.entity.VoteRecords;
import flybear.hziee.app.mapper.VoteCandidateMapper;
import flybear.hziee.app.mapper.VoteRecordsMapper;
import flybear.hziee.app.service.VoteRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteRecordsServiceImpl extends ServiceImpl<VoteRecordsMapper, VoteRecords> implements VoteRecordsService {

    @Autowired
    private VoteCandidateMapper voteCandidateMapper;

    @Autowired
    private VoteRecordsMapper voteRecordsMapper;

    @Override
    public Integer voteForCandidate(Long userID, Long candidateTableID) {
        VoteCandidate voteCandidate = voteCandidateMapper.selectById(candidateTableID);
        VoteRecords voteRecords = VoteRecords.builder()
                .voteId(voteCandidate.getVoteId())
                .voteCandidateTableId(candidateTableID)
                .ownerId(userID)
                .build();
        voteRecordsMapper.insert(voteRecords);
        return 1;
    }
}
