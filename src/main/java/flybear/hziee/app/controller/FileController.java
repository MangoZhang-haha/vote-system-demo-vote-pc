package flybear.hziee.app.controller;

import flybear.hziee.app.entity.Result;
import flybear.hziee.app.util.FileUploadUtil;
import flybear.hziee.app.util.ResultUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件控制器
 *
 * @author flybear
 * @since 2019/12/22 21:10
 */
@RestController
public class FileController {

    /**
     * 上传文件接口，保存的是临时文件
     *
     * @param file MultipartFile
     * @return 临时文件访问地址
     */
    @PostMapping("upload")
    @RequiresAuthentication
    public Result<String> upload(MultipartFile file) {
        return ResultUtil.success(FileUploadUtil.uploadTempFile(file));
    }

    /**
     * 上传文件接口，保存的是临时文件
     *
     * @param files MultipartFile[]
     * @return 临时文件访问地址
     */
    @PostMapping("uploads")
    @RequiresAuthentication
    public Result<String[]> upload(MultipartFile[] files) {
        String[] savePaths = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            savePaths[i] = FileUploadUtil.uploadTempFile(file);
        }
        return ResultUtil.success(savePaths);
    }

    /**
     * 删除临时文件
     *
     * @param fileUrl 临时文件访问地址
     * @return 是否成功
     */
    @DeleteMapping("file")
    @RequiresAuthentication
    public Result removeTempFile(String fileUrl) {
        if (FileUploadUtil.isTempUrl(fileUrl)) {
            FileUploadUtil.deleteFile(fileUrl);
        }
        return ResultUtil.success();
    }

}
