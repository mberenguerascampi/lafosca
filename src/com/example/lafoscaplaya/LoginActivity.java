package com.example.lafoscaplaya;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.api.ApiDelegate;
import com.example.api.ApiLafosca;
import com.example.api.ApiReturnType;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements ApiDelegate {
	private String authenticationToken;
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
    
    private void StartNewActivityAfterToastDisappears(){
    	Thread thread = new Thread(){
            @Override
           public void run() {
                try {
                   Thread.sleep(3500); // As I am using LENGTH_LONG in Toast
                   Intent intent = new Intent(LoginActivity.this, MaintainingBeachActivity.class);
                   intent.putExtra("authenticationToken", authenticationToken);
                   startActivity(intent);
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
		//Obtenemos la correspondiente información
		JSONObject json = (JSONObject)pid;
		try {
			authenticationToken = json.getString("authentication_token");
			Log.i("authentication_token", authenticationToken);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		//Ocultamos el mensaje de error si estava visible y mostramos un Toast indicando 
		//que la petición se ha realizado correctamente
		TextView errorText = (TextView)findViewById(R.id.error_text);
		errorText.setVisibility(View.GONE);
		Toast.makeText(getApplicationContext(), getString(R.string.login_successful),
				   Toast.LENGTH_LONG).show();
		
		//Empezamos una nueva activity
		StartNewActivityAfterToastDisappears();
	}

	@Override
	public void requestFailed(String msn, ApiReturnType response) {
		TextView errorText = (TextView)findViewById(R.id.error_text);
		errorText.setVisibility(View.VISIBLE);
	}

}
