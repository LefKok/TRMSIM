/**
 *  "TRMSim-WSN, Trust and Reputation Models Simulator for Wireless
 * Sensor Networks" is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version always keeping
 * the additional terms specified in this license.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 *
 * Additional Terms of this License
 * --------------------------------
 *
 * 1. It is Required the preservation of specified reasonable legal notices
 *   and author attributions in that material and in the Appropriate Legal
 *   Notices displayed by works containing it.
 *
 * 2. It is limited the use for publicity purposes of names of licensors or
 *   authors of the material.
 *
 * 3. It is Required indemnification of licensors and authors of that material
 *   by anyone who conveys the material (or modified versions of it) with
 *   contractual assumptions of liability to the recipient, for any liability
 *   that these contractual assumptions directly impose on those licensors
 *   and authors.
 *
 * 4. It is Prohibited misrepresentation of the origin of that material, and it is
 *   required that modified versions of such material be marked in reasonable
 *   ways as different from the original version.
 *
 * 5. It is Declined to grant rights under trademark law for use of some trade
 *   names, trademarks, or service marks.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program (lgpl.txt).  If not, see <http://www.gnu.org/licenses/>
 */

package es.ants.felixgm.trmsim_wsn.trm.templatetrm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import es.ants.felixgm.trmsim_wsn.network.Sensor;
import es.ants.felixgm.trmsim_wsn.network.Service;
import es.ants.felixgm.trmsim_wsn.outcomes.Outcome;
import es.ants.felixgm.trmsim_wsn.satisfaction.SatisfactionInterval;

/**
 * <p>
 * This class models a Sensor implementing TemplateTRM
 * </p>
 * 
 * @author LEfteris Kokoris
 */
public class TemplateTRM_Sensor extends Sensor {

	/**
	 * This constructor creates a new Sensor implementing TemplateTRM
	 * 
	 */
	TemplateTRM_Network platform;
	public HashMap<String, Double> servicesGoodness2;
	protected HashMap<String, Application_struct> friends; // vazw friends ana
	// application
	// epishs
	// xrismiopoiountai
	// kai gia
	// assits/recoms gia
	// alla appss
	protected static int _windowSize; 
	boolean malicious = false;

	public TemplateTRM_Sensor(TemplateTRM_Network Platform) {
		super();
		friends = new HashMap<String, Application_struct>();
		servicesGoodness2 = new HashMap<String, Double>();
		this.platform = Platform;
		friends.put("recommendation", new Application_struct("recommendation",
				Math.random() * 0.2 + 0.8));

	}

	/**
	 * This constructor creates a new Sensor implementing TemplateTRM
	 * 
	 * @param id
	 *            Identifier of the new sensor
	 * @param x
	 *            X coordinate of the new sensor
	 * @param y
	 *            Y coordinate of the new sensor
	 */
	public TemplateTRM_Sensor(int id, double x, double y,TemplateTRM_Network Platform) {
		super(id, x, y);
		friends = new HashMap<String, Application_struct>();
		servicesGoodness2 = new HashMap<String, Double>();
		this.platform = Platform;
		friends.put("recommendation", new Application_struct("recommendation",
				Math.random() * 0.5 + 0.5));
	}

	/**
	 * This method adds a new requestedService to the collection of shares of
	 * this sensor
	 * 
	 * @param name
	 *            the id of the service
	 * */

	public synchronized void addNewService(String name) {

		friends.put(name, new Application_struct(name,
				Math.random() * 0.5 + 0.5));
	}
	/**
	 * This method adds a new followee
	 * this sensor
	 * 
	 * @param followee the Sensor that is going to be added
	 * @param service the Service it is going to be added to
	 * */

	public synchronized void addfollowee(TemplateTRM_Sensor followee,
			String service,double trust) {
		// System.out.println(followee.toString());
		if (followee.id!=this.id)
		{
			this.addLink(followee);

			friends.get(service).followee_put(followee,trust);
			friends.get("recommendation").followee_put(followee,0.51);//also add him to the global struct of followees used for assists and recommendations

		}
	}

	public synchronized Set<String> getRequestedServices() {

		return friends.keySet();
	}

