package metric;

import java.util.HashMap;
import java.lang.Math;
public class metric {
	
	 public static double getoccut(String doc, HashMap<String, String> mapsClass) {
			double occ=0;
			for (String name : mapsClass.keySet()) {
				if (mapsClass.get(name)== doc) {
					occ+=1;
				}
			}
			return occ+1;
		}
	
	public static HashMap<String, Double> getListeclasse(HashMap<String, String> mapsClass) { // mapsclass: <doc,class> return maps <class,size>
		HashMap<String, Double> mapclass_size = new HashMap<String, Double>();
		for (String doc : mapsClass.keySet()) {
			if (!mapclass_size.containsKey(mapsClass.get(doc))) {
				double occ=getoccut(mapsClass.get(doc),mapsClass); //get <class,occ> (nom_class,maps<doc,class>)
				mapclass_size.put(mapsClass.get(doc),occ );
			}
		}
		return mapclass_size;// maps <class,size>
	}
	
	public static String getClasse_name(String name) {
		 String s=name.substring(4);
		 return s;
	   }
	 	 
	 public static double  precision(HashMap<String, String> ypred,HashMap<String, String> yr){ // <doc,class1>...
		 double score=0;
		 HashMap<String, Double> mapsclassPred= new HashMap<String, Double>();
		 mapsclassPred=getListeclasse(ypred); // maps <classe1 ,size1>...
		 for (String class_name : mapsclassPred.keySet()) { // pour chaque classe 
			 int score1=0;
			 for (String doc : ypred.keySet()) {
				if(yr.get(doc)==ypred.get(doc)) score1++; // si bien pred la class score++
			 }
			 score+=score1/mapsclassPred.get(class_name); // score/taille de classe
		 }       
	     return score/mapsclassPred.size();
	    }
	    public static double  recall(HashMap<String, String> ypred,HashMap<String, String> yr){
	   	 double score=0;
		 HashMap<String, Double> mapsclass= new HashMap<String, Double>();
		 mapsclass=getListeclasse(yr); // maps <classe1 ,size1>...
		 for (String class_name : mapsclass.keySet()) { // pour chaque classe 
			 int score1=0;
			 for (String doc : ypred.keySet()) {
				if(yr.get(doc)==ypred.get(doc)) score1++; // si bien pred la class score++
			 }
			 score+=score1/mapsclass.get(class_name); // score/taille de classe
		 	}       
	    return score/mapsclass.size();
	    }

	    public static double  f1mesure(HashMap<String, String> ypred,HashMap<String, String> yr){
	        return  100*((2*precision(ypred,yr)*recall(ypred,yr))/(precision(ypred,yr)+recall(ypred,yr))) ;
	        //return  Math.random()*100;
	    }
}
