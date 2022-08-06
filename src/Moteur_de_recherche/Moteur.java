package Moteur_de_recherche;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import TF_IDF.TF_IDF_Map;
public class Moteur {
	public static ArrayList<String> get_arryrequet(String[] r) {
		ArrayList<String> requet = new ArrayList<String>();
		for(String term : r) {
			requet.add(term);
		}
		return requet;
	}
	
	public static void main(String args[] ) throws FileNotFoundException {
	
		// tous les document 
		String chemin="docs";
		HashMap<String, HashMap<String, Double>>  mapGlobale= new HashMap<>();
    	HashMap<String, HashMap<String, Integer>> map2= new HashMap<>();
    	ArrayList<ArrayList<String>> listes = new ArrayList<ArrayList<String>>();
    	
    	// document de requet 
    	//String chemin_requet ="C:\\a\\requet";
    	HashMap<String, Double> requet_map= new HashMap<>();
    	ArrayList<String> requet = new ArrayList<String>();
		requet.add("رسم");
    	
    	requet_map=TF_IDF_Map.getmaprequet(requet);
    	//System.out.println(requet_map);
      	
    	listes=TF_IDF_Map.getListeDucument(chemin);
    	String[] doc_name =TF_IDF_Map.getDocumentName(chemin);
		map2=TF_IDF_Map.getmap2(listes,doc_name);   // map2:  <doc1,map1>,.....
		mapGlobale=TF_IDF_Map.getmapfinale(map2);   // map globale avec les TF_IDF
    	System.out.println(mapGlobale);	
		
		// calcule cos entre chaque document et la requet et retourner la document la plus proche 
		String result=cos.get_similaire(requet_map, mapGlobale);	
		System.out.println(result);	  
}
}
