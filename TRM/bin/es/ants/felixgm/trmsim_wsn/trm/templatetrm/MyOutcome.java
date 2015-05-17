/**
 * 
 */
package es.ants.felixgm.trmsim_wsn.trm.templatetrm;


import es.ants.felixgm.trmsim_wsn.outcomes.BasicOutcome;
import es.ants.felixgm.trmsim_wsn.satisfaction.*;


/**
 * @author Lefteris
 *
 */
public class MyOutcome extends BasicOutcome {

    protected SatisfactionDiscreteScale satisfaction;


	/**
	 * @param satisfaction
	 */
	public MyOutcome(SatisfactionInterval satisfaction) {
		super(satisfaction,satisfaction.getSatisfactionValue(),1);

	}



}
