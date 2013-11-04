package com.example.lafoscaplaya;

import org.json.JSONException;

import com.example.api.ApiLafosca;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	private ApiLafosca api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Inicializamos la api
        api = new ApiLafosca();
        
        //Le añadimos al botón de crear usuario la accion que debe realizar
        Button createUserBtn = (Button)findViewById(R.id.create_button);
        createUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	onCreateUserButtonPressed();
            }
        });
        
      //Le añadimos al botón de login la accion que debe realizar
        Button loginBtn = (Button)findViewById(R.id.login_button);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	onLoginButtonPressed();
            }
        });
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
    
    /**
     * Función que permite hacer login con el usuario creado
     */
    private void onLoginButtonPressed(){
    	EditText username = (EditText)findViewById(R.id.username_editText);
    	EditText password = (EditText)findViewById(R.id.password_editText);
    	api.logInUser(username.getText().toString(), password.getText().toString(), this.getBaseContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
