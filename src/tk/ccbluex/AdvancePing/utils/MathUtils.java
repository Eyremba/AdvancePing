/**
 * 
 */
package tk.ccbluex.AdvancePing.utils;

import java.util.List;

/**
 * @author Marco
 *
 */
public class MathUtils {

	public static int AverageCalculate(List<Integer> values) {
		int i = 0;
		for(Integer value : values)
			i += value;
		i /= values.size();
		return i;
	}
}