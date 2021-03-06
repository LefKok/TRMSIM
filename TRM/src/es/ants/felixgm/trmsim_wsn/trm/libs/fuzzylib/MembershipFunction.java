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

package es.ants.felixgm.trmsim_wsn.trm.libs.fuzzylib;

/**
 * <p>This class models a Membership function</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.4
 */
public abstract class MembershipFunction {

    /** Function's parameters */
    protected double parameters[];
    /** Universe max (range max) */
    protected double universeMax;
    /** Universe min (range min) */
    protected double universeMin;
    /** */
    protected Defuzzifier defuzzifier;

    /** Default Constructor */
    public MembershipFunction() {
        universeMin = 0.0;
        universeMax = 1.0;
    }

    /**
     * Get membership function's value.
     * @param input : Variable's 'x' value
     * Note: Output must be within the range [0,1]
     */
    public abstract double membership(double input);

    public double defuzzify() {
        return defuzzifier.defuzzify();
    }

    public void setDefuzzifier(Defuzzifier defuzzifier) {
        this.defuzzifier = defuzzifier;
        int numberOfPoints = FuzzySetWindow.DEFAULT_CHART_NUMBER_OF_POINTS;
        if (Double.isNaN(universeMin))
            universeMin = ((Defuzzifier)defuzzifier).getMin();
        if (Double.isNaN(universeMax))
            universeMax = ((Defuzzifier)defuzzifier).getMax();
        ((Defuzzifier)defuzzifier).init(universeMin,universeMax,numberOfPoints);
        double xx = universeMin;
        double step = (universeMax - universeMin) / ((double) numberOfPoints);
        for( int i = 0; i < numberOfPoints; i++, xx += step )
            ((Defuzzifier)defuzzifier).setValue(i,membership(xx));
    }

    public double getUniverseMax() {
        return universeMax;
    }

    public double getUniverseMin() {
        return universeMin;
    }
}