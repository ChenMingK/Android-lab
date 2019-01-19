package com.example.lab4;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class register extends Activity {
    private EditText uText; 
    private EditText pText; 
    private EditText psText;
    private TextView textview;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		uText  = (EditText)findViewById(R.id.register_username);
		pText  = (EditText)findViewById(R.id.register_password);
		psText = (EditText) findViewById(R.id.register_sure_password);
        textview  = (TextView)findViewById(R.id.register_tv);
        
        
	}
   /*
    * check点击事件绑定到确认，cancel绑定到取消
    * check会进行密码和确认密码的检验首先验证输入的信息是否符合要求（用户名至少5位，最多10位，
    * 以英文字母开头，只允许包含英文字母、数字以及_，
    * 同时必须至少有一个大写英文字母；密码为6-12位，只允许包含英文字母、数字和_，同时要求确认密码必须与密码一致），
    * 如不符合要求则在界面内提示错误，只有符合要求才提交给后台进行注册操作。
    * 如注册成功则跳转至欢迎界面，否则跳转回注册界面并提示用户的注册错误原因；
    */
	public void cancel(View view){
		   Intent intent = new Intent();
		   intent.setClass(register.this, login.class);
		   startActivity(intent);
	}
	public void check(View view){
		   String message    = textview.getText().toString();
		   String username  = uText.getText().toString();
		   String password   = pText.getText().toString();
		   String sure_password = psText.getText().toString();
		   
		   if(message.length()>0) textview.setVisibility(View.INVISIBLE);  //what?
		   
		   //check username
		   /*
		    * 1.用户名不能为空
		    * 2.用户名必须为5-10位
		    * 3.用户名必须以英文字母开头，只允许包含英文字母、数字以及_
		    * 4.用户名必须至少有一个大写英文字母
		    * 5.密码&确认密码不能为空
		    * 6.密码&确认密码必须为6-12位
		    * 7.密码&确认密码只允许包含英文字母、数字和_
		    * 8.密码与确认密码必须与密码一致
		    */
		   String mess = "";
		   if(username.isEmpty()){
			    textview.setText("用户名不能为空");
			    textview.setVisibility(View.VISIBLE);
			    return ;
		   }
	       if(username.length() < 5 || username.length() > 10)
	            mess += " 用户名必须为5-10位 ";
	        String pattern1 = "^[a-zA-Z][a-zA-Z0-9_]*$";    //^h表示必须以h开头,[]匹配方括号中任意字符,$该符号不匹配任何字符,字符串结束的位置,即r$必须以r结尾
	        String pattern2 = ".*[A-Z].*";					//至少一个大写英文字母
	        boolean match1 = Pattern.matches(pattern1, username);
	        boolean match2 = Pattern.matches(pattern2, username);
	        if(!match1) {
	            mess += "用户名必须以英文字母开头,只允许包含英文字母、数字以及_ ";
	        }
	        if(!match2) {
	            mess += "用户名至少有一个大写英文字母 ";
	        }
	        
	        if(!mess.isEmpty()) {
	        	textview.setText(mess);
			    textview.setVisibility(View.VISIBLE);
			    return ;
	        }
	         
		   //check密码
	        if(password.isEmpty()){
			    textview.setText("密码不能为空");
			    textview.setVisibility(View.VISIBLE);
			    return ;
		   }
		    mess = "";
	        if(password.length() < 6 || password.length() > 12 ||sure_password.length()>12||password.length()<6)
	            mess += "密码必须为6-12位  ";
	        if(!password.equals(sure_password)){
	        	mess +="两次输入的密码不一致 ";
	        }
	        String pattern3 = "[a-zA-Z0-9_]*";
	        boolean match3 = Pattern.matches(pattern3, password);
	        if(!match3) {
	            mess += "密码只允许包含英文字母、数字和_ ";
	        }
	        if(!mess.isEmpty()) {
	        	textview.setText(mess);
			    textview.setVisibility(View.VISIBLE);
			    return ;
	        }
	       /*
	        * 输入正确性验证完成后使用AlertDialog请求用户确认
	        */
		   else {
			   textview.setVisibility(View.INVISIBLE);
		       showNormalDialog();
		   }
	}
	public void showNormalDialog(){
		 final AlertDialog.Builder normalDialog =  new AlertDialog.Builder(this);
		 normalDialog.setIcon(R.drawable.ic_launcher);
		 normalDialog.setTitle("温馨提示");
	     normalDialog.setMessage("确认注册？");
	     normalDialog.setPositiveButton("确定", 
	     new DialogInterface.OnClickListener() {
	    	 @Override
	    	 public void onClick(DialogInterface dialog, int which) {
	                //...To-do
	    		    new SignUpProcess().execute(uText.getText().toString(), psText.getText().toString());  //完成全部检查使用用户名和密码进行注册
	            }
	     });
	     normalDialog.setNegativeButton("取消", 
	     new DialogInterface.OnClickListener() {
	    	 @Override
	         public void onClick(DialogInterface dialog, int which) {
	                //...To-do
	            }
	        });
	      // 显示
	      normalDialog.show();
		        
	}
	/*
	 * 如注册成功则跳转至欢迎界面，否则跳转回注册界面并提示用户的注册错误原因；
	 * 错误原因如用户名已存在
	 */
	private class SignUpProcess extends AsyncTask<String, String, String> {
	       @Override
	       protected String doInBackground(String... params) {  
	    	  
	           String username = params[0];
	           String password = params[1];
	           String result = "";
	           String s_url = "http://10.63.22.33:8080/testAndroidJSP/signup.jsp";  
	           s_url = s_url + "?username=" + username + "&password=" + password;    
	           /*HttpURLConnection
	            * step1:URL url = new URL(https://www.baidu.com);  						      创建URL对象
	            * step2:HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 获取对象实例
	            * step3:conn.setRequestMethod("GET");  									      设置请求方式
	            * step4:conn.setConnectTimeout(6*1000); conn.setReadTimeout(6 * 1000);     设置连接超时，读取超时的毫秒数，以及服务器希望得到的一些消息头
	            * step5:InputStream in = conn.getInputStream(); 调用getInputStream()方法获得服务器返回的输入流，然后输入流进行读取
	            * step6:conn.disconnect();		关闭HTTP连接
	            * PS:step4可对状态码进行判断，为200则继续
	            */
	           try {
	               URL url = new URL(s_url);
	               HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
	               conn.setRequestMethod("GET");
	               conn.connect();
	               InputStream is = conn.getInputStream();
	               InputStreamReader reader = new InputStreamReader(is, "UTF-8");
	               int temp;
	               while((temp=reader.read()) != -1) { //reader类方法？
	                   result += (char)temp;
	               }
	           } catch(Exception e) {
	               e.printStackTrace();
	           }
	           
	           return result;	//服务器以JSON格式传回来，又转换为字符串
	       }

		@Override
	       protected void onPostExecute(String result) {   //doInBackground的返回值将传入此方法,更新UI
	           try {
	               JSONObject result_json = new JSONObject(result);
	               if(result_json.has("error")) {	
	                   String error_code;
	                   error_code = result_json.getString("error");  //获得值
	                   textview.setText(error_code);
	                   textview.setVisibility(View.VISIBLE);
	               } else {
	                   SignUpSuccess(result_json);
	               }
	           } catch (Exception e) {
	               e.printStackTrace();
	           }
	       }

	   }
	   //成功接收到数据后的操作，数据以JSON形式传递
	   private void SignUpSuccess(JSONObject info) {
	       Intent intent = new Intent(this, Welcome.class);
	       Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT).show();
	       try {
	           intent.putExtra("username", uText.getText().toString());
	       } catch (Exception e) {
	           e.printStackTrace();
	       }
	       startActivity(intent);   //跳转到Welcome的页面  数据存储在intent中
	   } 
}
