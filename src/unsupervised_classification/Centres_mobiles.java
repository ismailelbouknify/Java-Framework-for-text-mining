package unsupervised_classification;

import java.util.ArrayList;
import java.util.HashMap;

public class Centres_mobiles {
	public static HashMap<Integer , ArrayList<String>> K_means(HashMap<String, HashMap<String, Integer>> data,int k) {
		HashMap<Integer, ArrayList<String>> cluster = new HashMap<Integer, ArrayList<String>>();//<class,doc>
		
		cluster=initializer(data,k);
		
		 
		return cluster;
	}

	private static HashMap<Integer, ArrayList<String>> initializer(HashMap<String, HashMap<String, Integer>> data, int k) {
		HashMap<Integer, ArrayList<String>> cluster = new HashMap<Integer, ArrayList<String>>();//<class,doc>
		int n=data.size()/k;
		ArrayList<String> L= new  ArrayList<String>();
		int t1=n,t0=0,j=0;
		for(int i=0;i<k;i++){
			j=0;L.clear();
			for(String doc :data.keySet()) {
				if(j>=t0 && j<t1) {
					L.add(doc);
				}
				j++;
			}
			t0=t1;
			t1+=n;
			cluster.put(i,L);
		}
		return cluster;
	}
}
