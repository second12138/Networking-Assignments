/*
 * Li, Yansong - B00755354 - CSCI3171 Assignment 3
 * Date: 2019-11-10
 * change: the server is able to decrypt the message
 */
import java.net.*;
import java.io.*;
public class CaesarCipherServer{
	public static void main(String[] args) throws IOException{
	ServerSocket serverSock = null;
	try{
		serverSock = new ServerSocket(50000);
	}
	catch (IOException ie) {
		System.out.println("Can't listen on 50000");
		System.exit(1);
	}
	Socket link = null;
	System.out.println("Listening for connection ...");
	try {
		link = serverSock.accept();
	}
	catch (IOException ie) {
		System.out.println("Accept failed");
		System.exit(1);
	}
	System.out.println("Connection successful");
	System.out.println("Listening for input...");
	PrintWriter output = new PrintWriter(link.getOutputStream(),true);
	BufferedReader input = new BufferedReader(new InputStreamReader(link.getInputStream()));
	String inputLine = input.readLine();
	//string for decrypted message
	String newString = "";
	//random key from 1-25
	int random = (int)(Math.random()*(25)+1);
	System.out.println("Message from client: " + inputLine);
	output.println("The key is " + random);
	inputLine = input.readLine();
	while (!inputLine.equals("Bye")) {
		/*
		 * reference: https://www.geeksforgeeks.org/replace-every-character-of-string-by-character-whose-ascii-value-is-k-times-more-than-it/
		 */
		for (int i = 0; i < inputLine.length(); ++i) {   
			int ascValue = inputLine.charAt(i); 
			char temp = inputLine.charAt(i);
	        int kValue = random;  
	        //find the cooresponding new character when the character is upper case 
	        if(Character.isUpperCase(temp)) {
	        	if (ascValue + random> 90) { 
		        	random -= (90 - ascValue); 
		            random = random % 26; 	                
		            newString += (char)(64 + random); 
		        } else { 
		        	newString += (char)(ascValue + random); 
		        } 
		        random = kValue; 
		    //keep the character same when it is a space
	        }else if(ascValue == 32){
	        	newString += (char)(ascValue); 
	        	random = kValue;
	        //find the cooresponding new character when the character is lower case 
	        }else {
	        	if (ascValue + random> 122) { 
		        	random -= (122 - ascValue); 
		            random = random % 26; 	                
		            newString += (char)(96 + random); 
		        } else { 
		        	newString += (char)(ascValue + random); 
		        } 
		        random = kValue; 
	        }
	        
		}
		System.out.println("Message from client: " + newString);
		System.out.println("Decrypted: " + inputLine);
		output.println(inputLine);
		inputLine = input.readLine();
		newString = "";
	}
	//exit when the message from client is Bye
	if (inputLine.equals("Bye")){
		System.out.println("Closing connection");
	}
	output.close();
	input.close();
	link.close();
	serverSock.close();
	}
}