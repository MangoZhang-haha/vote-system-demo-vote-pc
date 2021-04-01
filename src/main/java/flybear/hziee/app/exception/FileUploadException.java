package flybear.hziee.app.exception;

/**
 * 文件上传异常类
 *
 * @author flybear
 * @since 2019/12/22 20:48
 */
public class FileUploadException extends RuntimeException {
    public FileUploadException(String msg) {
        super(msg);
    }
}
