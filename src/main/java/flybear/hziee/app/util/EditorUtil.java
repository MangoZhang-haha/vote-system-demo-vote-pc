package flybear.hziee.app.util;

import flybear.hziee.app.config.FilePathConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 富文本常用类
 *
 * @author qiancj
 * @date 2019/12/17 22:55
 */
public class EditorUtil {

    /**
     * 服务器图片src正则，匹配src为 $ 开头的
     */
    private static final Pattern IMG_SRC_PATTERN = Pattern.compile("<img src=\"\\$(.*?)\"");


    /**
     * 移动富文本中的缓存文件到正式文件夹中
     *
     * @param content 富文本框内容
     * @return 移动后的图片访问url
     */
    public static List<String> moveImages(String content) {
        List<String> fileUrl = new ArrayList<>();
        Matcher m = IMG_SRC_PATTERN.matcher(content);
        while (m.find()) {
            // 匹配到的img标签的src属性
            String src = m.group(1);
            if (FileUploadUtil.isTempUrl(src)) {
                fileUrl.add(FileUploadUtil.moveTempFileFromFileUrl(src));
            } else {
                // 如果是原先就存在的，那么不需要移回缓存文件夹
                fileUrl.add(null);
            }
        }
        return fileUrl;
    }

    /**
     * 去掉缓存路径，并且src以 $ 开头
     *
     * @param content 富文本内容
     * @return 去掉缓存路径，并且src以 $ 开头的富文本内容
     */
    public static String parseContent(String content) {
        content = content.replaceAll("<img src=\"\\$" + FilePathConfig.getUrlPath(FilePathConfig.TEMP_PATH),
                "<img src=\"\\$/");
        return content;
    }

    /**
     * 删除失效的图片
     *
     * @param oldContent 旧的富文本内容
     * @param newContent 新的富文本内容
     */
    public static void deleteInvalidImages(String oldContent, String newContent) {
        List<String> oldFiles = getRelativeImageSrc(oldContent);
        List<String> newFiles = getRelativeImageSrc(newContent);
        // 剩下的是需要删除的图片
        oldFiles.removeAll(newFiles);
        for (String src : oldFiles) {
            FileUploadUtil.deleteFile(src);
        }
    }

    /**
     * 获取所有相对路径的图片src
     *
     * @param content 富文本内容
     * @return 所有相对路径的图片src
     */
    private static List<String> getRelativeImageSrc(String content) {
        List<String> srcList = new ArrayList<>();
        Matcher m = IMG_SRC_PATTERN.matcher(content);
        while (m.find()) {
            // 匹配到的img标签的src属性
            String src = m.group(1);
            srcList.add(src);
        }
        return srcList;
    }

}
