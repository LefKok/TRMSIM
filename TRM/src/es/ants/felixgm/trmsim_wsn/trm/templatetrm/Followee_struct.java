package es.ants.felixgm.trmsim_wsn.trm.templatetrm;

import java.util.LinkedList;


public class Followee_struct {
	
	
    public Followee_struct(TemplateTRM_Sensor S) {
    	shares = new LinkedList<Transaction>();
    	assists = new LinkedList<Transaction>();
    	this.Sensor=S;

	}
    TemplateTRM_Sensor Sensor;
	protected LinkedList<Transaction> shares;
    protected LinkedList<Transaction> assists;
    protected double trust=0.0;	 	/* from shares*/
    protected double rec_trust=0.0; /* from assists*/
    protected double reputation=0.0;
    protected int n_shares=0;
    protected int n_assists=0;

    public synchronized void fade(){
    	for (Transaction t : shares)
    		if ((t.fading-=0.05)==0)
    			 shares.remove(t);
    	for (Transaction t : assists)
    		if ((t.fading-=0.05)==0)
    			assists.remove(t);    
	}

    
    private void calculate_Trust(){
    	trust=0.0;
    	n_shares=1;		
    }
    
    private void calculate_rec_Trust(){
    	trust=0.0;
    	n_shares=1;		
    }
}