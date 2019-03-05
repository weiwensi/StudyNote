package com.spring.web.dao;

import java.util.List;

import com.spring.web.entity.Users;

public interface FaceDao {

	public List<Users> selectAllUsers();

	public int save(Users users);
	
	 public Users queryInfoByUsername(String username);

}
