package es.ants.felixgm.trmsim_wsn.trm.templatetrm;


import java.util.concurrent.LinkedBlockingDeque;

import es.ants.felixgm.trmsim_wsn.satisfaction.SatisfactionInterval;

public class Followee_struct {

	public Followee_struct(TemplateTRM_Sensor S) {
		shares = new  LinkedBlockingDeque<MyTransaction>();
		assists = new  LinkedBlockingDeque<MyTransaction>();
		this.Sensor = S;

	}
	
	public Followee_struct(TemplateTRM_Sensor S,double trust) {
		shares = new  LinkedBlockingDeque<MyTransaction>();
		assists = new  LinkedBlockingDeque<MyTransaction>();
		this.Sensor = S;
		this.trust=trust;
	}

	TemplateTRM_Sensor Sensor;
	protected  LinkedBlockingDeque<MyTransaction> shares;
	protected  LinkedBlockingDeque<MyTransaction> assists;
	protected double trust = 0.0; /* from shares */
	protected double rec_trust = 0.0; /* from assists */
	protected double reputation = 0.0;
	protected int n_shares = 0;
	protected int n_assists = 0;

	public synchronized void fade() {
		for (MyTransaction t : shares)
			if ((t.fading -= 0.02) <= 0)
				shares.remove(t);
		for (MyTransaction t : assists)
			if ((t.fading -= 0.02) <= 0)
				assists.remove(t);
	}
	public synchronized void fade(double value) {
		for (MyTransaction t : shares)
			if ((t.fading -= value) <= 0)
				shares.remove(t);
		for (MyTransaction t : assists)
			if ((t.fading -= value) <= 0)
				assists.remove(t);
	}

	double calculate_Trust() {
		if ((Math.random() < 0.2) && (shares.size() < 2))
			return 1.0; // if new friend (20% chance to trust him)
		if (shares.size() >10){
		double temp1 = get_trust(shares, shares.size()/10);
		double temp2 = get_trust(shares, shares.size());
		trust = Math.min(temp1, temp2);
		}
		else if(shares.size() >0) trust = get_trust(shares, shares.size());
		 //System.out.println(Sensor.toString()+"trust="+trust);
		return trust;
	}

	private double get_trust( LinkedBlockingDeque<MyTransaction> assists2, int size) {
		double sum = 0.0;// wi*xi
		double weights = 0.0;// wi
		double wx2 = 0.0;// wi*xi^2
		for (int i = 0; i < size; i++) {
			MyTransaction t = assists2.poll();
			if (t!=null){
			double weight = t.fading * t.severity;
			double xi = t.getSatisfaction().getSatisfactionValue();
			double temp = weight * xi;
			sum += temp; // �wi*xi
			wx2 += temp * xi;// �wi*xi*xi
			weights += weight;// �wi
		}
		}
		fade();

		double m = sum / weights;
		double s = (wx2 * weights - Math.pow(sum, 2)) / Math.pow(weights, 2);
	

		return m - s;
	}

	double calculate_rec_Trust() {
		if ((Math.random() < 0.1) && (assists.size() == 0))
			return 1.0; // if new friend (20% chance to trust him)
		rec_trust = get_trust(assists, assists.size());
		return rec_trust;
		

	}

	public MyOutcome requestService(String Service) {
		SatisfactionInterval satisfaction = null;
		try {
			satisfaction = new SatisfactionInterval(0.0, 1.0,
					Sensor.serve(Service));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new MyOutcome(satisfaction);
	}

	public synchronized double return_trust() {
		return trust;
	}
}