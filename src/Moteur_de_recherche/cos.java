package Moteur_de_recherche;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class cos {
   
    public static double cos(HashMap<String,Double> doc1,HashMap<String,Double> doc2) {
    	double numenator=0;
    	double denomenator=0;
    	double doc1_norm=0;
    	double doc2_norm=0;
    	for(String t:doc1.keySet()) {
    	numenator+=doc1.get(t)*doc2.get(t);
    	doc1_norm+=doc1.get(t)*doc1.get(t);
    	doc2_norm+=doc2.get(t)*doc2.get(t);
    	}
    	denomenator= (Math.sqrt(doc1_norm)*Math.sqrt(doc2_norm));
    	return numenator/denomenator;
    	}
    
    public static String get_similaire(HashMap<String, Double> query,HashMap<String, HashMap<String, Double>> docs){
    	String proch="aucun document";
    	double cosproch=0;
    	for (String doc_name : docs.keySet()) {        
    		for( HashMap<String, Double> mapdoc :  docs.values()){
    			double cosi=cos(query,mapdoc);
    			if (cosproch<cosi) {
    				cosproch=cosi;
    				proch=doc_name;
    			}
    		}
    	}
    	
    	return "\n\n la document la plus proche de requet est "+proch;
    }

}
