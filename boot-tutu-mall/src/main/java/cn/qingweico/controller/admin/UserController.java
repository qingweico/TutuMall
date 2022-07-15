package cn.qingweico.controller.admin;

import java.util.List;

import cn.qingweico.common.Result;
import cn.qingweico.dto.UserExecution;
import cn.qingweico.entity.User;
import cn.qingweico.enums.UserStateEnum;
import cn.qingweico.service.UserService;
import cn.qingweico.utils.ResponseStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @author zqw
 * @date 2020/11/14
 */
@Slf4j
@RestController
@RequestMapping("/a")
public class UserController {

    @Resource
    UserService userService;

    /**
     * 列出用户信息
     *
     * @return Result
     */
    @GetMapping("/list")
    private Result listUsers(@RequestParam Integer page, @RequestParam Integer pageSize) {
        UserExecution userExecution;
        // 获取分页信息
        if (page > 0 && pageSize > 0) {
            userExecution = userService.getUserList(page, pageSize);
            List<User> list = userExecution.getUserList();
            if (list != null) {
                return Result.ok(list);
            } else {
                return Result.errorMsg(userExecution.getStateInfo());
            }
        }
        log.warn("page: {}, pageSize: {}", page, pageSize);
        return Result.error();
    }

    /**
     * 修改用户信息, 主要是修改用户帐号可用状态
     *
     * @return Map
     */
    @PostMapping("/modify")
    private Result modifyUser(@RequestBody User user) {
        if (user != null && user.getId() != null) {
            UserExecution ue = userService.modifyUser(user);
            if (ue.getState() == UserStateEnum.SUCCESS.getState()) {
                return Result.errorCustom(ResponseStatusEnum.UPDATE_SUCCESS);
            } else {
                return Result.errorMsg(ue.getStateInfo());
            }
        }
        log.error("user: {}", user);
        return Result.error();

    }

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return Result
     */
    @GetMapping("/getUserByName")
    public Result getUserByName(@RequestParam String username) {
        if (StringUtils.isNotBlank(username)) {
            UserExecution ue = userService.getUserByName(username);
            if (ue.getState() == UserStateEnum.SUCCESS.getState()) {
                return Result.ok(ue.getUser());
            } else {
                return Result.errorMsg(ue.getStateInfo());
            }
        }
        log.error("username: {}", username);
        return Result.error();
    }

    /**
     * 根据用户id查询用户信息
     *
     * @param userId 用户id
     * @return User
     */
    @GetMapping("/getUserById")
    public Result getUserById(@RequestParam Long userId) {
        if (userId != null) {
            User user = userService.getUserById(userId);
            return Result.ok(user);
        }
        log.error("userId: {}", userId);
        return Result.error();
    }

}