	@Override
	public void run() {
		outcome = null;
		Set<String> Services = getRequestedServices();
		//Set<String> my=  servicesGoodness2.keySet();
		//for (String currServ:my)
		if ((servicesGoodness2.get("My service")!=null ) && (servicesGoodness2.get("My service")< 0.5)) malicious=true;	
		for (String s : Services)// gia kathe service pou thelw
		{
			if (s.equals("My service")) {
				Application_struct new_requiredService = get_app(s);
				outcome = new_requiredService.request_Service(); // request service base on Trust on followees
				if (outcome == null)
				{ if (Math.random() < 0.3 ) outcome=get_assistance(s,5,this.id); //if(outcome!=null) System.out.println("assist"+((SatisfactionInterval)outcome.get_satisfaction()).getSatisfactionValue());}//null if i trust no one for assits or he refuses 
				else outcome=get_recommendation(s);	
				//if(outcome!=null)System.out.println("recommend"+((SatisfactionInterval)outcome.get_satisfaction()).getSatisfactionValue());}//same as above
				}
				if (outcome == null) 
				{	TemplateTRM_Sensor recommended=platform.get_recommendation(s);
				if (recommended!=null){
					addfollowee(recommended,s,0.9);
					outcome = new_requiredService.request_Service();
					//if(outcome!=null)
					//System.out.println("network"+((SatisfactionInterval)outcome.get_satisfaction()).getSatisfactionValue());// request service base on Trust on followees
				}
				}
				//if (outcome == null)outcome = new MyOutcome(new SatisfactionInterval(0.0, 1.0, 0.0));
				if (malicious && collusion)outcome = null;

			}
			if (Math.random() <0.1) { this.report(); 
			this.blacklist(); 
			}


		}
	}

	/**
	 * This method removes malicous VE's from friends
	 * */
	private synchronized void blacklist() {
		Set<String> Services = getRequestedServices();

		for (String s : Services)// gia kathe service pou thelw
		{
			if (s.equals("My service")) {

				Application_struct checkedService = friends.get(s);
				PriorityQueue<ComparableFriend> MyQueue=checkedService.rank_friends("trust");
				while (!MyQueue.isEmpty()){
					ComparableFriend temp = MyQueue.poll();
					if ( temp.value  < 0.5 && !( malicious && collusion )){//if the friend behaved malicoucly and i am not in a malicous collusion(where i recommend these friends)

						Followee_struct current = checkedService.get_followee(temp.id);
						checkedService.followee_remove(temp.id);
						this.removeLink(current.Sensor);
					}


				}

			}
		}		
	}

