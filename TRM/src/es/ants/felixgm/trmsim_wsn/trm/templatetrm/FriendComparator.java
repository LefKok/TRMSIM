package es.ants.felixgm.trmsim_wsn.trm.templatetrm;

import java.util.Comparator;


public class FriendComparator implements Comparator<ComparableFriend>{

	@Override
	public int compare(ComparableFriend o1, ComparableFriend o2) {
		
		if (o1.value >=  o2.value)
			return 1;
		else 
			return -1;
	}



}
