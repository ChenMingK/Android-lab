package com.example.lab4;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.*;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.regex.Pattern;


public class login extends Activity {
	//private static final int VISIABLE = 0;
	//变量声明
    private EditText username;
    private EditText password;
    private TextView err;					
    private Button button1;
    private Button button2;
    private CheckBox cb;
    private Button changebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        err    = (TextView)findViewById(R.id.err);
        button1  = (Button)findViewById(R.id.log_button_1);
        button2  = (Button)findViewById(R.id.log_button_2);
        cb = (CheckBox) findViewById(R.id.checkBox1);
        changebutton = (Button) findViewById(R.id.changebutton);
        
        //CheckBox勾选按钮用于显示密码
        
        cb.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
		    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
				if(cb.isChecked()){
		            // show the password
		            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
		        }
		        else{
		            // hide the password 
		            password.setTransformationMethod(PasswordTransformationMethod.getInstance());   
		        }         
		    }
	    }); 
    }
    

    //登录按钮绑定的监听事件:检测用户名和密码是否为空，符合条件则调用后台服务
    public void login(View view){    
	      String user = username.getText().toString();
	      String pass = password.getText().toString();
	      if(user.isEmpty()||pass.isEmpty()){
	    	 err.setText("用户名和密码不能为空");
	    	 err.setVisibility(View.VISIBLE);
	      }
	      
	      else{
	    	  String pattern1 = "^[a-zA-Z][a-zA-Z0-9_]*$";    //用户名必须以英文字母开头,只允许包含英文字母、数字以及_
	    	  boolean match1 = Pattern.matches(pattern1, user);
	    	  if(!match1){
	    		  err.setText("该用户名不存在");
	    		  err.setVisibility(View.VISIBLE);
	    		  return;
	    	  }
	    	  err.setText(null);
	    	  err.setVisibility(View.INVISIBLE);
	    	  //Toast参数：1:上下文对象 2:显示的内容  3:显示的时间，只有LONG和SHORT两种会生效
	    	  Toast.makeText(this,"loging......",Toast.LENGTH_SHORT).show();  
	    	  new SignInProcess().execute(user,pass);   //调用后台服务，请求登录
	      }
   }
   
    //注册按钮绑定的事件：跳转到注册界面
   public void sign(View view){
	   Intent intent = new Intent(login.this, register.class);
       startActivity(intent);
   }
   
   //修改密码,跳到修改密码界面
   public void change(View view){
	   Intent intent = new Intent(login.this, ChangePassword.class);
       startActivity(intent);
   }
   //后台线程，执行异步任务，这里是和后台服务通讯并接收返回的数据 SignInProcess发送到signin
   /*AsyncTask:
    * 第1个参数：启动任务时输入的参数类型
    * 第2个参数：后台任务执行中返回进度值得类型
    * 第3个参数：后台任务执行完成后返回的结果类型
    * 方法
    * doInBackground:必须重写,异步执行后台线程要完成的任务,耗时操作将在此方法中完成.
	  onPreExecute:执行后台耗时操作前被调用,通常用于进行初始化操作.
      onPostExecute:当doInBackground方法完成后,系统将自动调用此方法,并将doInBackground方法返回的值传入此方法.通过此方法进行UI的更新.
      onProgressUpdate:当在doInBackground方法中调用publishProgress方法更新任务执行进度后,
          			      将调用此方法.通过此方法我们可以知晓任务的完成进度
    */
    private class SignInProcess extends AsyncTask<String, String, String> {

	//private static final int VISIBLE = 0;
	//private static final int VISIABLE = 0;

	@Override
       protected String doInBackground(String... params) {  
           String username = params[0];
           String password = params[1];
           String result = "";
           /*
            * 192.168.197.1:8080  192.168.1.122:8080  要在真机上使用的话需要查找计算机在局域网中的IPV4地址 ipconfig/all
            * 前面的用自己的IP地址替换  后面是发布在Tomcat上的项目名以及对应的文件夹/文件
            */
       
           String s_url = "http://10.63.22.33:8080/testAndroidJSP/index.jsp";  
           s_url = s_url + "?username=" + username + "&password=" + password;    //记得带参数，Manifest设置网络连接权限！     
           /*HttpURLConnection
            * step1:URL url = new URL(https://www.baidu.com);  						      创建URL对象
            * step2:HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 获取对象实例
            * step3:conn.setRequestMethod("GET");  									      设置请求方式
            * step4:conn.setConnectTimeout(6*1000); conn.setReadTimeout(6 * 1000);    
            * 设置连接超时，读取超时的毫秒数，以及服务器希望得到的一些消息头
            * 此处可增加一些其他判断操作，如状态码的判断......
            * step5:InputStream in = conn.getInputStream(); 							
            * 调用getInputStream()方法获得服务器返回的输入流，然后输入流进行读取
            * step6:conn.disconnect();	关闭HTTP连接
            */
           try {
               URL url = new URL(s_url);
               HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
               conn.setRequestMethod("GET");
               conn.setConnectTimeout(3000); // 设置超时时间 ms
               conn.setReadTimeout(3000);
               conn.connect();
               if(conn.getResponseCode() == 200){  				//状态码
            	   InputStream is = conn.getInputStream();		//InputStream获得字节输入流
                   InputStreamReader reader = new InputStreamReader(is, "UTF-8");  //字节到字符的桥梁,规定使用的字符集为UTF-8
                   int temp;
                   while((temp=reader.read()) != -1) { 			//字节转字符方法
                       result += (char)temp;
                   }
               }
           } catch(Exception e) {
               e.printStackTrace();
           }
           
           return result;	//服务器以JSON转换的字符串传回来，但是因为是字节输入流所以要转换成字符
       }

	@Override
       protected void onPostExecute(String result) {   //doInBackground的返回值将传入此方法,更新UI
           try {
               JSONObject result_json = new JSONObject(result);	//字符串转JSONObject
               if(result_json.has("error")) {					//键->error? 有error的键
                   String error_code;
                   error_code = result_json.getString("error");  //获得值
                   err.setText(error_code);
                   err.setVisibility(View.VISIBLE);				//接受错误信息并通过TextView显示
                   password.setText(null);
               } else {
                   SignInSuccess(result_json);
            
               }
           } catch (Exception e) {
               e.printStackTrace();
           }
       }

   }
   //成功接收到数据后的操作，数据以JSON形式传递
   private void SignInSuccess(JSONObject info) {
       Intent intent = new Intent(this, Welcome.class);
       Toast.makeText(this,"登录成功",Toast.LENGTH_SHORT).show();
       try {
           intent.putExtra("username", username.getText().toString());   
           //intent.putExtra("name", info.getString("name")); 
           /*
            * 送到的Activity怎么取Indent？
            * 1.Intent intent =getIntent();  取得启动该Activity的Intent对象
            * 2.String first = intent.getStringExtra("et1");  调用getStringExtra("键")
            */
       } catch (Exception e) {
           e.printStackTrace();
       }
       startActivity(intent);   //跳转到Welcome的页面  数据存储在intent中
   } 

}