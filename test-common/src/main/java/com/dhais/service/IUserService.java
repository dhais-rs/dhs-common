package com.dhais.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dhais.model.User;

/**
 * All rights Reserved, Designed By Fan Jun
 *
 * @author Fan Jun
 * @since 2021/3/11 15:04
 */
public interface IUserService extends IService<User> {

    void testDefaultDataSource();
}
