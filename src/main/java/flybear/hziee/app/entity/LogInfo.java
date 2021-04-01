package flybear.hziee.app.entity;

import flybear.hziee.app.enums.LogModuleEnum;
import lombok.Data;

/**
 * 日志注解信息实体类
 *
 * @author qiancj
 * @since 2020-03-21 15:15
 */
@Data
public class LogInfo {

    /**
     * 描述
     */
    private String description;

    /**
     * 是否需要记录日志
     */
    private boolean needRecord;

    /**
     * 日志记录模块
     */
    private LogModuleEnum module;

}
