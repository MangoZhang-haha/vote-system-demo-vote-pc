package flybear.hziee.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import flybear.hziee.app.entity.*;
import flybear.hziee.app.service.UserService;
import flybear.hziee.app.service.VoteEvService;
import flybear.hziee.app.service.VoteLimitService;
import flybear.hziee.app.service.VoteService;
import flybear.hziee.app.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mango
 * @Date: 2021/4/1 23:59:48
 */
@RestController
@RequestMapping("/ev")
public class VoteEvController {

    @Autowired
    private VoteEvService voteEvService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private VoteLimitService voteLimitService;

    @Autowired
    private UserService userService;

    /**
     *
     * @param type 3 全部  0 审核中 1 已通过 2 未通过
     * @param pageSize
     * @param pageNum
     * @return
     */
    @GetMapping
    public Result list(@RequestParam(value = "type", defaultValue = "3") Integer type,
                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                       @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<VoteEv> voteEvs = new ArrayList<>();
        LambdaQueryWrapper<VoteEv> wrapper = new LambdaQueryWrapper<>();
        if (type.equals(0)) {
            wrapper.eq(VoteEv::getApplyStatus, 0);
        } else if (type.equals(1)) {
            wrapper.eq(VoteEv::getApplyStatus, 1);
        } else if (type.equals(2)) {
            wrapper.eq(VoteEv::getApplicationId, 2);
        } else if (!type.equals(3)) {
            return ResultUtil.error("type不合法");
        }
        voteEvs = voteEvService.list(wrapper);
        PageInfo<VoteEv> pageInfo = new PageInfo<>(voteEvs);
        if (voteEvs.size() == 0) {
            return ResultUtil.success(pageInfo);
        }

        List<VoteLimit> voteLimits = voteLimitService.list();
        Map<Long, String> voteLimitMap = new HashMap<>(voteLimits.size());
        voteLimits.forEach(voteLimit -> {
            voteLimitMap.put(voteLimit.getId(), voteLimit.getVoteLimitName());
        });

        List<Long> voteIDs = new ArrayList<>();
        voteEvs.forEach(voteEv -> voteIDs.add(voteEv.getVoteId()));
        List<Vote> votes = voteService.listByIds(voteIDs);
        Map<Long, Vote> voteMap = new HashMap<>(votes.size());
        votes.forEach(vote -> {
            voteMap.put(vote.getId(), vote);
        });
        voteEvs.forEach(voteEv -> {
            Vote vote = voteMap.get(voteEv.getVoteId());
            voteEv.setTitle(vote.getTitle());
            voteEv.setStartTime(vote.getStartTime());
            voteEv.setEndTime(vote.getEndTime());
            voteEv.setVoteTypeName(voteLimitMap.get(vote.getVoteLimitId()));
        });
        pageInfo.setList(voteEvs);
        return ResultUtil.success(pageInfo);
    }

    /**
     * @param type 1同意 2 拒绝
     * @param voteEvID
     * @return
     */
    @PutMapping("/{voteEvID}/{type}/{userID}")
    public Result operate(@PathVariable("voteEvID") Long voteEvID,
                          @PathVariable("type") Integer type,
                          @PathVariable("userID") Long userID) {
        if (!type.equals(1) && !type.equals(2)) {
            return ResultUtil.error("type不合法");
        }
        voteEvService.operate(voteEvID, type, userID);
        return ResultUtil.success("操作成功");
    }

    @GetMapping("/{voteEvID}")
    public Result view(@PathVariable("voteEvID") Long voteEvID) {
        VoteEv voteEv = voteEvService.getById(voteEvID);
        if (voteEv != null) {
            Vote vote = voteService.getById(voteEv.getVoteId());
            User user = userService.getById(voteEv.getOperatorId());
            VoteLimit voteLimit = voteLimitService.getById(vote.getVoteLimitId());
            voteEv.setVoteTypeName(voteLimit == null ? null : voteLimit.getVoteLimitName());
            if (vote != null) {
                voteEv.setTitle(vote.getTitle());
                voteEv.setStartTime(vote.getStartTime());
                voteEv.setEndTime(vote.getEndTime());
            }
            voteEv.setOperatorName(user == null ? null : user.getUsername());
        }
        return ResultUtil.success(voteEv);
    }
}
