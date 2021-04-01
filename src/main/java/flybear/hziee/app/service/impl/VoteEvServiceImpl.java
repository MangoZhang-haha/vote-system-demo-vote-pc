package flybear.hziee.app.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import flybear.hziee.app.entity.VoteEv;
import flybear.hziee.app.mapper.VoteEvMapper;
import flybear.hziee.app.service.VoteEvService;

@Service
public class VoteEvServiceImpl extends ServiceImpl<VoteEvMapper, VoteEv> implements VoteEvService {

    @Autowired
    private VoteEvMapper voteEvMapper;

    @Override
    public Integer operate(Long voteEvID, Integer type, Long userID) {
        voteEvMapper.update(
                null,
                Wrappers.lambdaUpdate(VoteEv.class)
                        .set(VoteEv::getApplyStatus, type)
                        .set(VoteEv::getOperatorId, userID)
                        .eq(VoteEv::getId, voteEvID)
        );
        return 1;
    }
}


