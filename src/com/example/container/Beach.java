package com.example.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;

/**
 * Clase que representa la entidad de Playa
 *@author Marc Berengueras
 *
 **/
@SuppressLint("UseValueOf")
public class Beach {
	private String state;
	private int flag;
	private int happiness;
	private int dirtiness;
	private ArrayList<Kid> kids = new ArrayList<Kid>();
	
	public Beach(JSONObject json) throws JSONException{
		state = json.getString("state");
		if(!state.equals("closed")){
			flag = json.getInt("flag");
			happiness = json.getInt("happiness");
			dirtiness = json.getInt("dirtiness");
			
			//Obtenemos la lista de niños
			JSONArray jsonKids = json.getJSONArray("kids");
			for(int i = 0; i < jsonKids.length(); ++i){
				JSONObject iJsonKid = jsonKids.getJSONObject(i);
				Kid kid = new Kid(iJsonKid.getString("name"), iJsonKid.getInt("age"));
				kids.add(kid);
			}
			sortKids();
		}
	}
	
	@SuppressLint("UseValueOf")
	private void sortKids(){
		Collections.sort(kids, new Comparator<Kid>() {
			public int compare(Kid a, Kid b) {
		        return new Integer(a.getAge()).compareTo(new Integer(b.getAge()));
		    }
		});
		for(int i = 0; i < kids.size(); ++i){
			Log.i("KID", kids.get(i).getName());
		}
	}
	
	
	//Getters i setters
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public int getHappiness() {
		return happiness;
	}
	public void setHappiness(int happiness) {
		this.happiness = happiness;
	}
	public int getDirtiness() {
		return dirtiness;
	}
	public void setDirtiness(int dirtiness) {
		this.dirtiness = dirtiness;
	}
	public ArrayList<Kid> getKids() {
		return kids;
	}
	public void setKids(ArrayList<Kid> kids) {
		this.kids = kids;
	}
}