	/**
	 * This method returns the outcome of finding a new friend from recommendation and requesting the service s
	 * @param String s the Service 
	 * */
	private  Outcome get_recommendation(String s) {
		Application_struct struct1=null;
		TemplateTRM_Sensor my_new_sensor =null;
		Application_struct recommenders = friends.get("recommendation");
		PriorityQueue<ComparableFriend> MyQueue=recommenders.rank_friends("trust");
		ArrayList<Followee_struct> recommendations =  new ArrayList<Followee_struct>();
		LinkedList<TemplateTRM_Sensor> recommender =  new LinkedList<TemplateTRM_Sensor>();
		while (!MyQueue.isEmpty() && recommendations.size()<4){//mazeyw protaseiw apo most trusted filous
			ComparableFriend temp = MyQueue.poll();
			if ( temp.value > 0.5)
			{			
				TemplateTRM_Sensor current = recommenders.get_followee(temp.id).Sensor;
				if (current!=null)  struct1 = current.get_app(s);
				if (struct1!=null)	my_new_sensor = struct1.request_Sensor(collusion,malicious);
				if (my_new_sensor!=null && my_new_sensor.isActive()){Followee_struct	next =new Followee_struct(my_new_sensor);
				recommendations.add(next);	recommender.add(current);}	
			}else break;
		}
		//Exw tis protaseis zhtaw feedback gia ton kathena kai vriskw reputation apo ton most reputable  ton kanw filo kai zhtaw service 
		double reputation=0.0;
		for (Followee_struct current : recommendations){
			current.reputation = calculate_reputation(current.Sensor.id,s);
			// System.out.println(current.reputation);
			if (current.reputation > reputation) current.reputation = reputation;}
		for (Followee_struct current : recommendations){
			TemplateTRM_Sensor his_recommender = recommender.pollFirst();
			if (current.reputation ==  reputation) 
			{		Application_struct Service = friends.get(s);
			addfollowee(current.Sensor,s,0.0);			//vazw filo kai zhtaw service
			Service.change_reputation(current.Sensor.id,s,current.reputation);
			MyOutcome outcome1=current.requestService(s);
			Service.addNewShare(Integer.toString(current.Sensor.id), outcome1);
			recommenders.addNewShare(Integer.toString(his_recommender.id), outcome1);//kanw evaluate ton arxiko recommender 
			return outcome1;
			}


		}

		return null;
	}
	/**
	 * This method caluclates the reputation of a VE for a Service
	 * @param id the VE 
	 * @param String service the Service 
	 * */
	private double calculate_reputation(int id, String service) {
		//zhtaw feedback apo high reputable kai to vazw se lista mazi me to pios to edwse
		LinkedList<MyTransaction> feedback = new LinkedList<MyTransaction>();
		double m = 0,s=0;
		Application_struct recommenders = friends.get("recommendation");
		PriorityQueue<ComparableFriend> MyQueue=recommenders.rank_friends("trust");
		for(int i=0; i<5 && !MyQueue.isEmpty() ;i++ ){
			ComparableFriend temp = MyQueue.poll();
			double experience=recommenders.get_followee(temp.id).Sensor.ask_experience(Integer.toString(id),service);
			if (experience >=0){
				double weight=temp.value;
				feedback.add(new MyTransaction(new MyOutcome(new SatisfactionInterval(0.0, 1.0, experience)),weight,temp.id));}
		}
		// calculate intial reputation distribution
		double sum = 0.0;// wi*xi
		double weights = 0.0;// wi
		double wx2 = 0.0;// wi*xi^2
		int j=0;

		if (feedback.size()>0){
			for ( j = 0; j < feedback.size(); j++) {
				MyTransaction t = feedback.get(j);
				double weight = t.fading * t.severity;
				double xi = t.getSatisfaction().getSatisfactionValue();
				double temp = weight * xi;
				sum += temp; // �wi*xi
				wx2 += temp * xi;// �wi*xi*xi
				weights += weight;// �wi
			}
			m = sum / weights;
			s = (wx2 * weights - Math.pow(sum, 2)) / Math.pow(weights, 2);
		}
		//next get more feedback
		for(int i=0; i<15 && !MyQueue.isEmpty() ;i++ ){
			ComparableFriend temp = MyQueue.poll();
			double experience=recommenders.get_followee(temp.id).Sensor.ask_experience(Integer.toString(id),service);
			if (experience >=0){
				double weight=temp.value;
				if ((experience < m-0.5*s) || (experience > m+0.5*s)) {
					recommenders.addNewShare(temp.id, (new MyOutcome(new SatisfactionInterval(0.0, 1.0, 0.3))));
				}
				else feedback.add(new MyTransaction(new MyOutcome(new SatisfactionInterval(0.0, 1.0, experience)),weight,temp.id));}
		}
		//	System.out.println(feedback.size());
		if (feedback.size()>=j){ //continue calculating the reputation with the new feedbacks
			for ( ; j < feedback.size(); j++) {
				MyTransaction t = feedback.get(j);
				double weight = t.fading * t.severity;
				double xi = t.getSatisfaction().getSatisfactionValue();
				double temp = weight * xi;
				sum += temp; // �wi*xi
				wx2 += temp * xi;// �wi*xi*xi
				weights += weight;// �wi
			}
			m = sum / (weights+0.000001);
			s = (wx2 * weights - Math.pow(sum, 2)) / Math.pow(weights+0.00001, 2);
			for (int i=0; i<feedback.size();i++){ 			//kane evaluate tous arxikous		
				MyTransaction t = feedback.get(i);
				if ((t.getSatisfaction().getSatisfactionValue() < m-0.5*s) || (t.getSatisfaction().getSatisfactionValue() > m+0.5*s)) {
					recommenders.addNewShare(t.id, (new MyOutcome(new SatisfactionInterval(0.0, 1.0, 0.0))));
				}else 	recommenders.addNewShare(t.id, (new MyOutcome(new SatisfactionInterval(0.0, 1.0, 0.9))));
			}
			////System.out.println("m-"+m);
			return m-s;
		}
		else return -1;
	}
	/** 
	 * This Method returns the experience the VE had with another VE for a service
	 * @param id the sensor
	 * @param service the service
	 * @return the trust level
	 */
	public synchronized double ask_experience(String id, String service) {

		Application_struct f = friends.get(service);
		if (f!=null)	{	
			Followee_struct f1 = f.get_followee(id);
			if (f1!=null){
				if(malicious && collusion){ //if malicous and collusion return reverse experience
					return 1-f1.return_trust();
				}else return f1.return_trust();

			}
		}

		return -0.1; //negative means no experience at all
	}

	public synchronized Application_struct get_app(String S){
		return friends.get(S);


	}

