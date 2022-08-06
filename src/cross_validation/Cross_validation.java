package cross_validation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.File;
import TF_IDF.TF_IDF_Map;
import metric.metric;
import supervised_classification.KNN;
import supervised_classification.naive_bayes_classifier;
import unsupervised_classification.Centres_mobiles;
public class Cross_validation {
	
	
	public static void Labdata() { /// renemer les fichier par name_class pour chaque dossier
		String data="bbc";
		try {
			Scanner input=new Scanner(System.in);
			Stream<Path> paths = Files.list(Paths.get(data));
			ArrayList<Path> Folders = (ArrayList<Path>) paths.collect(Collectors.toList());
			for(int j=0;j<Folders.size();j++) {
				Stream<Path> docs_p = Files.walk(Folders.get(j));
				System.out.println(docs_p.toString());
				ArrayList<Path> docs = (ArrayList<Path>) docs_p.collect(Collectors.toList());
				for(int i=1;i<docs.size();i++) {
					System.out.println(docs.get(i).toString());
					File file = new File(docs.get(i).toString());
					File rename = new File(docs.get(i).toString().replaceAll(".txt", "")+"_"+Folders.get(j).getFileName().toString()+".txt");
					boolean flag = file.renameTo(rename);
					if (flag == true) {
						System.out.println("File Successfully Rename");
					}
					else {
						System.out.println("Operation Failed");
					}
				}
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		   System.out.println("Opération terminée!");
	}
		
	public static double moyenne(double [] score) {
		double sum=0;
		for (int i =0;i<score.length;i++) {
			sum+=score[i];
		}
		return sum/score.length;
	}
	
	public static HashMap<Integer ,HashMap<String, HashMap<String, Integer>>> split(HashMap<String,HashMap<String,Integer>> mapOcc,int n){
		HashMap<Integer ,HashMap<String, HashMap<String, Integer>>> mapVal= new HashMap<Integer ,HashMap<String, HashMap<String, Integer>>>();
		HashMap<String, HashMap<String, Integer>> maptm= new HashMap<String, HashMap<String, Integer>>();
		
		int t1=mapOcc.size()%n;
		int t0=0;
		int j=0;
		for (int i=1;i<=n;i++) {
			for(String key : mapOcc.keySet()) {
				if(j>t0 && j<=t1) {
					maptm.put(key,mapOcc.get(key));
				}
				j++;
			}
			t0=t1;
			t1+=t1;
			mapVal.put(i,maptm);
		}
		return mapVal;
	}
	
    public static HashMap<String,String> getMapClass(HashMap<String,HashMap<String,Integer>> mapOcc) {
    	HashMap<String,String> mapClass =new HashMap<String,String>();//<doc,class) d'apré leur name;
    	for (String name : mapOcc.keySet()) {
    		String nam=metric.getClasse_name(name);
    		mapClass.put(name, nam);
    	}
    	return mapClass;
    }
    
    
	public static double validationKnn(HashMap<String, HashMap<String, Integer>> mapOcc,int n) {
		double[] score = new double [n];
		HashMap<Integer ,HashMap<String, HashMap<String, Integer>>> mapVal = new HashMap<Integer ,HashMap<String, HashMap<String, Integer>>>();
		HashMap<String,String> Ypred = new  HashMap<String, String>();
		HashMap<String,String> Yr = new  HashMap<String, String>(); 
		HashMap<String, HashMap<String, Integer>> X_train = new HashMap<String, HashMap<String, Integer>>(); // <doc,<term,occ>> .....
		HashMap<String, HashMap<String, Integer>> X_test = new HashMap<String, HashMap<String, Integer>>();
		mapVal=split(mapOcc,n=5);
		int k=3;
		for (int i=1;i<=n;i++) {
			X_train=get_X_train(mapVal,i);
			X_test=get_X_test(mapVal,i);
			Yr=getMapClass(X_test);
			Ypred=KNN.Knn_prediction(X_train,X_test,k=3); //map <doc,class>
			score[i-1]=metric.f1mesure(Ypred,Yr); //Ypred map<d1,c1><d2,c2>.....
		}
		return moyenne(score);
	}


	private static HashMap<String, HashMap<String, Integer>> get_X_test(HashMap<Integer, HashMap<String, HashMap<String, Integer>>> mapVal, int j) {
		HashMap<String, HashMap<String, Integer>> X_test= new HashMap<String, HashMap<String, Integer>>();
		X_test=mapVal.get(j);
		return X_test;
	}

	private static HashMap<String, HashMap<String, Integer>> get_X_train(HashMap<Integer, HashMap<String, HashMap<String, Integer>>> mapVal, int j) {
		HashMap<String, HashMap<String, Integer>> X_train= new HashMap<String, HashMap<String, Integer>>();
		for(Integer i:mapVal.keySet()) {
			if(i!=j) {
				for(HashMap<String, HashMap<String, Integer>> map : mapVal.values()) {
					for(String x : map.keySet()) {
						X_train.put(x, map.get(x));
					}
				}
			}
		}
		return X_train;
	}

	public static double validationNaiveBay(HashMap<String,HashMap<String,Integer>> mapOcc,int n) {
		//double [] score  ;
		double[] score = new double [n];
		HashMap<Integer ,HashMap<String, HashMap<String, Integer>>> mapVal = new HashMap<Integer ,HashMap<String, HashMap<String, Integer>>>();
		HashMap<String,String> Ypred = new  HashMap<String, String>();
		HashMap<String,String> Yr = new  HashMap<String, String>(); 
		HashMap<String, HashMap<String, Integer>> X_train = new HashMap<String, HashMap<String, Integer>>(); // <doc,<term,occ>> .....
		HashMap<String, HashMap<String, Integer>> X_test = new HashMap<String, HashMap<String, Integer>>();
		Yr=getMapClass(mapOcc);
		mapVal=split(mapOcc,n=5);
		for (int i=1;i<=n;i++) {
			X_train=get_X_train(mapVal,i);
			X_test=get_X_test(mapVal,i);
			Yr=getMapClass(X_test);
			Ypred=naive_bayes_classifier.Naive_pred(X_train,X_test); //map <doc,class>
			score[i-1]=metric.f1mesure(Ypred,Yr); //Ypred map<d1,c1><d2,c2>.....
		}
		return moyenne(score);
	}
	
	
	public static void main(String args[] ) throws FileNotFoundException {
		String chemin="data_Train";
		HashMap<String, HashMap<String, Integer>> mapOcc= new HashMap<>();       //<doc
		ArrayList<ArrayList<String>> listes = new ArrayList<ArrayList<String>>();
		listes=TF_IDF_Map.getListeDucument(chemin);
		String[] doc_name =TF_IDF_Map.getDocumentName(chemin);
		mapOcc=TF_IDF_Map.getmap2(listes,doc_name);   // map2: map d'occurence <doc1,map1>,.....
		//System.out.println(mapOcc);
		//System.out.println(mapOcc);  // <doc1,map1>......
		//System.out.println(getMapClass(mapOcc));
		//System.out.println(validationKnn(mapOcc));
		//System.out.println(validationNaiveBay(mapOcc)); 
		//Labdata();
		//System.out.println( metric.getClasse_name("001_business"));
		/*
		//------------------------teste metric --------------------------------
		HashMap<String, HashMap<String, Integer>> mapOcc1 = TF_IDF_Map.getmap2(listes,doc_name);
		HashMap<String, HashMap<String, Integer>> mapOcc2 = TF_IDF_Map.getmap2(listes,doc_name);
		HashMap<String, String> map1class = getMapClass(mapOcc1); //<doc,class>......
		HashMap<String, String> map2class = getMapClass(mapOcc2);
		//System.out.println(map1class);
		System.out.println("nombre d'occurence de tech : "+metric.getoccut("tech",map1class));
		System.out.println(metric.getListeclasse(map1class));
		 
		System.out.println(metric.precision(map1class,map2class));
		//System.out.println(metric.recall(map1class,map2class));
		//System.out.println(metric.f1mesure(map1class,map2class));
		 */
	    //---------------------------------------------------
//		///////////////////////////////////: test split /////////////
		//System.out.println(mapOcc);
		//HashMap<Integer, HashMap<String, HashMap<String, Integer>>> mapVal = split(mapOcc,5);
		//System.out.println(mapVal);
		//System.out.println(get_X_test(mapVal,2));
		//System.out.println(get_X_train(mapVal,2));
/*		///////////////////////////////////: test knn Prediction /////////////
		HashMap<Integer, HashMap<String, HashMap<String, Integer>>> mapVal = split(mapOcc,5);
		HashMap<String, HashMap<String, Integer>> x_test = get_X_test(mapVal,1);
		HashMap<String, HashMap<String, Integer>> x_train = get_X_train(mapVal,1);
		System.out.println(x_test);System.out.println(x_train);
		System.out.println(KNN.Knn_prediction(x_train,x_test,3));
*/		
/*		
		///////////////////////////////////: test Bayes Prediction /////////////
		HashMap<Integer, HashMap<String, HashMap<String, Integer>>> mapVal = split(mapOcc,5);
		HashMap<String, HashMap<String, Integer>> x_test = get_X_test(mapVal,1);
		HashMap<String, HashMap<String, Integer>> x_train = get_X_train(mapVal,1);
		System.out.println(naive_bayes_classifier.Naive_pred(x_train,x_test));
*/	
/*		///////////////////////////////////: test knn validation /////////////
		System.out.println("Score de validation KNN : "+validationKnn(mapOcc,5));
		///////////////////////////////////: test NaiveBay validation /////////////
		System.out.println("Score de validation Naive bayesien  : "+validationNaiveBay(mapOcc,5));
*/
		//System.out.println(mapOcc.keySet());
		System.out.println(mapOcc.size());
		System.out.println(Centres_mobiles.K_means(mapOcc, 4));System.out.println(Centres_mobiles.K_means(mapOcc, 4).size());
	}   	   
}
