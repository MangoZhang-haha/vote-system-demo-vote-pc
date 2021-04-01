package flybear.hziee.app.task;

import flybear.hziee.app.config.FilePathConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 定时清理上传的缓存文件垃圾
 *
 * @author qiancj
 * @since 2020-01-14 01:40
 */
@Slf4j
@Component
public class FileCleanUpTask {

    /**
     * 需要忽略的文件名
     */
    private static final List<String> EXCLUDE_FILENAME_LIST = Arrays.asList(".DS_Store");

    /**
     * 是否定期删除缓存文件
     */
    @Value("${file.clean-cache:false}")
    private boolean cleanCache;
    /**
     * 缓存文件保存时间，单位（天）
     */
    @Value("${file.temp-file-save-date:2}")
    private int tempFileSaveDate;

    /**
     * 每天凌晨一点定时清理缓存文件
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void startCleanUp() {
        if (cleanCache && tempFileSaveDate >= 0) {
            // 默认删除两天前缓存
            Calendar now = Calendar.getInstance();
            now.add(Calendar.DATE, -tempFileSaveDate);
            // 日期目录文件名格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

            File tempDir = new File(FilePathConfig.getAbsolutePath(FilePathConfig.TEMP_PATH));
            if (tempDir.exists()) {
                for (File file : getFiles(tempDir)) {
                    // 忽略某些文件
                    if (EXCLUDE_FILENAME_LIST.contains(file.getName())) {
                        continue;
                    }
                    // 父文件为日期格式的目录 日期格式为yyyyMMdd
                    String dateStr = file.getParentFile().getName();
                    try {
                        Date date = sdf.parse(dateStr);
                        if (date.before(now.getTime())) {
                            // 删除缓存文件
                            file.delete();
                        }
                    } catch (ParseException e) {
                        log.error("定时删除缓存文件异常，无法格式化日期 \"" + dateStr + "\"：{}", e);

                    }
                }

            }
        }
    }

    /**
     * 遍历获取所有子文件
     *
     * @param dir 目录
     * @return 所有子文件
     */
    private static List<File> getFiles(File dir) {
        List<File> files = new ArrayList<>();
        if (dir.exists() && dir.isDirectory()) {
            File[] children = dir.listFiles();
            if (children != null) {
                for (File file : children) {
                    if (file.isDirectory()) {
                        files.addAll(getFiles(file));
                    } else {
                        files.add(file);
                    }
                }
            }
        }
        return files;
    }

}
