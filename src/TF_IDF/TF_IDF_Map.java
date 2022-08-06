package TF_IDF;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class TF_IDF_Map {
//////////////////////function return Liste document     
	public static ArrayList<ArrayList<String>> getListeDucument(String chemin) throws FileNotFoundException {
		
		File[] listFiles = new File(chemin).listFiles();
		ArrayList<ArrayList<String>> listes = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < listFiles.length; i++) {
			@SuppressWarnings("resource")
			Scanner docum = new Scanner(listFiles[i]);
			ArrayList<String> liste=new ArrayList<String>();
			while (docum.hasNext()) {			
				String word  = docum.next().trim();
				liste.add(word);
			}
			listes.add(liste);
		}		
		return listes;
	}
/////////////////: calculer TF
	public static double tf(Map<String, Integer> doc, String term) { 
	    	int size=0;
	    	double result=0;
	    	for (String key : doc.keySet()) {
	    		if(!doc.containsKey(term)) return 0;
	    		result = doc.get(key);
	    		size+=doc.get(key);
			}
	    	return result / size;
	    }
	   
/////////////////: calculer IDF	
	   public static double idf(HashMap<String, HashMap<String, Integer>> docs, String term) {
	        double n = 0;
	        for ( Map.Entry<String, HashMap<String, Integer>> doc : docs.entrySet()) {
	            for (String word : doc.getValue().keySet()) {
	                if (term.equalsIgnoreCase(word)) {
	                    n++;  
	               }
	            }
	        }
	        return Math.log10(docs.size() / n);
	    }
	   
/////////////////: calculer TF_IDF	   
	   public static double tfIdf(HashMap<String, HashMap<String, Integer>> docs, HashMap<String, Integer> doc, String term) {
	        return tf(doc, term) * idf(docs, term);

	    }
	   
///////////////////////////////map1: <<term1,ocu1><term2,ocu2>........>
	   
    public static HashMap<String, Integer> getmap1(List<String> list){
    	HashMap<String, Integer> frequencyMap = new HashMap<>();
        for (String s: list)
        {
            Integer count = frequencyMap.get(s);
            if (count == null) {
                count = 0;
            }
 
            frequencyMap.put(s, count + 1);
        }
        //System.out.println(frequencyMap);
    	return frequencyMap;
    }
    
/////////////////////////:////////// map2:  <doc1,map1>,<doc2,map2>.....
    public static HashMap<String, HashMap<String, Integer>> getmap2(ArrayList<ArrayList<String>> listes,String doc_name []){
    	HashMap<String, HashMap<String, Integer>> map2= new HashMap<>(); 
    	
    	for(int i=0;i< doc_name.length;i++) {
    		HashMap<String, Integer> map1 = new HashMap<>();
    		map1=getmap1(listes.get(i));
    		 String  nom = doc_name[i];
    		 map2.put(nom, map1);
    	}    	
    	return map2;
    }
    
    ////////////Map Globale <<d1,map1><d2,map2>.................> avec map1 : <<term1,tfidf1><term2,tfidf2>......>
    
    public static HashMap<String, HashMap<String, Double>> getmapfinale(HashMap<String, HashMap<String, Integer>> map2){
    	HashMap<String, Double> mapf= new HashMap<>();
    	HashMap<String, HashMap<String, Double>> mapGlobale= new HashMap<>();
    	
    	double tfidf=0;
    	List<String> d_name = new LinkedList<String>();  ////Nom des document 
    	for (String name: map2.keySet()) {
    		d_name.add(name);
    	}	
    	
    	int i=0;
    	for( HashMap<String, Integer> mapi :  map2.values()){
    		for(String term1 : mapi.keySet()) {
    			  tfidf=tfIdf(map2, mapi,  term1);
    			  mapf.put(term1, tfidf);
    		}
    		
    		mapGlobale.put(d_name.get(i), mapf);
    		i++;
    	}
    	return mapGlobale;
   
    }

////////////////////////// les nom des  documents 
    public static String [] getDocumentName(String chemin) {
    	File[] listFiles = new File(chemin).listFiles();
    	String doc_name []=new String[listFiles.length];
    	for (int i = 0; i < listFiles.length; i++) {
			doc_name[i] = listFiles[i].getName().replace(".txt", "");
			
		}
    	return doc_name;
    }
    
////// get map de la requet 
    
    public static HashMap<String, Double> getmaprequet(List<String> list){
    	HashMap<String, Double> Map = new HashMap<>();
        for (String s: list)
        {
            Double count = Map.get(s);
            if (count == null) {
                count = (double) 0;
            }
 
            Map.put(s, count + 1/list.size());
        }
        //System.out.println(Map);
    	return Map;
    }
    
//////// get text de la  requet
public static ArrayList<String> getrequet(String chemin) throws FileNotFoundException {
		
		File list = new File(chemin);
		@SuppressWarnings("resource")
		Scanner docum = new Scanner(list);System.out.println("bb");
		ArrayList<String> liste=new ArrayList<String>();
		while (docum.hasNext()) {			
				String word  = docum.next().trim();
				liste.add(word);
			}
				
		return liste;
}

/////// Main Création maps
public static void main(String args[] ) throws FileNotFoundException {
	String chemin="docs";
	HashMap<String, HashMap<String, Double>>  mapGlobale= new HashMap<>();
	HashMap<String, HashMap<String, Integer>> map2= new HashMap<>(); 
	ArrayList<ArrayList<String>> listes = new ArrayList<ArrayList<String>>();
	listes=TF_IDF_Map.getListeDucument(chemin);
	String[] doc_name =TF_IDF_Map.getDocumentName(chemin);
	map2=TF_IDF_Map.getmap2(listes,doc_name);   // map2:  <doc1,map1>,.....
	mapGlobale=TF_IDF_Map.getmapfinale(map2);   // map globale avec les TF_IDF
	System.out.println(mapGlobale);	
}   	   
}	

