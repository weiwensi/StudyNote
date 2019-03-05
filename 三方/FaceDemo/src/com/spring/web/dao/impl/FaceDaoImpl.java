package com.spring.web.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;

import com.spring.web.dao.FaceDao;
import com.spring.web.entity.Users;
import com.spring.web.service.FaceService;

@Repository
public class FaceDaoImpl implements FaceDao {

	@Resource
	private SqlSessionFactory sqlSessionFactory;

	public List<Users> selectAllUsers() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		List<Users> user = null;
		user = sqlSession.selectList(Users.class.getName() + ".selectAllUsers");
		return user;

	}
	

	@Override
	public int save(Users users) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		return sqlSession.insert(Users.class.getName() + ".save",users);
	}


	@Override
	public Users queryInfoByUsername(String username) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		Users users = sqlSession.selectOne(Users.class.getName() + ".queryInfoByUsername", username);
		return users;
	}



	@Test
	public void testSelectAll() {
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		FaceService dao = (FaceService) ac.getBean("faceServiceImpl");

		dao.selectAllUsers();
	}


	@Test
	public void testSelectAlls() {
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		FaceService dao = (FaceService) ac.getBean("faceServiceImpl");
		List<Users> users = dao.selectAllUsers();
	}


}
