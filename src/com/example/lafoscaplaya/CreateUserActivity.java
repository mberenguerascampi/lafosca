package com.example.lafoscaplaya;

import org.json.JSONException;

import com.example.api.ApiDelegate;
import com.example.api.ApiLafosca;
import com.example.api.ApiReturnType;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateUserActivity extends Activity implements ApiDelegate{
	private ApiLafosca api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_user);
		
		 //Inicializamos la api
        api = new ApiLafosca(this);
        
        //Le añadimos al botón de crear usuario la accion que debe realizar
        Button createUserBtn = (Button)findViewById(R.id.create_button);
        createUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	onCreateUserButtonPressed();
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_user, menu);
		return true;
	}
	
	 /**
     * Función que coje los valores de los editText i crea un nuevo usuario
     */
    private void onCreateUserButtonPressed(){
    	EditText username = (EditText)findViewById(R.id.username_editText);
    	EditText password = (EditText)findViewById(R.id.password_editText);
    	try {
			api.createNewUser(username.getText().toString(), password.getText().toString(), this.getBaseContext());
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
    private void finishActivityAfterToastDisappears(){
    	Thread thread = new Thread(){
            @Override
           public void run() {
                try {
                   Thread.sleep(3500); // As I am using LENGTH_LONG in Toast
                   CreateUserActivity.this.finish();
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
		Toast.makeText(getApplicationContext(), getString(R.string.user_created),
				   Toast.LENGTH_LONG).show();
		finishActivityAfterToastDisappears();
	}

	@Override
	public void requestFailed(String msn, ApiReturnType response) {

	}

}
