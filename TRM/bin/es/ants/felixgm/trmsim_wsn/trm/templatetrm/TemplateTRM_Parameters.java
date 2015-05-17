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

import es.ants.felixgm.trmsim_wsn.trm.TRMParameters;

/**
 * <p>This class represents the set of parameters' values of {@link TemplateTRM}.</p>
 * <p>A TemplateTRM parameters file has the following structure:</p>
 * <pre>
 *    ####################################
 *    # TemplateTRM parameters file
 *    ####################################
 *    threshold_max=0.8
 *    threshold_min=0.4
 * </pre>
 */
public class TemplateTRM_Parameters extends TRMParameters {
    /** Default parameters file name */
    public static final String defaultParametersFileName = "trmodels/templatetrm/TemplateTRMparameters.txt";
   /* trust threshold for services with minor severity*/
    private double threshold_min; 
    /* trust threshold for services with major severity*/
    private double threshold_max;


    /**
     * Creates a new instance of TemplateTRM_Parameters setting them to their default values
     */
    public TemplateTRM_Parameters() {
        super();
        parametersFileHeader = "####################################\n";
        parametersFileHeader += "# PeerTrust parameters file\n";
        parametersFileHeader += "# "+(new java.util.Date())+"\n";
        parametersFileHeader += "####################################\n";
        threshold_max=0.8;
        threshold_min=0.4;
    }

    /**
     * Creates a new instance of TemplateTRM_Parameters from a given parameters file name
     * @param fileName TemplateTRM parameters file name
     * @throws java.lang.Exception If any parameter can not be successfully retrieved
     */
    public TemplateTRM_Parameters(String fileName) throws Exception {
        super(fileName);
        parametersFileHeader = "####################################\n";
        parametersFileHeader += "# PeerTrust parameters file\n";
        parametersFileHeader += "# "+(new java.util.Date())+"\n";
        parametersFileHeader += "####################################\n";
        threshold_max=getDoubleParameter("threshold_max");
        threshold_min=getDoubleParameter("threshold_min");
    }

    @Override
	public String toString() {
		return "TemplateTRM_Parameters [threshold_min=" + threshold_min
				+ ", threshold_max=" + threshold_max + "]";
	}

	public double getThreshold_min() {
		return threshold_min;
	}

	public void setThreshold_min(double threshold_min) {
		this.threshold_min = threshold_min;
	}

	public double getThreshold_max() {
		return threshold_max;
	}

	public void setThreshold_max(double threshold_max) {
		this.threshold_max = threshold_max;
	}

	public static String getDefaultparametersfilename() {
		return defaultParametersFileName;
	}
}