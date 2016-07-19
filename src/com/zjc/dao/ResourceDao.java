package com.zjc.dao;

import java.util.List;

import com.zjc.dto.TreeDTO;
import com.zjc.model.Resource;

public interface ResourceDao {

	List<TreeDTO> getEmp(String id) throws Exception;

	void save(Resource r) throws Exception;

	Resource findById(int id) throws Exception;

	void update(Resource r) ;
	
	public List<Resource> getEmpChildren(int pid) throws Exception;
	
	void deleteById(int id) throws Exception;
	
	void deleteDep(int id) throws Exception;
	public boolean findDepById(int id) throws Exception;
	
	boolean findEmpByname(String name) throws Exception;
}
