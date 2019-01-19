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
	<!-- 上面不是有tomcat的目录嘛 /usr/local/tomcat
	直接将工程在Eclipse右键export war包出来放在tomcat webapps下面就行了。
	数据库访问时，直接给ip：port 再加实例名就可以访问了。 -->
	<%
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/lab4_db";	  //为lab4建立的数据库名为lab4_db 
		String db_username = "root";
		String db_password = "xiaokang";
		String username = request.getParameter("username");  //获取客户端传递的数据
		String password = request.getParameter("password");
		Connection conn = null;
		/*
		该后台程序用于注册，查询数据库中是否已存在传递来的用户名，
		若已存在则将错误信息返回-“用户名已存在”；若未存在，则往数据库中
		插入一个用户信息（包括用户名和密码）
		*/
		Class.forName(driver);
		conn = DriverManager.getConnection(url,db_username,db_password);
		JSONObject message = new JSONObject();
		
		String sql = "SELECT * FROM users WHERE username = '" + username + "'";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()){		//该用户名已存在
			message.put("error", "该用户名已存在");
		}
		
		else
		{
			//往数据库插入数据
			sql = "INSERT INTO users (username,password) values(?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,username);
			pstmt.setString(2,password);
			pstmt.executeUpdate();
		}
		
		response.setContentType("text/json;charset=UTF-8"); //???
		PrintWriter outP = response.getWriter();
			
		outP.println(message.toJSONString());
		outP.close();
		rs.close();
		pstmt.close();
		conn.close();  //注意关闭接口
	%>
</body>
</html>