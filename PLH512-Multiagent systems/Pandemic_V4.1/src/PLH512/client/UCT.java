package PLH512.client;

import java.util.Collections;
import java.util.Comparator;

public class UCT {
	public static double uctValue(
		      int totalVisits, double valuation, int nodeVisit) {
		        if (nodeVisit == 0) {
		            return Integer.MAX_VALUE;
		        }
		        return ((double) valuation / (double) nodeVisit) 
		          +  Math.sqrt(2) * Math.sqrt(Math.log(totalVisits) / (double) nodeVisit);
		    }

		    public static ActionNode findBestNodeWithUCT(ActionNode node) {
		        int parentVisit = node.getState().getVisits();
		        return Collections.max(
		          node.getChildArray(),
		          Comparator.comparing(c -> uctValue(parentVisit, 
		            c.getState().getEvaluation(), c.getState().getVisits())));
		    }
}
