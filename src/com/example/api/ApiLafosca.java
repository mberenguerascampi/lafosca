package com.example.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.example.utils.*;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;


/**
 * Clase que interactua con la API
 * @author Marc Berengueras
 *
 */
public class ApiLafosca {
	private JSONObject json;
	private ApiDelegate delegate;
	private int statusCode;
	private String loginUser = "";
	private String loginPassword = "";
	private String errorMsg = "";
	private String authenticationToken = "";
	
	/**
	 * Constructor de la clase
	 */
	public ApiLafosca(ApiDelegate delegate) {
		this.delegate = delegate;
	}
	
	/**
	 * Funci�n que permite realizar una petici�n de GET a la api
	 * @param url Direccion donde realizar la petici�n
	 * @param type tipo de petici�n
	 * @param context contexto de la aplicaci�n
	 */
	private void startGetRequest(String url, ApiReturnType type, Context context)
	{
		json = null;
		new ApiThreadGet(type, url, context).execute();
	}
	
	/**
	 * Funci�n que permite realizar una petici�n de POST a la api
	 * @param url Direccion donde realizar la petici�n
	 * @param type tipo de petici�n
	 * @param context contexto de la aplicaci�n
	 * @param postObject objeto de tipo JSON que queremos pasar
	 * @throws JSONException
	 */
	private void startPostRequest(String url, ApiReturnType type, Context context, JSONObject postObject) throws JSONException
	{
		json = null;
		new ApiThreadPost(type, url, context).execute(postObject);
	}
	
	/**
	 * Funci�n que permite realizar una petici�n de PUT a la api
	 * @param url Direccion donde realizar la petici�n
	 * @param type tipo de petici�n
	 * @param context contexto de la aplicaci�n
	 */
	private void startPutRequest(String url, ApiReturnType type, Context context)
	{
		json = null;
		new ApiThreadPut(type, url, context).execute();
	}
	
	/**
	 * Funcion que crea un nuevo usario
	 * @param username Nombre del nuevo usuario
	 * @param password Contrase�a del usuario
	 * @param context Contexto de la aplicaci�n
	 * @throws JSONException
	 */
	public void createNewUser(String username, String password, Context context) throws JSONException{
		JSONObject jsonUserProperties = new JSONObject();
		JSONObject jsonUser = new JSONObject();
		jsonUserProperties.put("username",username);
		jsonUserProperties.put("password",password);
		jsonUser.put("user", jsonUserProperties);
		startPostRequest(LaFoscaConstants.API_DEFAULT_BASE_URL+"users", ApiReturnType.kUser, context, jsonUser);
	}
	
	/**
	 * M�todo que permite hace login en la aplicaci�n
	 * @param username nombre de usuario
	 * @param password contrase�a del usuario
	 * @param context contexto de la aplicaci�n
	 */
	public void logInUser(String username, String password, Context context){
		loginUser = username;
		loginPassword = password;
		startGetRequest(LaFoscaConstants.API_DEFAULT_BASE_URL+"user", ApiReturnType.kUser, context);
	}
	
	/**
	 * M�todo que permite obtener el estado de la playa
	 * @param authenticationToken token para obtener la autorizaci�n de acceso
	 * @param context contexto de la aplicaci�n
	 */
	public void getBeachState(String authenticationToken, Context context){
		this.authenticationToken = authenticationToken;
		startGetRequest(LaFoscaConstants.API_DEFAULT_BASE_URL+"state", ApiReturnType.kBeach, context);
	}
	
	/**
	 * M�todo que permite abrir la playa
	 * @param authenticationToken token para obtener la autorizaci�n de acceso
	 * @param context contexto de la aplicaci�n
	 */
	public void openBeach(String authenticationToken, Context context){
		this.authenticationToken = authenticationToken;
		startPutRequest(LaFoscaConstants.API_DEFAULT_BASE_URL+"open", ApiReturnType.kOpenBeach, context);
	}
	
	/**
	 * M�todo que permite cerrar la playa
	 * @param authenticationToken token para obtener la autorizaci�n de acceso
	 * @param context contexto de la aplicaci�n
	 */
	public void closeBeach(String authenticationToken, Context context){
		this.authenticationToken = authenticationToken;
		startPutRequest(LaFoscaConstants.API_DEFAULT_BASE_URL+"close", ApiReturnType.KCloseBeach, context);
	}
	
