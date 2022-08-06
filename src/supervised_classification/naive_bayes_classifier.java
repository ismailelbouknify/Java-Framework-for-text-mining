package supervised_classification;

import java.util.HashMap;
import java.util.HashSet;
import metric.metric;
public class naive_bayes_classifier { 

	public static HashMap<String, HashMap<String, Double>> proba_mots = new  HashMap<String, HashMap<String, Double>>();//<mot , <class,proba>>>
	public static HashMap<String, Double> proba_classe = new HashMap<String, Double>(); // <class,proba>
	static String[] Tr= {"business","entertainment","politics","sport","tech"};
	public naive_bayes_classifier() {
		
	}
    public static void Naive_train(HashMap<String, HashMap<String, Integer>> x_train) { //remplir proba_mots proba_classe
    	int size=x_train.size();
    	double proba=0,prob=0;
    	int n;
    	HashMap<String, Double> map= new HashMap<String, Double>();//class et proba d'un term
    	HashMap<String, Integer> terms;
    	// remplire proba_classe
    	for(String doc :x_train.keySet()) {
    		if(!proba_classe.containsKey(metric.getClasse_name(doc))){
    			n=getnombredoc_class(x_train,metric.getClasse_name(doc));
    			proba_classe.put(metric.getClasse_name(doc), proba/size);
    		}
    		//remplir proba_mots
    		
    		terms = x_train.get(doc);
    		for( String term :terms.keySet()) {
    			if(!proba_mots.containsKey(term)){
    				prob=get_prob(x_train,metric.getClasse_name(doc),term); // proba d'un terme dans une class
    				map.put(metric.getClasse_name(doc), prob);//class , proba d'un term
    			}
				proba_mots.put(term, map);
    		}
    	}
    }

	// proba que term appartient a la class 
	private static double get_prob(HashMap<String, HashMap<String, Integer>> x_train, String classe_name, String term) {
		double proba=0;
		int nk=0,n=0,m=x_train.size();
		HashMap<String, Integer> terms;
		for(String doc : x_train.keySet()) {
			if(metric.getClasse_name(doc)==classe_name) {
				terms = x_train.get(doc);
				for(String ter :terms.keySet() ) {
					if(ter==term) {
						nk++;
					}
					n++;
				}
			}
		}
		return (nk+1)/(n+m);
	}
	private static int getnombredoc_class(HashMap<String, HashMap<String, Integer>> x_train,String classe_name) {
		int n=0;
		for(String doc :x_train.keySet()) {
			if(metric.getClasse_name(doc)==classe_name) {
				n++;
			}
		}
		return n;
	}
	public static HashMap<String,String> Naive_pred(HashMap<String,HashMap<String,Integer>> x_train,HashMap<String,HashMap<String,Integer>> x_test) {
		HashMap<String,String> Ypred = new HashMap<String,String>();
		Naive_train(x_train);
		double pc=1;
		double[] T  =new double[5];
		//System.out.println(proba_classe);
		int i=0;
		for (String doc:x_test.keySet()) {
			i=0;
			for (String cl :proba_classe.keySet()) {
				pc=proba_classe.get(cl);
				for(String term :x_test.get(doc).keySet()) {
					if(proba_mots.get(term).get(cl)!=null) {
					pc=pc*proba_mots.get(term).get(cl);
					}
				}
				T[i]=pc;
				i++;
			}
			int max=getmax(T);
			Ypred.put(doc, Tr[max]);
		}
		return Ypred;
	}
	private static int getmax(double[] t) { //return l'indice de proba max
		double max=t[0];
		int j=0;
		for(int i=1;i<t.length;i++) {
			if (t[i]>max) {
				max=t[i];
				j=i;
			}
		}
		return j;
	}
	
}
