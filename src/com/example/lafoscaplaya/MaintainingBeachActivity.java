package com.example.lafoscaplaya;

import com.example.api.ApiDelegate;
import com.example.api.ApiLafosca;
import com.example.api.ApiReturnType;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MaintainingBeachActivity extends Activity implements ApiDelegate{
	private String authenticationToken = "";
	private ApiLafosca api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintaining_beach);
		
		//Cogemos el authenticationToken que ha pasado la actividad de login
		Bundle b = getIntent().getExtras();
		authenticationToken = b.getString("authenticationToken");
		//Log.i("authenticationToken", authenticationToken);
		
		//Inicializamos la api
		api = new ApiLafosca(this);
		
		 //Le añadimos al botón de obtener el estado de la playa la accion que debe realizar
        Button getBeachBtn = (Button)findViewById(R.id.beach_state_button);
        getBeachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	getBeachState();
            }
        });
	}
	
	/**
	 * Función que permite obtener el estado de la playa
	 */
	private void getBeachState(){
		api.getBeachState(authenticationToken, this.getBaseContext());
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.maintaining_beach, menu);
		return true;
	}

	//Funciones que implementan el ApiDelegate
	@Override
	public void requestSucceded(Object pid, ApiReturnType response) {
		
	}

	@Override
	public void requestFailed(String msn, ApiReturnType response) {
		
	}

}
