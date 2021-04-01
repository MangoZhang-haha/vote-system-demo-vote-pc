package flybear.hziee.app.service;

import flybear.hziee.app.entity.VoteEv;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

public interface VoteEvService extends IService<VoteEv> {

    @Transactional(rollbackFor = Exception.class)
    Integer operate(Long voteEvID, Integer type, Long userID);
}


