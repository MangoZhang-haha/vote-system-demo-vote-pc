package flybear.hziee.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.regex.Matcher;

/**
 * 文件上传配置类
 *
 * @author flybear
 * @since 2019/12/22 21:27
 */
@Configuration
public class FilePathConfig {

    public static String REPOSITORY;
    @Value("${file-path.repository}")
    public void setRepository(String repository) {
        REPOSITORY = addSuffixFileSeparator(repository);
    }

    public static String URL_PATTERN;
    @Value("${file-path.url}")
    public void setUrlPattern(String urlPattern) {
        URL_PATTERN = getUrlPath(addFileSeparator(urlPattern));
    }

    public static String TEMP_PATH;
    @Value("${file-path.temp-path}")
    public void setTempPath(String tempPath) {
        TEMP_PATH = addFileSeparator(tempPath);
    }

    /**
     * 路径前添加文件分隔符，并且处理分隔符
     *
     * @param path 路径
     * @return 有末尾文件分隔符的路径
     */
    public static String addFileSeparator(String path) {
        return addSuffixFileSeparator(addPrefixFileSeparator(path));
    }

    /**
     * 路径前添加文件分隔符，并且处理分隔符
     *
     * @param path 路径
     * @return 有末尾文件分隔符的路径
     */
    public static String addPrefixFileSeparator(String path) {
        path = getFilePath(path);
        return path.startsWith(File.separator) ? path : File.separator + path;
    }

    /**
     * 路径后添加文件分隔符，并且处理分隔符
     *
     * @param path 路径
     * @return 有末尾文件分隔符的路径
     */
    public static String addSuffixFileSeparator(String path) {
        path = getFilePath(path);
        return path.endsWith(File.separator) ? path : path + File.separator;
    }

    /**
     * 获取绝对路径
     *
     * @param path 路径
     * @return 绝对路径
     */
    public static String getAbsolutePath(String path) {
        path = REPOSITORY + FilePathConfig.getFilePath(path);
        return path.replaceAll(Matcher.quoteReplacement(File.separator + File.separator),
                Matcher.quoteReplacement(File.separator));
    }

    /**
     * 获取URL形式的路径
     *
     * @param path 路径
     * @return URL形式的路径
     */
    public static String getUrlPath(String path) {
        return path.replaceAll(Matcher.quoteReplacement(File.separator), "/").replaceAll("//", "/");
    }

    /**
     * 获取文件形式的路径
     *
     * @param path 路径
     * @return 文件形式的路径
     */
    public static String getFilePath(String path) {
        return path.replaceAll("/", Matcher.quoteReplacement(File.separator));
    }
}
