package com.spring.web.service;

import java.util.List;

import com.mysql.jdbc.Blob;
import com.spring.web.entity.Users;

public interface FaceService {

	public List<Users> selectAllUsers();
	
    public int save(Users user);
    
    public Users queryInfoByUsername(String username);

}
