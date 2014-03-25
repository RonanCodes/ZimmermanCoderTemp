package gmit;

import java.io.*;
import java.util.*;

public class TestRunner {
	private static int time = (int) new Date().getTime();
	private static Random rand = new Random(time);
	private static int numCount = 0;
	private static int wordCount = 0;
	private static Map<String, List<String>> codeBook = new HashMap<String, List<String>>();
	private static Map<Integer, String> decodeBook = new HashMap<Integer, String>();
	
	public static void main(String [] args) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("commonEnglishWords.txt")));
		
		String line = null;
		
		while((line = br.readLine()) != null){
			String[]words = line.split("\n");
			
			for(int i = 0; i < words.length; i++){
				String word = words[i];
				
				encode(word);
			} // for
		} // while 
		
		for(Map.Entry entry : codeBook.entrySet()){
			System.out.println(entry.getKey());
		}
		
		System.out.println("\n==================\nTotal Numbers: " + numCount);
		System.out.println("Total Words: " + wordCount);
		
		br.close();
	} // main
	
	public static void encode(String word){
		if(!codeBook.containsKey(word)){
			int encodedWord;
			List encodedList = new ArrayList<String>();
			
			for(int i = 0; i < 83; i++){
				encodedWord = rand.nextInt(100000 - 10000) + 10000;
				encodedList.add(encodedWord);
				numCount++;
			}
			codeBook.put(word, encodedList);
			
			wordCount++;
		}
	} // encode
	
	public static String decode(int i){
		
		return "string";
	} // decode
}// class
