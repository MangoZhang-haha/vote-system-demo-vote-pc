package flybear.hziee.app.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import flybear.hziee.app.constant.CommonConstant;
import flybear.hziee.app.entity.Result;
import flybear.hziee.app.entity.Vote;
import flybear.hziee.app.entity.VoteCandidate;
import flybear.hziee.app.entity.VoteRecords;
import flybear.hziee.app.pojo.CandidateInfo;
import flybear.hziee.app.pojo.VoteTotalInfo;
import flybear.hziee.app.service.PublicService;
import flybear.hziee.app.service.VoteCandidateService;
import flybear.hziee.app.service.VoteRecordsService;
import flybear.hziee.app.service.VoteService;
import flybear.hziee.app.util.ResultUtil;
import flybear.hziee.app.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Mango
 * @Date: 2021/3/24 15:51:16
 */
@RestController
@RequestMapping("/candidate")
public class CandidateController {

    /**
     * 只能投票一次
     */
    private static final Long ONLY_VOTE_ONCE = 1L;

    /**
     * 每天可投一次
     */
    private static final Long VOTE_ONCE_PER_DAY = 2L;

    @Autowired
    private VoteService voteService;

    @Autowired
    private VoteCandidateService voteCandidateService;

    @Autowired
    private PublicService publicService;

    @Autowired
    private VoteRecordsService voteRecordsService;

    @GetMapping("/getCandidateDetail/{candidateTableID}")
    public Result getCandidateInfo(@PathVariable("candidateTableID") Long candidateTableID) {
        VoteCandidate candidate = voteCandidateService.getOne(
                Wrappers.lambdaQuery(VoteCandidate.class)
                        .eq(VoteCandidate::getId, candidateTableID)
        );
        CandidateInfo candidateInfo = new CandidateInfo();
        candidateInfo.setCandidateTableID(candidateTableID);
        candidateInfo.setCandidateName(candidate.getCandidateName());
        candidateInfo.setIntroduce(candidate.getIntroduce());
        if (StringUtils.isNotEmpty(candidate.getPicUrls())) {
            String[] urls = candidate.getPicUrls().split(CommonConstant.PIC_URLS_SPLIT_CHARS);
            for (int i = 0; i < urls.length; i++) {
                urls[i] = publicService.getStaticResPrefixUrl() + urls[i];
            }
            candidateInfo.setPicUrls(Arrays.asList(urls));
        }
        int count = voteRecordsService.count(
                Wrappers.lambdaQuery(VoteRecords.class)
                        .eq(VoteRecords::getVoteCandidateTableId, candidateTableID)
        );
        candidateInfo.setVoteNum(count);
        return ResultUtil.success(candidateInfo);
    }

    @GetMapping("/{voteID}")
    public Result getAllCandidates(@PathVariable("voteID") Long voteID) {
        Vote vote = voteService.getById(voteID);
        Date now = new Date();
        vote.setEndOrNot(vote.getEndTime().getTime() < now.getTime());
        VoteTotalInfo voteTotalInfo = new VoteTotalInfo();
        voteTotalInfo.setVote(vote);

        List<VoteCandidate> list = voteCandidateService.list(
                Wrappers.lambdaQuery(VoteCandidate.class)
                        .eq(VoteCandidate::getVoteId, voteID)
        );

        List<VoteTotalInfo.CandidateInfo> candidateInfos = new ArrayList<>();
        list.forEach(voteCandidate -> {
            VoteTotalInfo.CandidateInfo candidateInfo = new VoteTotalInfo.CandidateInfo();
            candidateInfo.setCandidateTableID(voteCandidate.getId());
            candidateInfo.setCandidateName(voteCandidate.getCandidateName());
            if (StringUtils.isNotEmpty(voteCandidate.getPicUrls())) {
                String[] split = voteCandidate.getPicUrls().split(CommonConstant.PIC_URLS_SPLIT_CHARS);
                String picUrl = publicService.getStaticResPrefixUrl() + split[0];
                candidateInfo.setPicUrl(picUrl);
            }
            candidateInfo.setVoteNum(voteRecordsService.count(
                    Wrappers.lambdaQuery(VoteRecords.class)
                            .eq(VoteRecords::getVoteCandidateTableId, voteCandidate.getId())
            ));
            candidateInfos.add(candidateInfo);
        });
        voteTotalInfo.setCandidateInfos(candidateInfos);
        return ResultUtil.success(voteTotalInfo);
    }

    @PostMapping("/voteForCandidate")
    public Result voteForCandidate(@RequestParam("userID") Long userID,
                                   @RequestParam("candidateTableID") Long candidateTableID) {
        VoteCandidate voteCandidate = voteCandidateService.getById(candidateTableID);
        Vote vote = voteService.getById(voteCandidate.getVoteId());
        if (vote.getVoteLimitId().equals(ONLY_VOTE_ONCE)) {
            int count = voteRecordsService.count(
                    Wrappers.lambdaQuery(VoteRecords.class)
                            .eq(VoteRecords::getVoteId, voteCandidate.getVoteId())
                            .eq(VoteRecords::getOwnerId, userID)
            );
            if (count > 0) {
                return ResultUtil.error("您已经投过票了,请勿重复投票");
            }
        } else if (vote.getVoteLimitId().equals(VOTE_ONCE_PER_DAY)){
            int count = voteRecordsService.count(
                    Wrappers.lambdaQuery(VoteRecords.class)
                            .eq(VoteRecords::getVoteId, voteCandidate.getVoteId())
                            .eq(VoteRecords::getOwnerId, userID)
                            .like(VoteRecords::getGmtCreate, TimeUtil.formatTime("yyyy-MM-dd"))
            );
            if (count > 0) {
                return ResultUtil.error("您今天已经投过票了,请勿重复投票");
            }
        } else {
            return ResultUtil.error("未知投票类型");
        }
        voteRecordsService.voteForCandidate(userID, candidateTableID);
        return ResultUtil.success();
    }
}
