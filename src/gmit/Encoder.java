package gmit;

import java.io.*;
import java.util.*;

public class Encoder {
	private static int time = (int) new Date().getTime();
	private static Random rand = new Random(time); // this makes sure random not chosen twice, maybe it could come up twice?
	private static int numCount = 0;
	private static int wordCount = 0;
	private static Map<String, List<String>> codeBook = new HashMap<String, List<String>>();
	private static Map<Integer, String> decodeBook = new HashMap<Integer, String>();
	private static String fileName = "commonEnglishWords.txt";
	
	public static void main(String [] args) throws IOException, ClassNotFoundException{
		startEncode();
		//saveCodebook();
		//loadCodebook();
		startDecode();
	}
	
	public static void startEncode() throws IOException{
		//BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileToEncode))); // don't hard code .txt name
		Scanner sr = new Scanner(new FileReader(fileName));
		
		//String line = null;
		int printCount = 0;
		
		File file = new File("encodedFile.txt");
		 
		// if file doesn't exists, then create it
		if (!file.exists()) {
			file.createNewFile();
			System.out.println("creating \"encodedFile.txt\"...");
		}
		else{
			System.out.println("overwriting \"encodedFile.txt\"...");
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		System.out.println("\n==================");
		System.out.println("\nStarting encode...");
		long timer = System.nanoTime();
		
		/* This while loop runs in O(n) time, reason: every single file has to be read in & written */
		while(sr.hasNext()){	//(line = br.readLine()) != null
			//String[]words = line.split("\n");
				String word = sr.next();
				String encodedWord;
			
			//for(int i = 0; i < words.length; i++){
				//String word = words[i];
				
				encodedWord = encode(word); //word
				
				/* This buffered writer method is appending the encoded word to the end of the file, thus is constant time O(1), no searching, it directly adds it to the end of the file */
				bw.write(encodedWord);
				printCount++;
				
				if((printCount % 10) == 0){
					bw.write("\n");
				}
				else
				{
					bw.write(" ");
				}
				
			//} // for
		} // while 
		
		/*====TEST====
		for(Map.Entry entry : codeBook.entrySet()){
			System.out.println(entry.getKey());
		}*/
		
		System.out.println("Encode Successful!");
		System.out.println("Encoded File written to \"encodedFile.txt\"");
		System.out.printf("\nTotal Numbers used:\t%d", numCount);
		System.out.printf("\nTotal Words encoded:\t%d", wordCount);
		System.out.printf("\nTime to encode:\t\t%d seconds\n", (System.nanoTime() - timer )/ 1000000000);
		
		sr.close();
		bw.close();
	} // main
	
	public static String encode(String word){
		int encodedWord;
		String tempString;
		
		if(!codeBook.containsKey(word)){
			List<String> encodedList = new ArrayList<String>();
			
			for(int i = 0; i < 83; i++){
				encodedWord = rand.nextInt(100000 - 10000) + 10000;
				tempString = encodedWord + "";
				
				encodedList.add(tempString);
				numCount++;
			}
			codeBook.put(word, encodedList);
			
			wordCount++;
		}
		else{
			List<String> encodedList = codeBook.get(word);
			
			for(int i = 0; i < 83; i++){
				encodedWord = rand.nextInt(100000 - 10000) + 10000;
				tempString = encodedWord + "";
				encodedList.add(tempString);
				numCount++;
			}			
		}
		
		List<String> encodedList = codeBook.get(word);
		
		Random rand = new Random(time);
		
		tempString = encodedList.get(rand.nextInt(encodedList.size()-1));
		
		decodeBook.put(Integer.parseInt(tempString), word);
		
		return tempString;
	} // encode
	
	public static void saveCodebook() throws IOException{
		/* http://stackoverflow.com/questions/9144472/java-hashmap-and-serializable:
		 * HashMap uses writeObject and readObject to implement custom serialization rather than just letting its field be serialized normally
		 * It writes the number of buckets, the total size and each of the entries to the stream and rebuilds itself from those fields when deserialized. As tzaman says, the table itself is unnecessary in the serial form, so it's not serialized to save space.
		 */
		long timer = System.nanoTime();
		System.out.println("\n==================");
		System.out.println("\nSaving Code Book...");
		
        File file = new File("codeBook.txt");
		if (!file.exists()) {
			System.out.println("creating \"codeBook.txt\"...");
			file.createNewFile();
		}
		else{
			System.out.println("overwriting \"codeBook.txt\"...");
		}
			
        
        FileOutputStream fileOut = new FileOutputStream(file);
        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
        
        objectOut.writeObject(codeBook);
        
        objectOut.close();
        fileOut.close();
        
        System.out.println("\nCode Book saved to \"codeBook.txt\"");
        System.out.printf("Time taken to save:\t%d seconds\n", (System.nanoTime() - timer )/ 1000000000);
	}
	
	public static void loadCodebook() throws IOException, ClassNotFoundException{
		long timer = System.nanoTime();
		System.out.println("\n==================");
		System.out.println("\nLoading Code Book...");
		
		File file = new File("codeBook.txt");
		if (!file.exists()) {
			System.out.println("ERROR: could not find file: \"codeBook.txt\"");
		}
		else{
			System.out.println("Loading from \"codeBook.txt\"...");
		}
		
	    FileInputStream fileIn = new FileInputStream(file);
	    ObjectInputStream objectIn = new ObjectInputStream(fileIn);
	    
	    codeBook = (HashMap<String, List<String>>) objectIn.readObject();
	    
	    objectIn.close();
	    fileIn.close();
	    
        System.out.println("\nCode Book loaded from \"codeBook.txt\"");
        System.out.printf("Time taken to load:\t%d seconds\n", (System.nanoTime() - timer )/ 1000000000);
	}
	
	public static void saveDecodebook() throws IOException{
		/* http://stackoverflow.com/questions/9144472/java-hashmap-and-serializable:
		 * HashMap uses writeObject and readObject to implement custom serialization rather than just letting its field be serialized normally
		 * It writes the number of buckets, the total size and each of the entries to the stream and rebuilds itself from those fields when deserialized. As tzaman says, the table itself is unnecessary in the serial form, so it's not serialized to save space.
		 */
		long timer = System.nanoTime();
		System.out.println("\n==================");
		System.out.println("\nSaving Decode Book...");
		
        File file = new File("decodeBook.txt");
		if (!file.exists()) {
			System.out.println("creating \"decodeBook.txt\"...");
			file.createNewFile();
		}
		else{
			System.out.println("overwriting \"decodeBook.txt\"...");
		}
			
        
        FileOutputStream fileOut = new FileOutputStream(file);
        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
        
        objectOut.writeObject(decodeBook);
        
        objectOut.close();
        fileOut.close();
        
        System.out.println("\nDecode Book saved to \"decodeBook.txt\"");
        System.out.printf("Time taken to save:\t%d seconds\n", (System.nanoTime() - timer )/ 1000000000);
	}
	
	public static void loadDecodebook() throws IOException, ClassNotFoundException{
		long timer = System.nanoTime();
		System.out.println("\n==================");
		System.out.println("\nLoading Decode Book...");
		
		File file = new File("decodeBook.txt");
		if (!file.exists()) {
			System.out.println("ERROR: could not find file: \"decodeBook.txt\"");
		}
		else{
			System.out.println("Loading from \"decodeBook.txt\"...");
		}
		
	    FileInputStream fileIn = new FileInputStream(file);
	    ObjectInputStream objectIn = new ObjectInputStream(fileIn);
	    
	    decodeBook = (HashMap<Integer, String>) objectIn.readObject();
	    
	    objectIn.close();
	    fileIn.close();
	    
        System.out.println("\nCode Book loaded from \"decodeBook.txt\"");
        System.out.printf("Time taken to load:\t%d seconds\n", (System.nanoTime() - timer )/ 1000000000);
	}
	
	public static void startDecode() throws IOException{
		System.out.println("\n==================\n");
		//File fileName = new File("encodedFile.txt");
		//FileReader fr = new FileReader("encodedFile.txt");
		Scanner sr = new Scanner(new FileReader("encodedFile.txt"));

		//BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("encodedFile.txt")));
		String line = null;
		int printCount = 0;
		
		File file = new File("decodedFile.txt");
		 
		if (!file.exists()) {
			file.createNewFile();
			System.out.println("creating \"decodedFile.txt\"...");
		}
		else{
			System.out.println("overwriting \"decodedFile.txt\"...");
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		
		System.out.println("\nStarting decode...");
		long timer = System.nanoTime();
		
		while(sr.hasNext()){ //(line = sr.next()) != null
				String codedStr = sr.next();
				int codedInt = Integer.parseInt(codedStr);
				String decodedStr;
				
				decodedStr = decode(codedInt); //word		Integer.parseInt(line)
				
				bw.write(decodedStr);
				printCount++;
				
				if((printCount % 10) == 0){
					bw.write("\n");
				}
				else
				{
					bw.write(" ");
				}
				
			//} // for
		} // while 
		
		System.out.println("Decode Successful!");
		System.out.println("Decoded File written to \"decodedFile.txt\"");
		//System.out.printf("\nTotal Numbers used:\t%d", numCount);
		//System.out.printf("\nTotal Words encoded:\t%d", wordCount);
		System.out.printf("\nTime to decode:\t\t%d seconds\n", (System.nanoTime() - timer )/ 1000000000);
		
		sr.close();
		bw.close();
	} // main

	public static String decode(int encodedWord){
		String tempString;
		boolean found = false;
		int searchCount = 0;
		
		//while(found == false && searchCount < decodeBook.size()){
		if(decodeBook.containsKey(encodedWord)){
			tempString = decodeBook.get(encodedWord);
		}
		else
		{
			tempString = "__unknownCode__";
		}
		
		return tempString;
	} // decode
	
}// class
