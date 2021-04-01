package flybear.hziee.app.controller;

import flybear.hziee.app.entity.Result;
import flybear.hziee.app.util.FileUtils;
import flybear.hziee.app.util.ResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Mango
 * @Date: 2021/3/22 14:45:24
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @PostMapping("/tmpUpload")
    public Result tmpUpload(@RequestParam(value = "file", required = false) MultipartFile multipartFile) throws IOException {
        if (multipartFile == null) {
            return ResultUtil.error("请选择文件上传");
        }
        if (StringUtils.isNotEmpty(multipartFile.getOriginalFilename())) {
            String url = FileUtils.uploadTmp(multipartFile);
            return ResultUtil.success(url);
        } else {
            return ResultUtil.error("文件不能为空");
        }
    }

    @DeleteMapping("/fileDelete")
    public Result fileDelete(@RequestParam("path") String path) {
        FileUtils.deleteFile(path);
        return ResultUtil.success();
    }
}
