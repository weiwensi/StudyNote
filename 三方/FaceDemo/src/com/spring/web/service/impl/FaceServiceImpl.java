package com.spring.web.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.spring.web.dao.FaceDao;
import com.spring.web.entity.Users;
import com.spring.web.service.FaceService;

@Service
public class FaceServiceImpl implements FaceService {
	@Resource
	private FaceDao facedao;


	public List<Users> selectAllUsers() {
		return facedao.selectAllUsers();
	}


	@Override
	public int save(Users user) {
		 return facedao.save(user);
	}


	@Override
	public Users queryInfoByUsername(String username) {
		// TODO Auto-generated method stub
		 return facedao.queryInfoByUsername(username);
	}

}
