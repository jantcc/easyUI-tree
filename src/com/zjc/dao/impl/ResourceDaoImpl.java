package com.zjc.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.zjc.dao.ResourceDao;
import com.zjc.dto.TreeDTO;
import com.zjc.model.Resource;
import com.zjc.util.JdbcUtil;

public class ResourceDaoImpl implements ResourceDao {

	
	public List<TreeDTO> getEmp(String id) throws Exception {
		// TODO Auto-generated method stub
		Connection conn = JdbcUtil.getConnection();
		String sql = "";
	//	System.out.println("id:"+id);
		if("".equals(id)||id==null){
			sql = "select * from depresource where parent_id = 1";
		}else{
			sql="select * from empresource where parent_id = "+id;
		}
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		List<Resource> rlist = new ArrayList<Resource>();
		while(rs.next()){
			Resource r = new Resource();
			r.setId(rs.getInt("id"));
			r.setIcon(rs.getString("icon"));
			r.setChecked(rs.getInt("checked"));
			r.setName(rs.getString("name"));
			r.setUrl(rs.getString("url"));
			r.setParent_id(rs.getInt("parent_id"));
			rlist.add(r);
		}
		JdbcUtil.closeAll(conn, ps, rs);
		List<TreeDTO> tlist = new ArrayList<TreeDTO>();
          for (Iterator iterator = rlist.iterator();iterator.hasNext();) {
        	  Resource resource =(Resource)iterator.next();
        	  TreeDTO tree = new TreeDTO();
			tree.setId(resource.getId());
			tree.setText(resource.getName());
			tree.setChecked(resource.getChecked());
			tree.setIconCls(resource.getIcon());
			tree.setParent_id(resource.getParent_id());
			if(getEmpChildren(resource.getId()).size()>0){
			tree.setState("closed");	
			}
			else{
				tree.setState("open");
			}
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("url", resource.getUrl());
			tree.setAttributes(map);
			tlist.add(tree);
		}
          
          return tlist;
	}
	
	//根据pid获取孩子
	public List<Resource> getEmpChildren(int pid) throws Exception{
		Connection conn = JdbcUtil.getConnection();
		String sql = "select *from empresource where parent_id =" +pid;
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		List<Resource> children = new ArrayList<Resource>();
		while(rs.next()){
			Resource r = new Resource();
			r.setId(rs.getInt("id"));
			r.setIcon(rs.getString("icon"));
			r.setChecked(rs.getInt("checked"));
			r.setName(rs.getString("name"));
			r.setUrl(rs.getString("url"));
			r.setParent_id(rs.getInt("parent_id"));
			children.add(r);
		}
		JdbcUtil.closeAll(conn, ps, rs);
		return children;
	}
//保存人员到数据库
	public void save(Resource r) throws Exception{
		
		    Connection conn = null;
		    PreparedStatement ps =null;
			conn = JdbcUtil.getConnection();
			String sql = "insert into empresource(name,url,parent_id) values (?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setObject(1, r.getName());
			ps.setObject(2, r.getUrl());
			ps.setObject(3, r.getParent_id());
			ps.executeUpdate();
			JdbcUtil.closeAll(conn, ps, null);
		}
		
	public boolean findDepById(int id) throws Exception {
		boolean flag =false;
		ResultSet rs = null;
		Connection conn = JdbcUtil.getConnection();
		String sql = "select * from depresource where id = "+id;
		PreparedStatement ps = conn.prepareStatement(sql);
		rs = ps.executeQuery();
		if(rs.next()){
			flag = true;
		}
		return flag;
	}
	
	public boolean findEmpByname(String name) throws Exception {
		boolean flag =false;
		ResultSet rs = null;
		Connection conn = JdbcUtil.getConnection();
		String sql = "select * from empresource where name = "+name;
		PreparedStatement ps = conn.prepareStatement(sql);
		rs = ps.executeQuery();
		if(rs.next()){
			flag = true;
		}
		return flag;
	}
	
	public Resource findById(int id) throws Exception{
		Connection conn = JdbcUtil.getConnection();
		String sql = "select * from empresource where id ="+id;
		PreparedStatement ps =conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		Resource resource = new Resource();	
			while(rs.next()){
				resource.setId(id);
				resource.setName(rs.getString("name"));
				resource.setUrl(rs.getString("url"));
				resource.setIcon(rs.getString("icon"));
				resource.setChecked(rs.getInt("checked"));
				resource.setParent_id(rs.getInt("parent_id"));
			}
			JdbcUtil.closeAll(conn, ps, rs);
			return resource;
			
	}

	public void update(Resource r)  {
		
		Connection conn = null;
		PreparedStatement ps =null;
		try {
			conn = JdbcUtil.getConnection();
			String sql = "update empresource set name=?,url=? where id="+r.getId();
			ps = conn.prepareStatement(sql);
			ps.setObject(1, r.getName());
			ps.setObject(2, r.getUrl());
			ps.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			try {
				JdbcUtil.closeAll(conn, ps, null);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}
		
	}

	public void deleteById(int id) throws Exception {
		// TODO Auto-generated method stub
		Connection conn = JdbcUtil.getConnection();
		boolean flag = findDepById(id);
		String sql = "delete from empresource where id = "+id;
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.executeUpdate();
		JdbcUtil.closeAll(conn, ps, null);
	}

	

	public void deleteDep(int id) throws Exception {
		Connection conn = JdbcUtil.getConnection();
		boolean flag = findDepById(id);
		//判断删除的是部门还是人员
		String sql = "delete from depresource where id = "+id;
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.executeUpdate();
		JdbcUtil.closeAll(conn, ps, null);
		
	}
}
