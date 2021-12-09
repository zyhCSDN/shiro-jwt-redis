package com.maplexl.shiroJwtRedis.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maplexl.shiroJwtRedis.mapper.UserMapper;
import com.maplexl.shiroJwtRedis.pojo.User;
import com.maplexl.shiroJwtRedis.services.UserService;
import org.springframework.stereotype.Service;

/**
 * @author 枫叶
 * @date 2020/11/1
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


}
