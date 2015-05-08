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
import java.util.Set;
import java.util.Vector;

import es.ants.felixgm.trmsim_wsn.network.Sensor;
import es.ants.felixgm.trmsim_wsn.network.Service;
import es.ants.felixgm.trmsim_wsn.trm.GatheredInformation;

/**
 * <p>This class models a Sensor implementing TemplateTRM</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.3
 * @since 0.3
 */
public class TemplateTRM_Sensor extends Sensor {

    /**
     * This constructor creates a new Sensor implementing TemplateTRM
     * 
     */
    protected HashMap<String,Application_struct> friends; //vazw friends ana application epishs xrismiopoiountai kai gia assits/recoms gia alla appss
    protected static int _windowSize; /* small windows for detection */
    public TemplateTRM_Sensor () {
        super();   
        friends=new HashMap<String,Application_struct>();

    }

    /**
     * This constructor creates a new Sensor implementing TemplateTRM
     * @param id Identifier of the new sensor
     * @param x X coordinate of the new sensor
     * @param y Y coordinate of the new sensor
     */
    public TemplateTRM_Sensor(int id, double x, double y) {
        super(id,x,y);
        friends=new HashMap<String,Application_struct>();
        
    }
    

    
  
    /**
     * This method adds a new requestedService to the collection of shares of this sensor
     * @param name the id of the service
     *      */
   
    public synchronized void addNewService( String name){
    	
    	friends.put(name,new Application_struct(name));
    }
    
   public synchronized void addfollowee(TemplateTRM_Sensor followee, String service){
	   //System.out.println(followee.toString());
	   friends.get(service).followee_put(followee);
    }
    
    public synchronized Set<String> getRequestedServices(){
		
    	return friends.keySet();
    }
    /**
     * This method adds a new Share to the collection of shares of this sensor
     * @param followee who provided the service
     * @param outcome Outcome of the transaction to be added
     */
   
    public synchronized void addNewShare(TemplateTRM_Sensor followee, MyOutcome outcome){ //TODO
    	
    	 friends.get(requiredService.id).followees.get(followee).shares.add(new Transaction(outcome));
    }
    /**
     * This method adds a new Assist to the collection of assists of this sensor
     * @param followee  who assisted the service
     * @param outcome Outcome of the transaction to be added
     */
    public synchronized void addNewAssist(TemplateTRM_Sensor followee, MyOutcome outcome){//TODO
    	
   	 friends.get(requiredService.id).followees.get(followee).assists.add(new Transaction(outcome));
   }
     @Override
    public void run(){
    	
    	 
    	 if (reachesQualifiedService(requiredService)) {
             GatheredInformation gi = trmmodelWSN.gatherInformation(this, requiredService);
             Vector<Sensor> path = trmmodelWSN.scoreAndRanking(this,gi);
             outcome = trmmodelWSN.performTransaction(path,requiredService);
             if (outcome != null) {
                 if (outcome.get_satisfaction().isSatisfied())
                     outcome = trmmodelWSN.reward(path,outcome);
                 else
                     outcome = trmmodelWSN.punish(path,outcome);
             }
         } else
             outcome = null;
    	
    }
    @Override
    public void reset() {
        friends=new HashMap<String,Application_struct>();
    }

    /**
     * Indicates if there is a collusion or not
     * @return true, if there is a collusion, false otherwise
     */
    public static boolean collusion() { return collusion; }

    /**
     * Returns the service requested by the client
     * @return The service requested by the client
     */
    public Service get_requiredService() { return requiredService; }

    /**
     * Sets the window size for storing transactions outcomes
     * @param windowSize New window size for storing transactions outcomes
     */
    public static void set_windowSize(int windowSize) { _windowSize = windowSize; }
}