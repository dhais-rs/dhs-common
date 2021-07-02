package com.dhais.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dhais.annotation.DataSourceTarget;
import com.dhais.mapper.IUserMapper;
import com.dhais.model.User;
import com.dhais.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * All rights Reserved, Designed By Fan Jun
 *
 * @author Fan Jun
 * @since 2021/3/11 15:05
 */
@Service
public class IUserServiceIml extends ServiceImpl<IUserMapper, User> implements IUserService {

    @DataSourceTarget("dhais")
    @Override
    public void testDefaultDataSource(){
        List<User> list = list(null);
        for (User user : list) {
            System.out.println(user);
        }
    }
}
