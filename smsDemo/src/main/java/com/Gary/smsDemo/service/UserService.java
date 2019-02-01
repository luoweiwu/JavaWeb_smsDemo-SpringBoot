package com.Gary.smsDemo.service;

import com.Gary.smsDemo.domain.User;

public interface UserService {
	//保存用户
	void save(User user);
	//改变用户激活状态
	void changeUserState(String telephone ,String username,String passwprd);
	//校验用户登录
	User findUserByUsernameAndPassword(String username,String password);
	//校验用户激活
	User findUserByUsernameAndPasswordAndTelephone(String username,String password,String telephone);
}
