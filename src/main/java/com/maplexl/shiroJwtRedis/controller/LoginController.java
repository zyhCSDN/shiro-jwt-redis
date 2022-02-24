package com.maplexl.shiroJwtRedis.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.maplexl.shiroJwtRedis.constant.CommonConstant;
import com.maplexl.shiroJwtRedis.enums.StatusCode;
import com.maplexl.shiroJwtRedis.exception.GlobalException;
import com.maplexl.shiroJwtRedis.pojo.User;
import com.maplexl.shiroJwtRedis.response.BaseResponse;
import com.maplexl.shiroJwtRedis.services.UserService;
import com.maplexl.shiroJwtRedis.util.JwtUtil;
import com.maplexl.shiroJwtRedis.util.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author 枫叶
 * @date 2020/11/1
 */
@RestController
@Slf4j
@Api(tags = "登录注册模块")
public class LoginController {
    @Resource
    UserService userService;

    /**
     * 登录接口
     * @param user 用户
     * @param response 响应
     * @return 登录成功则返回token
     */
    @ApiOperation(value = "登录接口",notes = "获取token", httpMethod = "POST")
    @PostMapping("/login")
    public BaseResponse login(@RequestBody User user, HttpServletResponse response) {

        JSONObject jsonObject=new JSONObject();
        String userName = user.getUserName();
        String password = user.getPassword();
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            return new BaseResponse(StatusCode.INVALID_PARAMS);
        }
        //查询是否有此用户
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_name", userName);
        User dbUser=  userService.getOne(queryWrapper);
        if (dbUser == null) {
            return new BaseResponse(StatusCode.NO_SUCH_USER);
        }
        //校验密码
        password = new SimpleHash("MD5", password, dbUser.getSalt(), 32).toString();
        if (password.equals(dbUser.getPassword())) {
            //密码正确 生成token
            String token = JwtUtil.sign((long)dbUser.getUserId());
            RedisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token,JwtUtil.REFRESH_EXPIRE_TIME );
            //保存用户信息到redis，不用设置过期时间 token为key
            RedisUtil.set(CommonConstant.PREFIX_USER + dbUser.getUserId(),dbUser);
            dbUser.setPassword(null);
            response.setHeader("Authorization", token);
            response.setHeader("Access-Control-Expose-Headers", "Authorization");
            jsonObject.put("user", dbUser);
            jsonObject.put("token", token);
            return new BaseResponse (StatusCode.SUCCESS,jsonObject);
        }
        return new BaseResponse(StatusCode.PASSWORD_ERROR,jsonObject);
    }

    /**
     * 添加用户,即注册
     *
     * @param user 用户信息
     * @return 是否保存成功
     */
    @ApiOperation(value = "注册接口",notes = "注册用户", httpMethod = "POST")
    @PostMapping("/register")
    public BaseResponse add(@RequestBody User user) {
        //验证用户名是否重复
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name",user.getUserName());
        User dbUser = userService.getOne(wrapper);
        if (dbUser!=null){
            return new BaseResponse(StatusCode.USER_EXIST,dbUser.getUserName());
        }
        //加密用户的密码
        String salt = UUID.randomUUID().toString().replace("-","").substring(0, 24);
        String password = new SimpleHash("MD5", user.getPassword(), salt, 32).toString();
        user.setSalt(salt);
        user.setPassword(password);
        boolean save = userService.save(user);
        if (save) {
            return new BaseResponse(StatusCode.REGISTER_SUCCESS,user);
        }
        return new BaseResponse(StatusCode.FAIL);
    }

    /**
     * 登出
     * @return 是否登出
     */
    @ApiOperation(value = "退出登录",notes = "注销token", httpMethod = "GET")
    @GetMapping("/logout")
    public BaseResponse logout(HttpServletRequest request){
        /*
         * 清除redis中的RefreshToken即可
         */
//        long userId = JwtUtil.getUserId(request);

        String token = request.getHeader("token");
        RedisUtil.del(CommonConstant.PREFIX_USER_TOKEN+token);
        return new BaseResponse(StatusCode.SUCCESS);
    }


    /**
     *
     * 不加注解的话默认不验证，登录接口一般是不验证的。所以我在getMessage()中加上了登录注解，说明该接口必须登录获取token后，在请求头中加上token并通过验证才可以访问。
     * 该接口需要带签名才能访问
     * @return
     */
    @ApiOperation(value = "无token验证",httpMethod = "GET")
    @GetMapping("/getMessage")
    public String getMessage(){
        return "你已通过验证";
    }

    @ApiOperation(value = "有token验证",httpMethod = "GET")
    @GetMapping("/getMessage1")
    public String getMessage1(HttpServletRequest request){
        Long userId = JwtUtil.getUserId(request);
        return userId.toString();
    }


    @ApiOperation(value = "有token验证",httpMethod = "GET")
    @GetMapping("/getMessage2")
    public void getMessage2(HttpServletRequest request){
        Long userId = JwtUtil.getUserId(request);
        //在service实现类里，可抛出异常
        throw  new GlobalException(StatusCode.REGISTER_SUCCESS);
    }


    @ApiOperation(value = "有token验证",httpMethod = "GET")
    @GetMapping("/getUser")
    public BaseResponse getUser(HttpServletRequest request){
        Long userId = JwtUtil.getUserId(request);
        Object o = RedisUtil.get(CommonConstant.PREFIX_USER + userId);
        return new BaseResponse(StatusCode.SUCCESS,o);
    }

    @ApiOperation(value = "有token验证",httpMethod = "GET")
    @GetMapping("/getUserInfo")
    public BaseResponse getUserInfo(HttpServletRequest request){
        String accessToken = request.getHeader("token");
        User userInfo = JwtUtil.getUserInfo(accessToken);
        if (userInfo==null){
            new User();
        }
         return new BaseResponse(StatusCode.SUCCESS,userInfo);
    }
}
