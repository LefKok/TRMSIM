package es.ants.felixgm.trmsim_wsn.trm.templatetrm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;

import es.ants.felixgm.trmsim_wsn.network.Service;
import es.ants.felixgm.trmsim_wsn.outcomes.Outcome;
/**
 * <p>
 * This class models a A Struct holding everything necessary for one Service
 * </p>
 * 
 * @author Lefteris Kokoris
 */
public class Application_struct {

	HashMap<String, Followee_struct> followees;
	Service service;
	double threshold;

	public Application_struct(String domain, double threshold) {
		service = new Service(domain);
		followees = new HashMap<String, Followee_struct>();//Hashmap Holding followees
		this.threshold = threshold;
	}

	/* add VE to friends List */
	public void followee_put(TemplateTRM_Sensor key, double trust) {
		followees.put(Integer.toString(key.id), new Followee_struct(key,trust));
	}

	/* remove VE from friend List */
	public void followee_remove(String key) {
		followees.remove(key);
	}

	public TemplateTRM_Sensor get_random_entry() { 
		ArrayList<String> keysAsArray = new ArrayList<String>(
				followees.keySet());
		Random r = new Random();
		return get_followee(keysAsArray.get(r.nextInt(keysAsArray.size()))).Sensor;
	}

	/**
	 * This method request service from the most trusted followee
	 */
	public synchronized Outcome request_Service() {
		MyOutcome outcome = null;
		ComparableFriend friend = rank_friends2();//get most trusted VE
		if (friend.value > threshold) {// then trust him
			outcome = get_followee(friend.id).requestService(service.id);
			if (outcome!=null)
				addNewShare(friend.id, outcome);
			//System.out.println(outcome.get_satisfaction().toString());
			return outcome;
		}
		return null;
	};
	/**
	 * This method returns a followee as recommendation to someone else
	 */
	public synchronized TemplateTRM_Sensor request_Sensor(boolean collusion, boolean malicious) {
		TemplateTRM_Sensor sensor = null;
		ComparableFriend friend = rank_friends2();
		if (friend.value > threshold) {
			sensor = get_followee(friend.id).Sensor;
		}
		if (malicious && collusion) {
			friend = rank_friends_reverse();
			if (friend.value<0.5)
				sensor = get_followee(friend.id).Sensor;
		}
		return sensor;
	};

	synchronized ComparableFriend  rank_friends_reverse() {// compute trust for all followees
		// and return lowest trustee
		ComparableFriend selected = new ComparableFriend("no", 1.1);
		ArrayList<String> keysAsArray = new ArrayList<String>(
				followees.keySet());
		for (String id : keysAsArray) {
			if (get_followee(id).Sensor.isActive()){
				double ttrust = get_followee(id).calculate_Trust();
				if (ttrust < selected.value) {
					selected.id = id;
					selected.value = ttrust;
				}
			}}
		return selected;
	}

	public synchronized Followee_struct get_followee(String id){	
		return followees.get(id);
	}

	/**
	 * This method returns all frinds sorted by trust descending
	 * 
	 * @param list caclulate trust from shares or from assits(rec_trust)
	 */
	public synchronized PriorityQueue<ComparableFriend> rank_friends(String list) {
		PriorityQueue<ComparableFriend> evaluationQeue;

		Comparator<ComparableFriend> comparator = new FriendComparator();
		evaluationQeue = new PriorityQueue<ComparableFriend>(1,
				comparator);
		ArrayList<String> keysAsArray = new ArrayList<String>(
				followees.keySet());
		if (list.equals("trust")){
			for (String id : keysAsArray){
				if (get_followee(id).Sensor.isActive()){

					evaluationQeue.add(new ComparableFriend(id, get_followee(id).calculate_Trust()));}}
		}else{

			for (String id : keysAsArray){
				if (get_followee(id).Sensor.isActive()){
					evaluationQeue.add(new ComparableFriend(id, get_followee(id)
							.calculate_rec_Trust()));}}

		}

		return evaluationQeue;
	}

	/**
	 * This method returns the most trusted friend only
	 * 
	 * @param list caclulate trust from shares or from assits(rec_trust)
	 */

	public synchronized ComparableFriend rank_friends2() {// compute trust for all followees
		// and return higher trustee
		ComparableFriend selected = new ComparableFriend("no", -0.1);

		ArrayList<String> keysAsArray = new ArrayList<String>(
				followees.keySet());
		for (String id : keysAsArray) {
			if (get_followee(id).Sensor.isActive()){

				double ttrust = get_followee(id).calculate_Trust();
				if (ttrust > selected.value) {
					selected.id = id;
					selected.value = ttrust;
				}
			}}
		return selected;
	}

	/**
	 * This method adds a new Share to the collection of shares of this sensor
	 * 
	 * @param id
	 *            who provided the service
	 * @param outcome
	 *            Outcome of the transaction to be added
	 */

	public synchronized void addNewShare(String id, MyOutcome outcome) { // TODO

		Followee_struct f = get_followee(id);
		if (f!=null)//mporei na edwsa service se emena apo recommendation
			f.shares.addFirst(new MyTransaction(outcome));
	}

	/**
	 * This method adds a new Assist to the collection of assists of this sensor
	 * 
	 * @param followee
	 *            who assisted the service
	 * @param outcome
	 *            Outcome of the transaction to be added
	 */
	public synchronized void addNewAssist(String id,
			MyOutcome outcome) {
		get_followee(id).assists.addFirst(new MyTransaction(outcome));
	}

	public void change_reputation(int id, String s, double d) {
		Followee_struct f = followees.get(Integer.toString(id));
		if (f!=null)
			f.reputation=d;

	}
}

