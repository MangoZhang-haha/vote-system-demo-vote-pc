package flybear.hziee.app.util;

import flybear.hziee.app.config.FilePathConfig;
import flybear.hziee.app.exception.FileUploadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 文件上传工具类
 * <p>
 * 注意：后台中 fileUrl 一律不添加 URL_PATTERN 前缀
 * 缓存文件目录比正式文件目录多一个 TEMP_PATH 前缀
 *
 * @author flybear
 * @since 2019/12/22 20:02
 */
@Slf4j
public class FileUploadUtil {
    /**
     * 图片类型后缀
     */
    private static final List<String> IMAGE_EXT = Arrays.asList("jpg", "jpeg", "png", "bmp", "gif");
    /**
     * 视频类型后缀
     */
    private static final List<String> VIDEO_EXT = Arrays.asList("mp4", "avi", "mkv", "flv", "mpg", "mpeg", "wmv");

    private static final String FILE_TYPE_FILE = "file";
    private static final String FILE_TYPE_IMAGE = "image";
    private static final String FILE_TYPE_VIDEO = "video";

    /**
     * 单文件上传临时文件
     *
     * @param file MultipartFile
     * @return 文件在服务器上的临时相对保存位置
     */
    public static String uploadTempFile(MultipartFile file) {
        String saveName = generateSaveName(file);
        String savePath = generateSavePath(saveName);
        save(file, FilePathConfig.REPOSITORY + FilePathConfig.TEMP_PATH + File.separator + savePath, saveName);
        return FilePathConfig.getUrlPath(FilePathConfig.TEMP_PATH + savePath + "/" + saveName);
    }

    /**
     * 移动临时文件到正式文件夹中
     *
     * @param fileUrls 临时文件URL
     * @return 正式文件访问URL数组
     */
    public static String[] moveTempFilesFromFileUrl(String[] fileUrls) {
        for (int i = 0; i < fileUrls.length; i++) {
            fileUrls[i] = moveTempFileFromFileUrl(fileUrls[i]);
        }
        return fileUrls;
    }

