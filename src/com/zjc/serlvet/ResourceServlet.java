package com.zjc.serlvet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.zjc.dao.ResourceDao;
import com.zjc.dao.impl.ResourceDaoImpl;
import com.zjc.dto.TreeDTO;
import com.zjc.model.Resource;

public class ResourceServlet extends HttpServlet {

	private ResourceDao rdao = new ResourceDaoImpl();
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request,response);
	
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String method = request.getParameter("method");
		if("loadTree".equals(method)){
			
			loadTree(request,response);
		} else if("save".equals(method)){
			save(request,response);
		}else if("update".equals(method)){
			update(request,response);
		}else if("delete".equals(method)){
			delete(request,response);
		}
	
	}


	//加载tree的数据方法
	private void loadTree(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			//获取当前展开节点的id
			String id = request.getParameter("id");
			List<TreeDTO> treelist = this.rdao.getEmp(id);
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(JSONArray.fromObject(treelist).toString());
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	//删除节点
	private void delete(HttpServletRequest request, HttpServletResponse response) {
		try {
			String  id = request.getParameter("id");
			boolean flag = this.rdao.findDepById(Integer.parseInt(id));
			if(flag){
				this.rdao.deleteDep(Integer.parseInt(id));
			}
			this.deletenodes(Integer.parseInt(id));
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
	}
//更新节点
	private void update(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		try {
			String id = request.getParameter("id");
			Resource r = this.rdao.findById(Integer.parseInt(id));
			String name = request.getParameter("name");
			String url = request.getParameter("url");	
			r.setName(name);
			r.setUrl(url);
			this.rdao.update(r);
			
		} catch (Exception e) {
		e.printStackTrace();	
		}
	}
	
//保存节点
	private void save(HttpServletRequest request, HttpServletResponse response) {
		
	
			try{
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=utf-8");
			String parentId = request.getParameter("parentId");
			String name = request.getParameter("name");
			String url = request.getParameter("url");
			Resource r = new Resource();
			r.setName(name);
			r.setUrl(url);
			r.setParent_id(Integer.parseInt(parentId));
			this.rdao.save(r);
			}catch(Exception e){
				try {
					request.getRequestDispatcher( "01.jsp ").forward(request,response);
				}  catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		
		
	}

	//递归删除节点
	private void deletenodes(int id){
		//删除当前节点下的所有子节点
		try {
			
		List<Resource> rlist =	this.rdao.getEmpChildren(id);
			for(int i=0;i<rlist.size();i++){
				int cid = rlist.get(i).getId();
				this.rdao.deleteById(cid);
				deletenodes(cid);
			}
			this.rdao.deleteById(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
