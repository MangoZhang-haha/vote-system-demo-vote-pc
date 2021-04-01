package flybear.hziee.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import flybear.hziee.app.entity.Vote;
import flybear.hziee.app.entity.VoteCandidate;
import flybear.hziee.app.entity.VoteRecords;
import flybear.hziee.app.mapper.VoteCandidateMapper;
import flybear.hziee.app.mapper.VoteMapper;
import flybear.hziee.app.mapper.VoteRecordsMapper;
import flybear.hziee.app.service.VoteService;
import flybear.hziee.app.util.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class VoteServiceImpl extends ServiceImpl<VoteMapper, Vote> implements VoteService {

    @Autowired
    private VoteMapper voteMapper;
    @Autowired
    private VoteCandidateMapper voteCandidateMapper;

    @Autowired
    private VoteRecordsMapper voteRecordsMapper;

    @Override
    public String getNoticedIDs(String json) {
        List<Long> ownerIDs = new ArrayList<>();
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONArray array = JSONObject.parseArray(JSON.toJSONString(jsonObject.get("range")));
        array.forEach(community -> {
            JSONObject communityInfo = JSONObject.parseObject(community.toString());
            calCommunity(communityInfo, ownerIDs);
        });
        return StringUtils.join(ownerIDs, ",");
    }

    @Override
    public Integer createVote(Vote vote, List<VoteCandidate> candidateList) {
        voteMapper.insert(vote);
        for (VoteCandidate candidate : candidateList) {
            candidate.setVoteId(vote.getId());
            candidate.setPicUrls(FileUtils.moveToDocument(candidate.getPicUrls()));
        }
        return voteCandidateMapper.insertList(candidateList, new Date());
    }

    @Override
    public Integer deleteVote(Long voteID) {
        voteMapper.deleteById(voteID);

        voteCandidateMapper.delete(
                Wrappers.lambdaQuery(VoteCandidate.class)
                        .eq(VoteCandidate::getVoteId, voteID)
        );

        voteRecordsMapper.delete(
                Wrappers.lambdaQuery(VoteRecords.class)
                        .eq(VoteRecords::getVoteId, voteID)
        );
        return 1;
    }

    public void calCommunity(JSONObject jsonObject, List<Long> ownerIDs) {
        if (JSONObject.parseArray(JSONObject.toJSONString(jsonObject.get("children"))) == null) {
            return;
        }
        JSONArray communityChildren = JSONObject.parseArray(JSONObject.toJSONString(jsonObject.get("children")));
        if (Boolean.parseBoolean(jsonObject.get("checked").toString())) {
            communityChildren.forEach(building -> {
                JSONObject buildingInfo = JSONObject.parseObject(building.toString());
                calBuilding(true, buildingInfo, ownerIDs);
            });
        } else {
            communityChildren.forEach(building -> {
                JSONObject buildingInfo = JSONObject.parseObject(building.toString());
                calBuilding(Boolean.parseBoolean(buildingInfo.getString("checked")), buildingInfo, ownerIDs);
            });
        }
    }

    public void calBuilding(Boolean flag, JSONObject jsonObject, List<Long> ownerIDs) {
        if (JSONObject.parseArray(JSONObject.toJSONString(jsonObject.get("children"))) == null) {
            return;
        }
        JSONArray buildingChildren = JSONObject.parseArray(JSONObject.toJSONString(jsonObject.get("children")));
        if (flag) {
            buildingChildren.forEach(unit -> {
                JSONObject unitInfo = JSONObject.parseObject(unit.toString());
                calUnit(true, unitInfo, ownerIDs);
            });
        } else {
            buildingChildren.forEach(unit -> {
                JSONObject unitInfo = JSONObject.parseObject(unit.toString());
                calUnit(Boolean.parseBoolean(unitInfo.get("checked").toString()), unitInfo, ownerIDs);
            });
        }
    }

    public void calUnit(Boolean flag, JSONObject jsonObject, List<Long> ownerIDs) {
        if (JSONObject.parseArray(JSONObject.toJSONString(jsonObject.get("children"))) == null) {
            return;
        }
        JSONArray children = JSONObject.parseArray(JSONObject.toJSONString(jsonObject.get("children")));
        if (flag) {
            children.forEach(child -> {
                JSONObject houseInfo = JSONObject.parseObject(child.toString());
                ownerIDs.add(Long.parseLong(houseInfo.get("ownerID").toString()));
            });
        } else {
            children.forEach(child -> {
                JSONObject houseInfo = JSONObject.parseObject(child.toString());
                if (Boolean.parseBoolean(houseInfo.get("checked").toString())) {
                    ownerIDs.add(Long.parseLong(houseInfo.get("ownerID").toString()));
                }
            });
        }
    }
}


