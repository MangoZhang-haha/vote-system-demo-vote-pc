package flybear.hziee.app.util;

import cn.hutool.core.io.FileUtil;
import flybear.hziee.app.constant.CommonConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Mango
 * @Date: 2021/3/22 15:10:44
 */
@Configuration
public class FileUtils {

    public static String TMP_PATH;
    @Value("${file.tmp}")
    public void setTmpPath(String tmpPath) {
        TMP_PATH = tmpPath;
    }

    public static String DOCUMENT_PATH;
    @Value("${file.document}")
    public void setDocumentPath(String documentPath) {
        DOCUMENT_PATH = documentPath;
    }

    public static String APPLICATION_NAME;
    @Value("${spring.application.name}")
    public void setApplicationName(String name) {
        APPLICATION_NAME = name;
    }

    public static String uploadTmp(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String realName = UUID.randomUUID() + suffix;
        File tmp = new File(TMP_PATH, realName);
        org.apache.commons.io.FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), tmp);
        String path = tmp.getAbsolutePath();
        path = path.substring(path.indexOf(APPLICATION_NAME) - File.separator.length());
        return path;
    }

    public static void deleteTmp(String path) {
        String fileName = path.substring(path.lastIndexOf(File.separator) + 1);
        File tmp = new File(TMP_PATH, fileName);
        if (tmp.exists()) {
            tmp.delete();
        }
    }

    public static String moveToDocument(String url) {
        if (StringUtils.isNotEmpty(url)) {
            File document = new File(DOCUMENT_PATH, TimeUtil.formatTime("yyyy-MM-dd"));
            if (!document.exists()) {
                document.mkdirs();
            }
            List<String> singleUrl = new ArrayList<>();
            List<String> picUrls = Arrays.stream(url.split(CommonConstant.PIC_URLS_SPLIT_CHARS)).map(String::valueOf).collect(Collectors.toList());
            picUrls.forEach(s -> {
                s = s.substring(s.lastIndexOf(File.separator) + 1);
                System.out.println("s = " + s);
                File item = new File(document, s);
                FileUtil.move(new File(TMP_PATH, s), item, true);
                String absPath = item.getPath();
                singleUrl.add(absPath.substring(absPath.indexOf(APPLICATION_NAME) - File.separator.length()));
            });
            return StringUtils.join(singleUrl, CommonConstant.PIC_URLS_SPLIT_CHARS);
        }
        return "";
    }
}
