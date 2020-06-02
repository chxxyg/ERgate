package com.kh.ergate.group.model.service;

import java.util.ArrayList;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.ergate.group.model.dao.GroupDao;
import com.kh.ergate.main.model.vo.Employee;

@Service("grService")
public class GroupServiceImpl implements GroupService {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	@Autowired
	private GroupDao grDao;

	
	@Override
	public ArrayList<Employee> selectEmpList() {
		return grDao.selectEmpList(sqlSession);
	}

	@Override
	public Employee selectEmpProfile(String empId) {
		return grDao.selectEmpProfile(sqlSession, empId);
	}

//	@Override
//	public Employee searchGroupList(String condition, String keyword) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public int selectRequestList() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public int selectDetail(String empId) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public int updateEmployee(Employee e) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public int noUpdateEmployee(Employee e) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
}
