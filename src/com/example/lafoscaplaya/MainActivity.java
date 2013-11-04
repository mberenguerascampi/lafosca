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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Le añadimos al botón de crear usuario la accion que debe realizar
        Button createUserBtn = (Button)findViewById(R.id.create_button);
        createUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	onCreateUserButtonPressed();
            }
        });
    }

    /**
     * Función que coje los valores de los editText i crea un nuevo usuario
     */
    private void onCreateUserButtonPressed(){
    	EditText username = (EditText)findViewById(R.id.username_editText);
    	EditText password = (EditText)findViewById(R.id.password_editText);
    	ApiLafosca api = new ApiLafosca();
    	try {
			api.createNewUser(username.getText().toString(), password.getText().toString(), this.getBaseContext());
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
