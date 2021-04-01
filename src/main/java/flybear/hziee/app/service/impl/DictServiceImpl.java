package flybear.hziee.app.service.impl;

import flybear.hziee.app.entity.Dict;
import flybear.hziee.app.mapper.DictMapper;
import flybear.hziee.app.service.DictService;
import org.springframework.stereotype.Service;

/**
 * 字典表服务实现类
 *
 * @author flybear
 * @since 2020/01/01 19:09
 */
@Service("dictService")
public class DictServiceImpl extends BaseServiceImpl<DictMapper, Dict> implements DictService {
}
