package com.Gary.smsDemo.serviceimpl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Gary.smsDemo.domain.User;
import com.Gary.smsDemo.repository.UserRepository;
import com.Gary.smsDemo.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public void save(User user) {
		// TODO Auto-generated method stub
		userRepository.save(user);
	}

	@Transactional
	@Override
	public void changeUserState(String telephone, String username, String passwprd) {
		// TODO Auto-generated method stub
		userRepository.changeUserState(telephone, username, passwprd);
	}

	@Override
	public User findUserByUsernameAndPassword(String username, String password) {
		return userRepository.findUserByUsernameAndPassword(username, password);
	}

	@Override
	public User findUserByUsernameAndPasswordAndTelephone(String username, String password, String telephone) {
		return userRepository.findUserByUsernameAndPasswordAndTelephone(username, password,telephone);
	}
	
}
