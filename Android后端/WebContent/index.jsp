<%@ page language="java" import="java.sql.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="com.alibaba.fastjson.JSON" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>
<%@ page import="com.alibaba.fastjson.JSONObject" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":"
	+ request.getServerPort() + path + "/";
%>
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
		String db_username = "root";
		String db_password = "xiaokang";
		String username = request.getParameter("username");  //获取客户端URL携带的数据
		String password = request.getParameter("password");
		Connection conn = null;
		/*
		该后台程序用于登录查询，查询数据库中是否有该用户名再验证密码是否正确
		密码正确则啥都不做，错误的话要讲用户名错误还是密码错误的信息与error组成
		键值对传回去
		*/
		
		Class.forName(driver);
		conn = DriverManager.getConnection(url,db_username,db_password);
		JSONObject message = new JSONObject();
		
		String sql = "SELECT * FROM users WHERE username = '" + username + "'";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		int flag = 1;
		if(rs.next())
		{
			//存在,继续验证密码
			String temp_check = rs.getString("password");
			if(!temp_check.equals(password))  //主键是username的话（不允许有重复）rs只存了1行，只判断1次
				flag = 0;
		}
		else
		{
			//不存在，返回错误信息，用户名错误
			message.put("error", "用户名不存在");
			
		}
		if(flag == 0)
		{
			message.put("error","密码错误");
		}
		response.setContentType("text/json;charset=UTF-8"); //数据传输格式...
		PrintWriter outP = response.getWriter();
			
		outP.println(message.toJSONString());
		outP.close();
		rs.close();
		pstmt.close();
		conn.close();
		
		
	%>
	
</body>
</html>