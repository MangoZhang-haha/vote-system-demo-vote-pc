package flybear.hziee.app.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @author Mango
 * @Date: 2021/3/22 19:36:17
 */
@Component
public class FileTask {

    /**
     * 最长未操作时间 30分钟
     */
    private static final Long MAX_NO_OPERATION_TIME = 1000 * 60 * 30L;

    private static String TMP_PATH;
    @Value("${file.tmp}")
    private void setTmpPath(String tmpPath) {
        TMP_PATH = tmpPath;
    }

    /**
     * 定时删除tmp里面的文件
     * 每十二小时
     */
    @Scheduled(cron = "0 0 */12 * * ?")
    public void deleteTmp() {
        Date now = new Date();
        File tmpFolder = new File(TMP_PATH);
        File[] files = tmpFolder.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (now.getTime() - files[i].lastModified() > MAX_NO_OPERATION_TIME) {
                files[i].delete();
            }
        }
    }
}
