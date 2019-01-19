<%@ page language="java" import="java.sql.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="com.alibaba.fastjson.JSON" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
		<% 
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/lab4_db";	  //为lab4建立的数据库名为lab4_db 
		String db_username = "root";		//用户名
		String db_password = "xiaokang";	//密码
		String username = request.getParameter("username");  //客户端是以什么方式将这两个传入？
		String old_password = request.getParameter("old_password");//旧密码
		String new_password = request.getParameter("new_password");//新密码
		Connection conn = null;
		
		Class.forName(driver);
		conn = DriverManager.getConnection(url,db_username,db_password);
		JSONObject message = new JSONObject();
		
		String sql = "SELECT * FROM users WHERE username = '" + username + "'";
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		if(!rs.next()){
			//用户名不存在
			message.put("error","用户名不存在");
		}
		else{
			if(!old_password.equals(rs.getString("password"))){
				//验证密码错误，返回错误信息
				message.put("error","原始密码不正确");
			}
			else{
				//修改成功
				sql="UPDATE users set password='"+new_password+"' where username='"+username+"'";
				stmt.executeUpdate(sql);
				message.put("success","密码修改成功");
			}
		}
		response.setContentType("text/json;charset=UTF-8");
		PrintWriter outP=response.getWriter();//得到向客户端发送数据的字符输出流
			
		outP.println(message.toString());//该数据在响应消息体中
		outP.close();
		rs.close();
		stmt.close();
		conn.close();
		%>
</body>
</html>