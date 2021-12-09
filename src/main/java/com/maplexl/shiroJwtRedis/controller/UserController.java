package com.maplexl.shiroJwtRedis.controller;

import com.maplexl.shiroJwtRedis.constant.CommonEnum;
import com.maplexl.shiroJwtRedis.pojo.User;
import com.maplexl.shiroJwtRedis.pojo.vo.Result;
import com.maplexl.shiroJwtRedis.services.UserService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 枫叶
 * @date 2020/10/31
 */
@RestController
public class UserController {
    @Resource
    UserService userService;

    /**
     * 根据id删除用户
     *
     * @param userId 用户id
     * @return 是否删除成功
     */
    @RequiresRoles("edit")
    @DeleteMapping("/user/{id}")
    public Result<Boolean> delete(@PathVariable("id") Long userId) {
        boolean del = userService.removeById(userId);
        if (del) {
            return new Result<>(CommonEnum.SUCCESS, true);
        }
        return new Result<>(CommonEnum.INVALID_DELETE, false);
    }

    /**
     * 修改用户信息
     *
     * @param user 需修改的信息
     * @return 是否成功
     */
    @RequiresRoles("edit")
    @PutMapping("/user")
    public Result<Boolean> update(@RequestBody User user) {
        boolean up = userService.updateById(user);
        if (up) {
            return new Result<>(CommonEnum.SUCCESS, true);
        }
        return new Result<>(CommonEnum.INVALID_UPDATE, false);
    }

    /**
     * 根据用户id查询用户
     *
     * @param userId 用户id
     * @return 用户信息
     */
    @RequiresRoles("view")
    @GetMapping("/user/{id}")
    public Result<User> getById(@PathVariable("id") Long userId) {
        User user = userService.getById(userId);
        if (user != null) {
            return new Result<>(CommonEnum.SUCCESS, user);
        }
        return new Result<>(CommonEnum.NO_SUCH_USER);
    }

    /**
     * 获取用户列表
     *
     * @return 用户列表
     */
    @RequiresRoles("view")
    @GetMapping("/user/list")
    public Result<List<User>> getList() {
        List<User> list = userService.list();
        return new Result<>(CommonEnum.SUCCESS, list);
    }
}
