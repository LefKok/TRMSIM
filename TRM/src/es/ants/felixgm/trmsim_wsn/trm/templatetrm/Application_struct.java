package es.ants.felixgm.trmsim_wsn.trm.templatetrm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import es.ants.felixgm.trmsim_wsn.network.Service;

public class Application_struct {

	
	String domain;
	HashMap	<String,Followee_struct> followees;
	Service service;
	
	public Application_struct(String domain){
		this.domain=domain;
		service=new Service(domain);
		followees = new HashMap<String, Followee_struct>();
	}
	
	/*add VE to friends List*/
	public void followee_put(TemplateTRM_Sensor key) {
		 followees.put(Integer.toString(key.id), new Followee_struct(key));
	}
	/* remove VE from friend List*/
	public void followee_remove(TemplateTRM_Sensor key) {
		followees.remove(Integer.toString(key.id));
	}
	public TemplateTRM_Sensor get_random_entry(){ //not tested
		ArrayList<String> keysAsArray = new ArrayList<String>(followees.keySet());
				Random r = new Random();				
		return followees.get(keysAsArray.get(r.nextInt(keysAsArray.size()))).Sensor;
	};
	
	
}