	/**
	 * 
	 * @param s the service
	 * @param i the TTL
	 * @param id id of the requester so that i do not lie to myself when colluding
	 * @return
	 */
	public synchronized Outcome get_assistance(String s, int i,int id) {
		Application_struct recommenders = friends.get("recommendation");
		PriorityQueue<ComparableFriend> MyQueue=recommenders.rank_friends("assists");
		while (!MyQueue.isEmpty() && outcome ==  null){
			ComparableFriend temp = MyQueue.poll();			
			if ( temp.value > 0.5)
			{ 
				if(collusion && (id!=this.id) && malicious){ //collude in order to return outcome of a malicous friend

					Application_struct bad_struct = get_app(s);
					if (bad_struct!=null){
						PriorityQueue<ComparableFriend> BadQueue= bad_struct.rank_friends("trust");
						while (!BadQueue.isEmpty()){
							ComparableFriend bad_temp = BadQueue.poll();
							if (bad_temp.value < 0.3){ //found bad friend
								Followee_struct ff = bad_struct.get_followee(temp.id);
								if (ff!=null){
									TemplateTRM_Sensor current = ff.Sensor;
									Application_struct new_requiredService = current.get_app(s);
									if (new_requiredService!=null){
										outcome = new_requiredService.request_Service();}
								}}
						}
					}


				}else
				{
					TemplateTRM_Sensor current = recommenders.get_followee(temp.id).Sensor;
					Application_struct new_requiredService = current.get_app(s);
					if (new_requiredService!=null){

						outcome = new_requiredService.request_Service();
					}if (i>0 && outcome==null) outcome = current.get_assistance(s, i-1,this.id);
				}

				if (outcome!=null){
					recommenders.addNewAssist(temp.id,(MyOutcome)outcome);}		
			}else break;

		}
		return outcome;
	}
	/**
	 * this method reports to the platform about experiences the malicous collusion is handled at the platform
	 */
	private synchronized void report() {
		Set<String> Services = getRequestedServices();

		for (String s : Services)// gia kathe service pou thelw
		{
			if (s.equals("My service")) {

				Application_struct reportedService = friends.get(s);//pernw struct
				platform.report(reportedService,this.serve(s),collusion);//to stelnw platforma gia report

			}
		}
	}

	/**
	 * Adds a new service to the set of offered services of this sensor
	 * 
	 * @param service
	 *            The new service to be added
	 * @param goodness
	 *            The goodness when offering that new service
	 */
	@Override
	public void addService(Service service, double goodness) {
		servicesGoodness2.put(service.id, new Double(goodness));
		servicesGoodness.put(service, new Double(goodness));

	}
	/**
	 * This methods returns the actual service value
	 * @param service
	 * @return the satisfaction
	 */
	public synchronized double serve(String service) {
		// System.out.println("goodness= "+ servicesGoodness2.get(service));
		/*return Math.round(10 * (Math.random() * (0.2) + 0.8 * servicesGoodness2
				.get(service))) / 10.0;*/
		numRequests++;
		if (numRequests == numRequestsThreshold) { // Edited by Hamed Khiabani for dynamic nodes
			numRequests = 0;
			if (dynamic && runningSimulation) {
				activeState = false;
				numRequestsTimer = new Timer();
				numRequestsTimer.schedule(new TimerTask(){
					@Override
					public void run() {
						activeState = true;
						numRequestsTimer.cancel();
					}
				},sleepingTimeoutMilis);
			}
		}
		if(servicesGoodness2.get(service)!=null && this.isActive()){
			double quality = servicesGoodness2.get(service);
			return Math.round(10 * (quality)) / 10.0;}
		else return -0.1;
	}

	@Override
	public void reset() {
		friends = new HashMap<String, Application_struct>();
	}

	/**
	 * Indicates if there is a collusion or not
	 * 
	 * @return true, if there is a collusion, false otherwise
	 */
	public static boolean collusion() {
		return collusion;
	}

	/**
	 * Returns the service requested by the client
	 * 
	 * @return The service requested by the client
	 */
	public Service get_requiredService() {
		return requiredService;
	}
	@Override
	public void set_goodness(Service service, double goodness) throws Exception {
		if (!offersService(service.id()))
			throw new Exception("Server "+id+" doesn't offer service "+service.id());

		servicesGoodness.put(service, new Double(goodness));
		servicesGoodness2.put(service.id, new Double(goodness));

	}

	/**
	 * Sets the window size for storing transactions outcomes
	 * 
	 * @param windowSize
	 *            New window size for storing transactions outcomes
	 */
	public static void set_windowSize(int windowSize) {
		_windowSize = windowSize;
	}
}