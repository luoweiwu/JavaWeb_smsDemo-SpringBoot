package com.Gary.smsDemo.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.Gary.smsDemo.domain.User;

public interface UserRepository extends CrudRepository<User,Long>{
	
	@Query(value="update user u set u.state = 1 where u.telephone = ?1 and u.username=?2 and password = ?3",nativeQuery=true)
	@Modifying
	void changeUserState(String telephone ,String username,String passwprd);
	
	User findUserByUsernameAndPassword(String username,String password);
	User findUserByUsernameAndPasswordAndTelephone(String username,String password,String telephone);
}
