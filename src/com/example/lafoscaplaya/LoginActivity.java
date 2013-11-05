package com.example.lafoscaplaya;

import com.example.api.ApiDelegate;
import com.example.api.ApiLafosca;
import com.example.api.ApiReturnType;

import android.opengl.Visibility;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements ApiDelegate {
	private ApiLafosca api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		 //Inicializamos la api
        api = new ApiLafosca(this);
        
        //Le añadimos al botón de login la accion que debe realizar
        Button loginBtn = (Button)findViewById(R.id.login_button);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	onLoginButtonPressed();
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	 /**
     * Función que permite hacer login con el usuario creado
     */
    private void onLoginButtonPressed(){
    	EditText username = (EditText)findViewById(R.id.username_editText);
    	EditText password = (EditText)findViewById(R.id.password_editText);
    	api.logInUser(username.getText().toString(), password.getText().toString(), this.getBaseContext());
    }
    
    private void finishActivityAfterToastDisappears(){
    	Thread thread = new Thread(){
            @Override
           public void run() {
                try {
                   Thread.sleep(3500); // As I am using LENGTH_LONG in Toast
                   LoginActivity.this.finish();
               } catch (Exception e) {
                   e.printStackTrace();
               }
            }  
          };
          thread.start();
    }

    //Funciones que implementan el ApiDelegate
	@Override
	public void requestSucceded(Object pid, ApiReturnType response) {
		TextView errorText = (TextView)findViewById(R.id.error_text);
		errorText.setVisibility(View.GONE);
		Toast.makeText(getApplicationContext(), getString(R.string.login_successful),
				   Toast.LENGTH_LONG).show();
		finishActivityAfterToastDisappears();
	}

	@Override
	public void requestFailed(String msn, ApiReturnType response) {
		TextView errorText = (TextView)findViewById(R.id.error_text);
		errorText.setVisibility(View.VISIBLE);
	}

}
