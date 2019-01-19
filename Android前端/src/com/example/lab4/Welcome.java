package com.example.lab4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Welcome extends Activity{
	private static final int VISIBLE = 0;
	private TextView show;
	private Button bt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		bt = (Button) findViewById(R.id.welcome_bt);
		show = (TextView) findViewById(R.id.welcome_text);
		Intent intent = getIntent();  
        String str = "Welcome " + intent.getStringExtra("username");  
        show.setText(str);
        show.setVisibility(VISIBLE);
	}
	public void logout(View view){
		Intent intent = new Intent(this, login.class);
		startActivity(intent);
	}
}
