package flybear.hziee.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import flybear.hziee.app.entity.Owner;
import flybear.hziee.app.entity.Result;
import flybear.hziee.app.entity.VoteCandidate;
import flybear.hziee.app.entity.VoteRecords;
import flybear.hziee.app.pojo.VoteSituation;
import flybear.hziee.app.service.OwnerService;
import flybear.hziee.app.service.VoteCandidateService;
import flybear.hziee.app.service.VoteRecordsService;
import flybear.hziee.app.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mango
 * @Date: 2021/4/2 3:02:03
 */
@RestController
@RequestMapping("/records")
public class VoteRecordsController {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private VoteRecordsService voteRecordsService;

    @Autowired
    private VoteCandidateService voteCandidateService;

    /**
     * 默认全是小程序投票
     * @param key
     * @param voteID
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/{voteID}")
    public Result query(@RequestParam(value = "key", defaultValue = "") String key,
                        @PathVariable("voteID") Long voteID,
                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                        @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        List<Long> ownerIDs = new ArrayList<>();
        LambdaQueryWrapper<Owner> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(key)) {
            if (isNumeric(key)) {
                queryWrapper.eq(Owner::getIdNumber, key);
            } else {
                queryWrapper.like(Owner::getOwnerName, key);
            }
            List<Owner> owners = ownerService.list(queryWrapper);
            if (owners.size() == 0) {
                return ResultUtil.success(null);
            }
            owners.forEach(owner -> {
                ownerIDs.add(owner.getId());
            });
        }
        PageHelper.startPage(pageNum, pageSize);
        List<VoteRecords> list = voteRecordsService.list(
                Wrappers.lambdaQuery(VoteRecords.class)
                        .in(ownerIDs.size() !=0,  VoteRecords::getOwnerId, ownerIDs)
                        .eq(VoteRecords::getVoteId, voteID)
                        .orderByDesc(VoteRecords::getGmtCreate)
        );
        PageInfo<VoteRecords> pageInfo = new PageInfo<>(list);
        if (list.size() == 0) {
            return ResultUtil.success(pageInfo);
        }
        ownerIDs.clear();
        List<Long> candidateTableIDs = new ArrayList<>();
        list.forEach(voteRecords -> {
            ownerIDs.add(voteRecords.getOwnerId());
            candidateTableIDs.add(voteRecords.getVoteCandidateTableId());
        });
        List<Owner> owners = ownerService.listByIds(ownerIDs);
        Map<Long, Owner> ownerMap = new HashMap<>(ownerIDs.size());
        owners.forEach(owner -> {
            ownerMap.put(owner.getId(), owner);
        });
        List<VoteCandidate> voteCandidates = voteCandidateService.listByIds(candidateTableIDs);
        Map<Long, VoteCandidate> voteCandidateMap = new HashMap<>(voteCandidates.size());
        voteCandidates.forEach(
                voteCandidate -> {
                    voteCandidateMap.put(voteCandidate.getId(), voteCandidate);
                }
        );
        List<VoteSituation> voteSituations = new ArrayList<>();
        list.forEach(voteRecords -> {
            VoteSituation voteSituation = new VoteSituation();
            voteSituation.setOwnerID(voteRecords.getOwnerId());
            voteSituation.setOwnerName(ownerMap.get(voteRecords.getOwnerId()).getOwnerName());
            voteSituation.setAvatarUrl(ownerMap.get(voteRecords.getOwnerId()).getAvatarUrl());
            voteSituation.setIdNumber(ownerMap.get(voteRecords.getOwnerId()).getIdNumber());
            voteSituation.setVoteTime(voteRecords.getGmtCreate());
            voteSituation.setCandidateName(voteCandidateMap.get(voteRecords.getVoteCandidateTableId()).getCandidateName());
            voteSituations.add(voteSituation);
        });
        PageInfo<VoteSituation> result = new PageInfo<>();
        result.setList(voteSituations);
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setPages(pageInfo.getPages());
        return ResultUtil.success(result);
    }

    public Boolean isNumeric(String str) {
        String patternStr = "[0-9]{1,}";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher((CharSequence) str);
        return matcher.matches();
    }
}
