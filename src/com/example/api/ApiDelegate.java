package com.example.api;

/**
 * Interface encargada de recibir los resultados a una petición realizada a la API
 * @author Marc Berengueras
 * 
 **/
public interface ApiDelegate {
	public void requestSucceded(Object pid, ApiReturnType response);
	public void requestFailed(String msn, ApiReturnType response);
}
