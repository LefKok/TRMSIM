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

import es.ants.felixgm.trmsim_wsn.network.Network;
import es.ants.felixgm.trmsim_wsn.network.Sensor;
import es.ants.felixgm.trmsim_wsn.network.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * <p>This class models a network composed by sensors implementing TemplateTRM</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.3
 * @since 0.3
 */
public class TemplateTRM_Network extends Network {
	
	
    protected HashMap<String,Application_struct> Service_VEs; //

	
	
    /**
     * This constructor creates a new random TemplateTRM Network using the given parameters
     * @param numSensors Network number sensors
     * @param probClients The network will have a number of clients depending of this parameter.
     * @param rangeFactor Range Factor.
     * @param probServices Probability of servers offers services and clients requesting services.
     * @param probGoodness Probability of servers being good.
     * @param services Services that servers offers to clients.
     */
    public TemplateTRM_Network(
            int numSensors,
            double probClients,
            double rangeFactor,
            Collection<Double> probServices,
            Collection<Double> probGoodness,
            Collection<Service> services) {
    	super(null,null,null);
    	Service_VEs=new HashMap<String,Application_struct>();
    	clients = new ArrayList<Sensor>();
        servers = new ArrayList<Sensor>();
        sensors = new ArrayList<Sensor>();
        this.services = new ArrayList<Service>();//ola ta services tou systhmatos
       /* services.add(new Service("vehicle"));
        probServices.add(0.7);
        probGoodness.add(0.7);*/
        for (Service service : services)					
        	Service_VEs.put(service.id,new Application_struct(service.id)); //create Platforms log file




        Sensor.resetId();
        Sensor.setMaxDistance(maxDistance);

        for (int i = 0; i < numSensors; i++) {
            if (Math.random() <= probClients) { //client intialize
                TemplateTRM_Sensor client = newSensor();
                clients.add(client);
                sensors.add(client);
                Iterator<Double> itProbServices = probServices.iterator(); 
                for (Service service : services)					//add requested services
                    if (Math.random() <= itProbServices.next().doubleValue()) {
                    	client.addNewService(service.id);
                    }
                	
            } else { 							//server intialize
            	TemplateTRM_Sensor server = newSensor();
                clients.add(server);
                servers.add(server);
                sensors.add(server);

                Iterator<Double> itProbServices1 = probServices.iterator();
                Iterator<Double> itProbServices2 = probServices.iterator(); 
                Iterator<Double> itProbGoodness = probGoodness.iterator();
                
                for (Service service : services) //add requested services
                    if (Math.random() <= itProbServices1.next().doubleValue()) {
                    	server.addNewService(service.id);
                    }
                
                for (Service service : services) //add provided services
                    if (Math.random() <= itProbServices2.next().doubleValue()) { 
                    	Service_VEs.get(service.id).followee_put(server); //network has every sensor classfied by provided service
                    	if (Math.random() <= itProbGoodness.next().doubleValue())//check quality of service
                            if(Math.random() <= 0.5 )
                            	server.addService(service, Math.random()*(0.25)+0.5);  //neutral
                            else server.addService(service, Math.random()*(0.25)+0.75); ///vendor
                        else
                            server.addService(service, Math.random()*0.5); //malicious

                        if (!this.services.contains(service))
                            this.services.add(service);
                    }
            }
        }
        
        if (clients.size() == 0) {
        	TemplateTRM_Sensor client = newSensor();
            clients.add(client);
            sensors.add(client);
            Iterator<Double> itProbServices = probServices.iterator(); 
            for (Service service : services)					//add requested services
                if (Math.random() <= itProbServices.next().doubleValue()) {
                	client.addNewService(service.id);
                }
        }

        // We are going to add some friends to the sensors one for each service it requests
        for (Sensor client : clients){
        	TemplateTRM_Sensor myclient = (TemplateTRM_Sensor) client;
        	Set<String> ServiceSet = myclient.getRequestedServices();
        	for (String service : ServiceSet){
        	//	System.out.println(Service_VEs.get(service.id)+service.toString());
        		TemplateTRM_Sensor followee = Service_VEs.get(service).get_random_entry();
        		client.addLink(followee);
        		myclient.addfollowee(followee,service);      		
        	}      	
        }
    	
    	
        reset();
        
        
        
    }

    /**
     * This method loads a network from a XML file and creates the specific
     * corresponding TemplateTRM Network
     * @param xmlFilePath Path of the XML to load the network from
     * @throws java.lang.Exception If the XML file given does not have the appropriate structure, or if
     * a sensor links to an undefined sensor, or if a sensor links to itself
     */
    public TemplateTRM_Network(String xmlFilePath) throws Exception {
        super(xmlFilePath);
        reset();
    }

    @Override
    public TemplateTRM_Sensor newSensor(){
        return new TemplateTRM_Sensor();
    }

    @Override
    public Sensor newSensor(int id, double x, double y) {
        return new TemplateTRM_Sensor(id,x,y);
    }
}