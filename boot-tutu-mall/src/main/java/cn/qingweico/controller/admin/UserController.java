package cn.qingweico.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import cn.qingweico.dto.Constant4SuperAdmin;
import cn.qingweico.dto.UserExecution;
import cn.qingweico.entity.User;
import cn.qingweico.enums.UserStateEnum;
import cn.qingweico.exception.UserOperationException;
import cn.qingweico.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author 周庆伟
 * @date 2020/11/14
 */
@RestController
@RequestMapping("/superadmin")
public class UserController {

    UserService userService;

    private final Map<String, Object> map = new HashMap<>(5);

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 列出用户信息
     *
     * @return Map
     */
    @GetMapping("/listusers/{pageIndex}/{pageSize}")
    private Map<String, Object> listUsers(@PathVariable("pageIndex") Integer pageIndex, @PathVariable("pageSize") Integer pageSize) {
        UserExecution userExecution;
        // 获取分页信息
        if (pageIndex > 0 && pageSize > 0) {
            try {
                userExecution = userService.getUserList(pageIndex, pageSize);
            } catch (Exception e) {
                map.put("success", false);
                return map;
            }
            if (userExecution.getUserList() != null) {
                map.put(Constant4SuperAdmin.PAGE_SIZE, userExecution.getUserList());
                map.put(Constant4SuperAdmin.TOTAL, userExecution.getCount());
                map.put("success", true);
            } else {
                map.put(Constant4SuperAdmin.PAGE_SIZE, new ArrayList<User>());
                map.put(Constant4SuperAdmin.TOTAL, 0);
                map.put("success", true);
            }
        } else {
            map.put("success", false);
            map.put("errorMessage", "空的查询信息");
        }
        return map;
    }

    /**
     * 修改用户信息, 主要是修改用户帐号可用状态
     *
     * @return Map
     */
    @PostMapping("/modifyuser")
    private Map<String, Object> modifyUser(@RequestBody Map<String, Object> params) {
        // 从前端请求中获取用户Id以及可用状态
        int userId = (int) params.get("userId");
        boolean enableStatus = (boolean) params.get("enableStatus");
        int enable = enableStatus ? 1 : 0;
        // 非空判断
        if (userId >= 0) {
            try {
                User user = new User();
                user.setUserId(userId);
                user.setEnableStatus(enable);
                // 修改用户可用状态
                UserExecution ue = userService.modifyUser(user);
                if (ue.getState() == UserStateEnum.SUCCESS.getState()) {
                    map.put("success", true);
                } else {
                    map.put("success", false);
                    map.put("errorMessage", ue.getStateInfo());
                }
            } catch (RuntimeException e) {
                map.put("success", false);
                return map;
            }

        } else {
            map.put("success", false);
            map.put("errorMessage", "请输入需要修改的帐号信息");
        }
        return map;
    }

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return model
     */
    @GetMapping("/getuserbyname/{username}")
    public Map<String, Object> getUserByName(@PathVariable("username") String username) {
        if (username == null) {
            map.put("errorMessage", "请输入用户名!");
            return map;
        }
        UserExecution ue = userService.getUserByName(username);
        if (ue.getState() == UserStateEnum.SUCCESS.getState()) {
            map.put("success", true);
            map.put("user", ue.getUser());
        } else {
            map.put("success", false);
            map.put("errorMessage", ue.getStateInfo());
        }
        return map;
    }

    /**
     * 根据用户id查询用户信息
     * @param userId 用户id
     * @return User
     */
    @GetMapping("/getuserbyid/{id}")
    public User getUserById(@PathVariable("id") Integer userId) {
        if (userId == null) {
            throw new RuntimeException("空的userId!");
        }
        if (userId > 0) {
            return userService.getUserById(userId);
        } else {
            throw new UserOperationException("不合法的参数!");
        }
    }

}
