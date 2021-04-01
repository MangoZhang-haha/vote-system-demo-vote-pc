package flybear.hziee.app.controller;

import flybear.hziee.app.entity.Result;
import flybear.hziee.app.service.VoteLimitService;
import flybear.hziee.app.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mango
 * @Date: 2021/4/1 14:26:17
 */
@RestController
@RequestMapping("/limit")
public class VoteLimitController {

    @Autowired
    private VoteLimitService voteLimitService;

    @GetMapping
    public Result getAll() {
        return ResultUtil.success(voteLimitService.list());
    }
}
