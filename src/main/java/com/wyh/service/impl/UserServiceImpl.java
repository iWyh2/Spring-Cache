package com.wyh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wyh.entity.User;
import com.wyh.mapper.UserMapper;
import com.wyh.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
}
