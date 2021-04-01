package flybear.hziee.app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * @author Mango
 * @Date: 2021/3/20 14:55:05
 */
@Slf4j
@Configuration
public class StaticFilePathConfig {

    @Value("${file.root}")
    public String rootPath;

    @Value("${file.avatar}")
    public String avatarPath;

    @Value("${file.tmp}")
    public String tmpPath;

    @Value("${file.document}")
    public String documentPath;

    @PostConstruct
    public void init() {
        log.info("************** 文件目录初始化开始 **************");

        //项目根目录
        File root = new File(rootPath);
        if (!root.exists()) {
            boolean flag = root.mkdirs();
            if (flag) {
                root.setExecutable(true);
                root.setReadable(true);
                root.setWritable(true);
                log.info(root.getAbsolutePath() + "创建成功");
            } else {
                log.error(root.getAbsolutePath() + "创建失败");
            }
        } else {
            log.info(root.getAbsolutePath() + "已经存在");
        }

        //头像目录
        File avatar = new File(avatarPath);
        if (!avatar.exists()) {
            boolean flag = avatar.mkdirs();
            if (flag) {
                avatar.setExecutable(true);
                avatar.setReadable(true);
                avatar.setWritable(true);
                log.info(avatar.getAbsolutePath() + "创建成功");
            } else {
                log.error(avatar.getAbsolutePath() + "创建失败");
            }
        } else {
            log.info(avatar.getAbsolutePath() + "已经存在");
        }

        //临时文件
        File tmp = new File(tmpPath);
        if (!tmp.exists()) {
            boolean flag = tmp.mkdirs();
            if (flag) {
                tmp.setExecutable(true);
                tmp.setReadable(true);
                tmp.setWritable(true);
                log.info(tmp.getAbsolutePath() + "创建成功");
            } else {
                log.error(tmp.getAbsolutePath() + "创建失败");
            }
        } else {
            log.info(tmp.getAbsolutePath() + "已经存在");
        }

        //正式存储文件
        File document = new File(documentPath);
        if (!document.exists()) {
            boolean flag = document.mkdirs();
            if (flag) {
                document.setExecutable(true);
                document.setReadable(true);
                document.setWritable(true);
                log.info(document.getAbsolutePath() + "创建成功");
            } else {
                log.error(document.getAbsolutePath() + "创建失败");
            }
        } else {
            log.info(document.getAbsolutePath() + "已经存在");
        }

        log.info("************** 文件目录初始化结束 **************");
    }
}
