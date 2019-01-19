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
    * check����¼��󶨵�ȷ�ϣ�cancel�󶨵�ȡ��
    * check����������ȷ������ļ���������֤�������Ϣ�Ƿ����Ҫ���û�������5λ�����10λ��
    * ��Ӣ����ĸ��ͷ��ֻ�������Ӣ����ĸ�������Լ�_��
    * ͬʱ����������һ����дӢ����ĸ������Ϊ6-12λ��ֻ�������Ӣ����ĸ�����ֺ�_��ͬʱҪ��ȷ���������������һ�£���
    * �粻����Ҫ�����ڽ�������ʾ����ֻ�з���Ҫ����ύ����̨����ע�������
    * ��ע��ɹ�����ת����ӭ���棬������ת��ע����沢��ʾ�û���ע�����ԭ��
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
		    * 1.�û�������Ϊ��
		    * 2.�û�������Ϊ5-10λ
		    * 3.�û���������Ӣ����ĸ��ͷ��ֻ�������Ӣ����ĸ�������Լ�_
		    * 4.�û�������������һ����дӢ����ĸ
		    * 5.����&ȷ�����벻��Ϊ��
		    * 6.����&ȷ���������Ϊ6-12λ
		    * 7.����&ȷ������ֻ�������Ӣ����ĸ�����ֺ�_
		    * 8.������ȷ���������������һ��
		    */
		   String mess = "";
		   if(username.isEmpty()){
			    textview.setText("�û�������Ϊ��");
			    textview.setVisibility(View.VISIBLE);
			    return ;
		   }
	       if(username.length() < 5 || username.length() > 10)
	            mess += " �û�������Ϊ5-10λ ";
	        String pattern1 = "^[a-zA-Z][a-zA-Z0-9_]*$";    //^h��ʾ������h��ͷ,[]ƥ�䷽�����������ַ�,$�÷��Ų�ƥ���κ��ַ�,�ַ���������λ��,��r$������r��β
	        String pattern2 = ".*[A-Z].*";					//����һ����дӢ����ĸ
	        boolean match1 = Pattern.matches(pattern1, username);
	        boolean match2 = Pattern.matches(pattern2, username);
	        if(!match1) {
	            mess += "�û���������Ӣ����ĸ��ͷ,ֻ�������Ӣ����ĸ�������Լ�_ ";
	        }
	        if(!match2) {
	            mess += "�û���������һ����дӢ����ĸ ";
	        }
	        
	        if(!mess.isEmpty()) {
	        	textview.setText(mess);
			    textview.setVisibility(View.VISIBLE);
			    return ;
	        }
	         
		   //check����
	        if(password.isEmpty()){
			    textview.setText("���벻��Ϊ��");
			    textview.setVisibility(View.VISIBLE);
			    return ;
		   }
		    mess = "";
	        if(password.length() < 6 || password.length() > 12 ||sure_password.length()>12||password.length()<6)
	            mess += "�������Ϊ6-12λ  ";
	        if(!password.equals(sure_password)){
	        	mess +="������������벻һ�� ";
	        }
	        String pattern3 = "[a-zA-Z0-9_]*";
	        boolean match3 = Pattern.matches(pattern3, password);
	        if(!match3) {
	            mess += "����ֻ�������Ӣ����ĸ�����ֺ�_ ";
	        }
	        if(!mess.isEmpty()) {
	        	textview.setText(mess);
			    textview.setVisibility(View.VISIBLE);
			    return ;
	        }
	       /*
	        * ������ȷ����֤��ɺ�ʹ��AlertDialog�����û�ȷ��
	        */
		   else {
			   textview.setVisibility(View.INVISIBLE);
		       showNormalDialog();
		   }
	}
	public void showNormalDialog(){
		 final AlertDialog.Builder normalDialog =  new AlertDialog.Builder(this);
		 normalDialog.setIcon(R.drawable.ic_launcher);
		 normalDialog.setTitle("��ܰ��ʾ");
	     normalDialog.setMessage("ȷ��ע�᣿");
	     normalDialog.setPositiveButton("ȷ��", 
	     new DialogInterface.OnClickListener() {
	    	 @Override
	    	 public void onClick(DialogInterface dialog, int which) {
	                //...To-do
	    		    new SignUpProcess().execute(uText.getText().toString(), psText.getText().toString());  //���ȫ�����ʹ���û������������ע��
	            }
	     });
	     normalDialog.setNegativeButton("ȡ��", 
	     new DialogInterface.OnClickListener() {
	    	 @Override
	         public void onClick(DialogInterface dialog, int which) {
	                //...To-do
	            }
	        });
	      // ��ʾ
	      normalDialog.show();
		        
	}
	/*
	 * ��ע��ɹ�����ת����ӭ���棬������ת��ע����沢��ʾ�û���ע�����ԭ��
	 * ����ԭ�����û����Ѵ���
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
	            * step1:URL url = new URL(https://www.baidu.com);  						      ����URL����
	            * step2:HttpURLConnection conn = (HttpURLConnection) url.openConnection(); ��ȡ����ʵ��
	            * step3:conn.setRequestMethod("GET");  									      ��������ʽ
	            * step4:conn.setConnectTimeout(6*1000); conn.setReadTimeout(6 * 1000);     �������ӳ�ʱ����ȡ��ʱ�ĺ��������Լ�������ϣ���õ���һЩ��Ϣͷ
	            * step5:InputStream in = conn.getInputStream(); ����getInputStream()������÷��������ص���������Ȼ�����������ж�ȡ
	            * step6:conn.disconnect();		�ر�HTTP����
	            * PS:step4�ɶ�״̬������жϣ�Ϊ200�����
	            */
	           try {
	               URL url = new URL(s_url);
	               HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
	               conn.setRequestMethod("GET");
	               conn.connect();
	               InputStream is = conn.getInputStream();
	               InputStreamReader reader = new InputStreamReader(is, "UTF-8");
	               int temp;
	               while((temp=reader.read()) != -1) { //reader�෽����
	                   result += (char)temp;
	               }
	           } catch(Exception e) {
	               e.printStackTrace();
	           }
	           
	           return result;	//��������JSON��ʽ����������ת��Ϊ�ַ���
	       }

		@Override
	       protected void onPostExecute(String result) {   //doInBackground�ķ���ֵ������˷���,����UI
	           try {
	               JSONObject result_json = new JSONObject(result);
	               if(result_json.has("error")) {	
	                   String error_code;
	                   error_code = result_json.getString("error");  //���ֵ
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
	   //�ɹ����յ����ݺ�Ĳ�����������JSON��ʽ����
	   private void SignUpSuccess(JSONObject info) {
	       Intent intent = new Intent(this, Welcome.class);
	       Toast.makeText(this,"ע��ɹ�",Toast.LENGTH_SHORT).show();
	       try {
	           intent.putExtra("username", uText.getText().toString());
	       } catch (Exception e) {
	           e.printStackTrace();
	       }
	       startActivity(intent);   //��ת��Welcome��ҳ��  ���ݴ洢��intent��
	   } 
}
