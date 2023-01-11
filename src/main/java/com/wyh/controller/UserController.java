package com.wyh.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wyh.entity.User;
import com.wyh.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final CacheManager cacheManager;

    private final UserService userService;

    @Autowired
    public UserController(CacheManager cacheManager, UserService userService) {
        this.cacheManager = cacheManager;
        this.userService = userService;
    }

    @CachePut(value = "userCache", key = "#user.id")//SpEL表达式 动态的生成缓存key - 将方法中的user的id作为key
    @PostMapping
    public User save(User user){
        userService.save(user);
        return user;
    }

    @CacheEvict(value = "userCache",key = "#id")//SpEL表达式 动态的生成缓存key
    //@CacheEvict(value = "userCache",key = "#p0")
    //@CacheEvict(value = "userCache",key = "#root.args[0]")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        userService.removeById(id);
    }

    @CacheEvict(value = "userCache",key = "#user.id")
    //@CacheEvict(value = "userCache",key = "#p0.id")
    //@CacheEvict(value = "userCache",key = "#root.args[0].id")
    //@CacheEvict(value = "userCache",key = "#result.id")
    @PutMapping
    public User update(User user){
        userService.updateById(user);
        return user;
    }

    @Cacheable(value = "userCache",key = "#id",unless = "#result == null")
    @GetMapping("/{id}")
    public User getById(@PathVariable Long id){
        return userService.getById(id);
    }

    @Cacheable(value = "userCache",key = "#user.id + '_' + #user.name",unless = "#result != null")
    @GetMapping("/list")
    public List<User> list(User user){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(user.getId() != null,User::getId,user.getId());
        queryWrapper.eq(user.getName() != null,User::getName,user.getName());
        return userService.list(queryWrapper);
    }
}
