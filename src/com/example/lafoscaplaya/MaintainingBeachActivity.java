package com.example.lafoscaplaya;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.api.ApiDelegate;
import com.example.api.ApiLafosca;
import com.example.api.ApiReturnType;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MaintainingBeachActivity extends Activity implements ApiDelegate{
	private String authenticationToken = "";
	private ApiLafosca api;
	private boolean closed = false;

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
        
        //Le añadimos al botón de abrir/cerrar la playa la accion que debe realizar
        Button openCloseBtn = (Button)findViewById(R.id.open_close_button);
        openCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	onOpenCloseButtonPressed();
            }
        });
	}
	
	/**
	 * Función que permite obtener el estado de la playa
	 */
	private void getBeachState(){
		api.getBeachState(authenticationToken, this.getBaseContext());
	}
	
	private void onOpenCloseButtonPressed(){
		if(closed){
			api.openBeach(authenticationToken, this.getBaseContext());
		}
		else{
			api.closeBeach(authenticationToken, this.getBaseContext());
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.maintaining_beach, menu);
		return true;
	}
	
	/**
	 * Función que muestra el estado recibido de la playa
	 * @param json estado de la playa en format JSON
	 * @throws JSONException
	 */
	private void setStateText(JSONObject json) throws JSONException{
		TextView stateText = (TextView)findViewById(R.id.state_text);
		String state = json.getString("state");
		closed = (state.equals("closed"));
		stateText.setText(json.toString());
	}
	
	private void showToast(String text){
		Toast.makeText(getApplicationContext(), text,
				   Toast.LENGTH_LONG).show();
	}

	//Funciones que implementan el ApiDelegate
	@Override
	public void requestSucceded(Object pid, ApiReturnType response) {
		switch(response){
			case kBeach:
				try {
					setStateText((JSONObject)pid);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
				
			case kOpenBeach:
				closed = false;
				showToast(getString(R.string.open_beach));
				break;
				
			case KCloseBeach:
				closed = true;
				showToast(getString(R.string.close_beach));
				break;
				
			default:
				break;
		}
	}

	@Override
	public void requestFailed(String msn, ApiReturnType response) {
		
	}

}