    /**
     * 移动临时文件到正式文件夹中
     *
     * @param fileUrl 临时文件URL
     * @return 正式文件访问URL
     */
    public static String moveTempFileFromFileUrl(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl) || !isTempUrl(fileUrl)) {
            return fileUrl;
        }
        String tempFilePath = FilePathConfig.getAbsolutePath(fileUrl);
        String savePath = FilePathConfig.getAbsolutePath(removeTempPath(fileUrl));
        moveFile(tempFilePath, savePath);
        return FilePathConfig.getUrlPath(removeTempPath(fileUrl));
    }

    /**
     * 获取临时文件保存后的地址
     *
     * @param fileUrl 临时文件URL
     * @return 正式文件访问URL
     */
    public static String tempFileUrl2FileUrl(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl) || !isTempUrl(fileUrl)) {
            return fileUrl;
        }
        return FilePathConfig.getUrlPath(removeTempPath(fileUrl));
    }

    /**
     * 将正式文件移回临时文件夹中（用于回滚操作）
     *
     * @param fileUrls 正式文件访问URL集合
     * @return 临时文件URL是猪
     */
    public static String[] moveFileBackToTemp(List<String> fileUrls) {
        String[] tempUrls = new String[fileUrls.size()];
        for (int i = 0; i < fileUrls.size(); i++) {
            tempUrls[i] = moveFileBackToTemp(fileUrls.get(i));
        }
        return tempUrls;
    }

    /**
     * 将正式文件移回临时文件夹中（用于回滚操作）
     *
     * @param fileUrl 正式文件访问URL
     * @return 临时文件URL
     */
    public static String moveFileBackToTemp(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl) || isTempUrl(fileUrl)) {
            return fileUrl;
        }
        String filePath = FilePathConfig.getAbsolutePath(fileUrl);
        String saveTempPath = FilePathConfig.getAbsolutePath(FilePathConfig.TEMP_PATH + fileUrl);
        moveFile(filePath, saveTempPath);
        return FilePathConfig.getUrlPath(FilePathConfig.TEMP_PATH + fileUrl);
    }

    /**
     * 保存文件
     *
     * @param file     MultipartFile
     * @param savePath 保存路径
     * @param saveName 文件名
     */
    private static void save(MultipartFile file, String savePath, String saveName) {
        File dir = new File(savePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(savePath, saveName));
        } catch (IOException e) {
            log.error("文件保存失败{}", e);
            throw new FileUploadException("文件保存失败");
        }
    }

    /**
     * 获取文件保存相对路径
     */
    private static String generateSavePath(String saveName) {
        String timestampStr = saveName.substring(0, 13);
        // 解析日期目录
        String ymd = new SimpleDateFormat("yyyyMMdd").format(s2n(timestampStr));
        // 获取文件种类
        String fileKind = parseFileKind(saveName);
        return fileKind + File.separator + ymd;
    }

    /**
     * 生成新的文件名
     */
    private static String generateSaveName(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (StringUtils.isEmpty(fileName)) {
            throw new FileUploadException("文件名获取失败");
        }

        String fileKind = parseFileKind(fileName);

        String fileNameSuffix = "";
        // 生成图片尺寸后缀(图片宽度x图片高度.扩展名)
        if (FILE_TYPE_IMAGE.equals(fileKind)) {
            try {
                BufferedImage bimg = ImageIO.read(file.getInputStream());
                int width = bimg.getWidth();
                int height = bimg.getHeight();
                fileNameSuffix = width + "x" + height + getExtName(fileName);
            } catch (IOException e) {
                // 获取图片信息失败，则按照原来的方式命名
                fileNameSuffix = fileName + getExtName(fileName);
                log.error("获取图片信息失败：{}", e);
                e.printStackTrace();
            }
        } else {
            fileNameSuffix = getExtName(fileName);
        }

        return n2s(System.currentTimeMillis()) + "_!!" + fileNameSuffix;

    }

    /**
     * 是否为缓存文件URL
     *
     * @param fileUrl 文件URL
     * @return 是否为缓存文件URL
     */
    public static boolean isTempUrl(String fileUrl) {
        return fileUrl.startsWith(FilePathConfig.getUrlPath(FilePathConfig.TEMP_PATH));
    }

    /**
     * 获取文件类型
     *
     * @param saveName 文件名
     * @return 文件类型 （file，video，image）
     */
    private static String parseFileKind(String saveName) {
        String fileExt = getExtName(saveName).substring(1);
        if (VIDEO_EXT.contains(fileExt)) {
            return FILE_TYPE_VIDEO;
        }
        if (IMAGE_EXT.contains(fileExt)) {
            return FILE_TYPE_IMAGE;
        }
        // 默认是file种类
        return FILE_TYPE_FILE;
    }

    /**
     * 根据文件名解析出文件URL
     *
     * @param fileName 文件名
     * @return 文件URL
     */
    public static String parseFileUrl(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return null;
        }
        try {
            String timestampStr = fileName.substring(0, 13);
            long timestamp = s2n(timestampStr);
            // 解析日期目录
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String ymd = sdf.format(new Date(timestamp));
            // 获取文件种类
            String fileKind = parseFileKind(fileName);

            return FilePathConfig.URL_PATTERN + fileKind + "/" + ymd + "/" + fileName;
        } catch (Exception ignore) {
            return "error_file";
        }
    }

    /**
     * 根据文件名返回文件扩展名,小写,包含小数点
     */
    private static String getExtName(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
    }

    /**
     * 把13位时间戳数字转成字符串(混合大小写)
     */
    private static String n2s(long num) {
        StringBuilder str = new StringBuilder();
        char ch;
        String numStr = String.valueOf(num);
        for (int i = 0; i < numStr.length(); i++) {
            int n = Integer.parseInt(numStr.substring(i, i + 1));
            if (n <= 2) {
                // 这里最后一组只保留xyz不转换，用来做分隔符
                ch = (char) (n + 65 + rand(0, 3) * 10 + rand(0, 2) * 32);
            } else {
                ch = (char) (n + 65 + rand(0, 2) * 10 + rand(0, 2) * 32);
            }
            str.append(ch);
        }
        return str.toString();
    }

    /**
     * 把字符串转成13位时间戳数字(混合大小写)
     */
    private static long s2n(String str) {
        StringBuilder numStr = new StringBuilder();
        str = str.toUpperCase();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            numStr.append((((int) ch) - 65) % 10);
        }
        return Long.parseLong(numStr.toString());
    }

    /**
     * 产生随机整数,范围[m,n)
     */
    private static int rand(int m, int n) {
        return (int) (Math.random() * (n - m) + m);
    }

    /**
     * 移除文件URL中的临时路径字符串
     *
     * @param url 文件URL
     * @return 临时路径字符串
     */
    public static String removeTempPath(String url) {
        return url.replaceFirst(FilePathConfig.getUrlPath(FilePathConfig.TEMP_PATH), "");
    }

    /**
     * 移动文件
     *
     * @param fromPath 源文件路径
     * @param toPath   目标路径
     */
    public static void moveFile(String fromPath, String toPath) {
        File fromFile = new File(fromPath);
        if (fromFile.exists()) {
            File toDir = new File(toPath.substring(0, toPath.lastIndexOf(File.separator)));
            if (!toDir.exists()) {
                toDir.mkdirs();
            }
            fromFile.renameTo(new File(toPath));
        }
    }

    /**
     * 删除文件
     *
     * @param fileUrl 文件访问地址
     */
    public static void deleteFile(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            return;
        }
        File file = new File(FilePathConfig.getFilePath(FilePathConfig.REPOSITORY + fileUrl));
        if (file.exists()) {
            file.delete();
        }
    }
}
