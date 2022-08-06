package supervised_classification;

import java.util.HashMap;

import metric.metric;

public class KNN {
	public static HashMap<String,HashMap<String,Double>> map; // map<doctr,<doctest,sim>...
	
	public static HashMap<String,HashMap<String,Double>> Knn_train(HashMap<String, HashMap<String, Integer>> x_train,HashMap<String, HashMap<String, Integer>> x_test){	
		HashMap<String,HashMap<String,Double>> m2 = new HashMap<String,HashMap<String,Double>>();
		HashMap<String, Double> m1 = new HashMap<String, Double>();
		for(String ts :x_test.keySet()) {
			double sim=0;
			m1.clear();
			for(String tr : x_train.keySet()) {
				sim=(double) getDis(x_train.get(ts),x_test.get(tr));
				m1.put(tr,sim);
			}
		m2.put(ts,m1);	
		}
		return m2;
	}

	private static Double getDis(HashMap<String, Integer> Map, HashMap<String, Integer> Map2) {
		double sim=0;
		for (String m : Map.keySet()){
			if(Map2.containsKey(m)) {
				sim+=Map.get(m)*Map2.get(m);	
			}
		}
		return sim;
	}

	public static HashMap<String, String> Knn_prediction(HashMap<String, HashMap<String, Integer>> x_train,HashMap<String, HashMap<String, Integer>> x_test, int k){
		HashMap<String,String> Ypred =new HashMap<String,String>();
		map=Knn_train(x_train,x_test); // test <tr,sim>
		for(String m : map.keySet()) {
			String clas=getclassK(map.get(m),k); // return class maj
			Ypred.put(m, clas);
		}
		return Ypred;
	}

	private static String getclassK( HashMap<String, Double> Map,int k) {//class des k plus proch
		String c = null;
		String max=null;
		double a=0.0;
		HashMap<String,Double> ma =new HashMap<String,Double>(); // class et distance
		for(int i=0;i<k;i++) {
			a=0.0;
			for(String m : Map.keySet()) {
				if(Map.get(m)>a ){
					if(ma.containsKey(m)) {
						ma.replace(m,ma.get(m)+Map.get(m)); //val+new val
					}
					else {
						a=Map.get(m);
						max=m;
					}
					
				}
			}
			ma.put(metric.getClasse_name(max), a);
		}
		a=0.0;
		for (String m : ma.keySet()) {
			if(ma.get(m)>a ) {
				c=m;
				a=ma.get(m);
			}
		}
		return c;
	}
}