	/**
	 * M�todo al que se llama cuando la petici�n ha tenido exito
	 * @param statusCode C�digo devuelto po la api
	 * @param returnType Tipo de objeto 
	 */
	private void requestError(int statusCode, ApiReturnType returnType){
		delegate.requestFailed(errorMsg, returnType);
	}
	
	/**
	 * M�todo al que se llama cuando ha habido un error al realizar la petici�n
	 * @param returnType Tipo de objeto
	 */
	private void requestSuccess(ApiReturnType returnType){
		delegate.requestSucceded(json, returnType);
	}
	
	/**
	 * Funci�n que perite convertir un InputStream en un String
	 * @param is InputStream que deseamos convertir
	 * @return
	 */
	public static String convertStreamToString(InputStream is) 
	{
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
	
	/**
	 * Thread que nos permite realizar una petici�n de GET de forma as�ncrona
	 * @author Marc
	 *
	 */
	public class ApiThreadGet extends AsyncTask<Void, Void, HttpResponse> {
		ApiReturnType returnType;
		String url;
		Context context;
			
		public ApiThreadGet(ApiReturnType type, String loadUrl, Context context) {
			returnType = type;
			url = loadUrl;
			this.context = context;
		}

		protected HttpResponse doInBackground(Void...voids) {
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 30000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
				
			HttpClient httpclient = new DefaultHttpClient(httpParameters);

		    // Prepare a request object
		    HttpGet httpget = new HttpGet(url); 
		    
		    //A�adimos la autorizaci�n en el caso oportuno
		    if (returnType.equals(ApiReturnType.kUser) && !loginUser.equals("")){
		    	 Log.i("USER", loginUser);
		    	 UsernamePasswordCredentials credentials =
		                 new UsernamePasswordCredentials(loginUser, loginPassword);
		             BasicScheme scheme = new BasicScheme();
		             Header authorizationHeader;
					try {
						authorizationHeader = scheme.authenticate(credentials, httpget);
						httpget.addHeader(authorizationHeader);
						httpget.setHeader("Accept", "application/json");
						httpget.setHeader("Content-type", "application/json");
					} catch (AuthenticationException e) {
						e.printStackTrace();
					}
		    }
		    
		    else if (returnType.equals(ApiReturnType.kBeach) && !authenticationToken.equals("")){
					httpget.setHeader("Authorization", "Token token="
		                     + authenticationToken);
					httpget.setHeader("Accept", "application/json");
					httpget.setHeader("Content-type", "application/json");
		    }
		
		    HttpResponse response = null;

		    try {
		    	response = httpclient.execute(httpget);
		    }
		    catch (ClientProtocolException e) {
		        e.printStackTrace();
		    } 
		    catch (IOException e) {
		    	e.printStackTrace();
		    } 
		        
		    if(response == null) return null;
		        
		    try {
				//obtenemos la respuesta
				HttpEntity entity = response.getEntity();

		        if (entity != null){
		        	String temp = EntityUtils.toString(entity);
					errorMsg =  response.getStatusLine().toString();
					json=new JSONObject(temp);
					Log.i("RESPONSE JSON",temp);
		        }
			} 
			catch (IllegalStateException e){
				e.printStackTrace();
			} 
			catch (IOException e){
				e.printStackTrace();
			} 
			catch (JSONException e){
				e.printStackTrace();
			}
		        
		    return response;
		}
			
		//Se ejecuta en el UI thread
		protected void onPostExecute(HttpResponse result){
			if(result == null){
				requestError(0, returnType);
				return;
			}
				
			statusCode = result.getStatusLine().getStatusCode();
				
			//Miramos si hay error o no y redireccionamos
			if(statusCode == 200){
				requestSuccess(returnType);
			}
			else{
				requestError(statusCode, returnType);
			}
		}
	}
		
	/**
	 * Thread que nos permite realizar una petici�n de POST de forma as�ncrona
	 * @author Marc
	 *
	 */
	public class ApiThreadPost extends AsyncTask<JSONObject, Void, HttpResponse> {
		ApiReturnType returnType;
		String url;
		Context context;

		public ApiThreadPost(ApiReturnType type, String loadUrl, Context context){
			returnType = type;
			url = loadUrl;
			this.context = context;
		}

		protected HttpResponse doInBackground(JSONObject ... jsonObj){
			HttpClient httpclient = new DefaultHttpClient();

			// Prepare a request object
			HttpPost httppost = new HttpPost(url);
			HttpResponse response = null;

			try {
				JSONObject user;
				try {
					user = jsonObj[0].getJSONObject("user");
					String nom = user.getString("username");
					String password = user.getString("password");
					Log.i("JSON", "Nom: " + nom + ", pass: " + password);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				
				StringEntity entity = new StringEntity(jsonObj[0].toString());
                httppost.setEntity(entity); 
                httppost.setHeader("Accept", "application/json");
                httppost.setHeader("Content-type", "application/json");
				response = httpclient.execute(httppost);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
					
			if(response == null) return null;
					
			try {
				// obtenemos la respuesta
				HttpEntity entity = response.getEntity();

				if (entity != null) {
					String temp = EntityUtils.toString(entity);
					errorMsg =  response.getStatusLine().toString();
					json=new JSONObject(temp);
					Log.i("RESPONSE",temp);
				}
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return response;
		}

		// Se ejecuta en el UI thread
		protected void onPostExecute(HttpResponse result){
			if (result == null) {
				requestError(0, returnType);
				return;
			}
					
			statusCode = result.getStatusLine().getStatusCode();
			Log.i("OnPostExecute", ""+statusCode);
			
			// Miramos si hay error o no y redireccionamos
			if (statusCode == 201) {
				requestSuccess(returnType);
			}
			else {
				requestError(statusCode, returnType);
			}
		}
	}
	
	
	/**
	 * Thread que nos permite realizar una petici�n de PUT de forma as�ncrona
	 * @author Marc
	 *
	 */
	public class ApiThreadPut extends AsyncTask<Void, Void, HttpResponse> {
		ApiReturnType returnType;
		String url;
		Context context;
			
		public ApiThreadPut(ApiReturnType type, String loadUrl, Context context) {
			returnType = type;
			url = loadUrl;
			this.context = context;
		}

		protected HttpResponse doInBackground(Void...voids) {
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 30000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
				
			HttpClient httpclient = new DefaultHttpClient(httpParameters);

		    // Prepare a request object
		    HttpPut httpput = new HttpPut(url); 
		    
		    //A�adimos la autorizaci�n en el caso oportuno
		    if (!authenticationToken.equals("")){
		    		httpput.setHeader("Authorization", "Token token="
		                     + authenticationToken);
		    		httpput.setHeader("Accept", "application/json");
		    		httpput.setHeader("Content-type", "application/json");
		    }
		
		    HttpResponse response = null;

		    try {
		    	response = httpclient.execute(httpput);
		    }
		    catch (ClientProtocolException e) {
		        e.printStackTrace();
		    } 
		    catch (IOException e) {
		    	e.printStackTrace();
		    } 
		        
		    if(response == null) return null;
		        
		    try {
				//obtenemos la respuesta
				HttpEntity entity = response.getEntity();

		        if (entity != null){
		        	String temp = EntityUtils.toString(entity);
					errorMsg =  response.getStatusLine().toString();
					//json=new JSONObject(temp);
					Log.i("RESPONSE JSON",temp);
		        }
			} 
			catch (IllegalStateException e){
				e.printStackTrace();
			} 
			catch (IOException e){
				e.printStackTrace();
			} 
		        
		    return response;
		}
			
		//Se ejecuta en el UI thread
		protected void onPostExecute(HttpResponse result){
			if(result == null){
				requestError(0, returnType);
				return;
			}
				
			statusCode = result.getStatusLine().getStatusCode();
				
			//Miramos si hay error o no y redireccionamos
			if(statusCode == 200){
				requestSuccess(returnType);
			}
			else{
				requestError(statusCode, returnType);
			}
		}
	}
}
