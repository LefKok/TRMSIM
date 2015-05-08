/**
 * 
 */
package es.ants.felixgm.trmsim_wsn.trm.templatetrm;

import java.util.Collection;

import es.ants.felixgm.trmsim_wsn.outcomes.Outcome;
import es.ants.felixgm.trmsim_wsn.satisfaction.Satisfaction;

/**
 * @author Lefteris
 *
 */
public class MyOutcome extends Outcome {

	

	/**
	 * @param satisfaction
	 */
	public MyOutcome(Satisfaction satisfaction,double severity) {
		super(satisfaction);

		// TODO Auto-generated constructor stub
	}


	@Override
	public Outcome aggregate(Collection<Outcome> outcomes) {
		// TODO Auto-generated method stub
		return null;
	}

}
