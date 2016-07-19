<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>



<html>
  <head>
   
    
  
<!-- 引入JQuery -->
<script type="text/javascript" src="js/jquery-easyui-1.2.6/jquery-1.7.2.min.js"></script>

   <!-- 引入EasyUI -->
   <script type="text/javascript" src="js/jquery-easyui-1.2.6/jquery.easyui.min.js"></script>
   <!-- 引入EasyUI的中文国际化js，让EasyUI支持中文 -->
   <script type="text/javascript" src="js/jquery-easyui-1.2.6/locale/easyui-lang-zh_CN.js"></script>
   <!-- 引入EasyUI的样式文件-->
   <link rel="stylesheet" href="js/jquery-easyui-1.2.6/themes/default/easyui.css" type="text/css"/>
   <!-- 引入EasyUI的图标样式文件-->
   <link rel="stylesheet" href="js/jquery-easyui-1.2.6/themes/icon.css" type="text/css"/>
   
   	<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.2.6/demo/demo.css">
 <script>
 	var flag ;
 	$(function(){
 		
 	 $('#t1').tree({
 			url:'ResourceServlet?method=loadTree',	
 			        
 			        onContextMenu: function(e,node){
					e.preventDefault();
					$(this).tree('select', node.target);
					$('#mm').menu('show', {
						left: e.pageX,
						top: e.pageY
					});
				}
		});
	
//取消
 	$('#cancelbtn').click(function(){
 	$('#mydiv').dialog('close');
 	});
 	
 	
//保存
 	 	$('#savebtn').click(function(){	
 	 	//判断是添加还是修改
 	 	if(flag == 'add'){
 	 		//获取选中的父级节点
 		//前台更新
 		var node = $('#t1').tree('getSelected');
 		$('#t1').tree('append',{
 			parent:node.target,
 			data:[{
 				text:$('#myform').find('input[name=name]').val(),
 				attributes:{
 				url:$('#myform').find('input[name=url]').val()
 				}
 			}]
 		
 		});
 		
 		//后台同步更新
 		$.ajax({
 			type:'post',
 			url:'ResourceServlet?method=save',
 			cache:false,
 			data:{
 				parentId:node.id,
 				name:$('#myform').find('input[name=name]').val(),
 				url:$('#myform').find('input[name=url]').val()
 			},
 			dataType:'json',
 			success:function(result){
 				//添加完成后再次刷新节点
 				var parent = $('#t1').tree('getParent',node.target);
 				$('#t1').tree('reload',parent.target);
 				$.messager.show({
 				title:'提示信息',
 				msg:'操作成功！'
 				});
 			},
 			error:function(){
 				var name123= $('#myform').find('input[name=name]').val();
 				alert(""+name123+"已存在于其他部门或本部门,添加失败");
 				location.reload();
 			}
 		
 		});
 		
 			//添加完毕后关闭mydialog
 			 $('#mydiv').dialog('close');
 	 	}
 	 	
  	
 	 //修改操作 editor
 	 	else{	
 	 				
 	 	
 	 			//后台同步更新
 		$.ajax({
 			type:'post',
 			url:'ResourceServlet?method=update',
 			cache:false,
 			data:{
 				id:$('#myform').find('input[name=id]').val(),
 				name:$('#myform').find('input[name=name]').val(),
 				url:$('#myform').find('input[name=url]').val()
 			},
 			dataType:'json',
 			success:function(result){
 			//刷新的节点要是选中的父级节点
 	     var node = $('#t1').tree('getSelected');
 	     var parent = $('#t1').tree('getParent',node.target);
 	      $('#t1').tree('reload',parent.target);
 				$.messager.show({
 				title:'提示信息',
 				msg:'操作成功！'
 				});
 			}
 		
 		});
 		
 	 	//更新完毕后关闭mydialog
 			 $('#mydiv').dialog('close');
 	 	}
 	
 	});
 	
 	
 	
 	});
		
		function append(){
			flag = 'add';
			$('#mydiv').dialog('open');
		}
		
		function editor(){
			flag = 'edit';
		//清空表单
			$('#myform').form('clear');
		//获取选中节点
			var node = $('#t1').tree('getSelected');
		//填充表单
			$('#myform').form('load',{
				id:node.id,
				name:node.text,
				url: node.attributes.url
			});
			//打开dilog
			$('#mydiv').dialog('open');
		}
		
		function remove1(){
		//前台删除
			var node = $('#t1').tree('getSelected');
			$('#t1').tree('remove',node.target);
		//后台删除
		$.post('ResourceServlet?method=delete',{id:node.id},function(){
		$.messager.show({
 				title:'提示信息',
 				msg:'操作成功！'
 				});
		});
		}

	</script>



  </head>
  
  <body>
     <ul id ="t1"></ul>
     <div  title="添加" id="mydiv" class="easyui-dialog" style="width:250px;" closed="true">
     	<form method="post" id="myform">
     	<input type="hidden" name="id" value="" />
     		<table>
     		<tr>
     		    <td>名称</td>
     			<td><input type="text" name="name" value="">  </td>
     		</tr>
     		
     		<tr>
     		    <td>url:</td>
     			<td> <input type="text" name="url" value=""> </td>
     		</tr>
     		
     		<tr align="center">
     		    <td colspan="2" >
     		            <a id="savebtn" class="easyui-linkbutton">保存</a>
     		    		<a id="cancelbtn" class="easyui-linkbutton">取消</a>
     		    </td>		
     		</tr>
     		</table>
     	</form>

     </div>
   <div id="mm" class="easyui-menu" style="width:150px;">
		<div onclick="append()">append</div>
		<div onclick="editor()">editor</div>
		<div onclick="remove1()">remove</div>
	</div>
   </body>
   
</html>
     