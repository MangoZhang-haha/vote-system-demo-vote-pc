package flybear.hziee.app.service.impl;

import flybear.hziee.app.constant.CommonConstant;
import flybear.hziee.app.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import flybear.hziee.app.mapper.VoteCandidateMapper;
import flybear.hziee.app.entity.VoteCandidate;
import flybear.hziee.app.service.VoteCandidateService;
@Service
public class VoteCandidateServiceImpl extends ServiceImpl<VoteCandidateMapper, VoteCandidate> implements VoteCandidateService{

    @Autowired
    private VoteCandidateMapper voteCandidateMapper;

    @Override
    public Integer delCan(Long candidateTableID) {
        VoteCandidate voteCandidate = voteCandidateMapper.selectById(candidateTableID);
        String[] item = voteCandidate.getPicUrls().split(CommonConstant.PIC_URLS_SPLIT_CHARS);
        for (int i = 0; i < item.length; i++) {
            FileUtils.deleteFile(item[i]);
        }
        voteCandidateMapper.deleteById(candidateTableID);
        return 1;
    }
}
