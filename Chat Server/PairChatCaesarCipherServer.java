import java.io.*; 
import java.net.*; 
import java.util.HashSet; 
import java.util.Hashtable;
import java.util.Map;
import java.util.Set; 
public class PairChatCaesarCipherServer {       
	//Hash table for Client Names and corresponding PrintWriter objects   
	//Hash table for Client IDs and corresponding message received  
	private static Hashtable<String, PrintWriter> writers = new Hashtable<>();      
	private static Hashtable<Integer, String> clientNames = new Hashtable<>(); 
	private static Hashtable<String, Integer> clientNumbers = new Hashtable<>();
	private static int [] key = new int [999];
	//private ArrayList <Pair> pair = new ArrayList<Pair>();
    //TO-DO: Declare other static variables here (ServerSocket and PORT)  
	private static ServerSocket serverSock;
	private static int first=0;
	private static final int PORT = 3456;
	//as in the basic multi-client/server program 
	
    //main method 
	public static void main(String[] args) throws IOException {
		//TO-DO: code similar to basic multi-client/server program  
		try{// try to create a new server sockcet with the port number that was declared.
			serverSock=new ServerSocket(PORT);
		}catch (IOException e) {//catch the input and output exception, print " Can't listen on" with the corresponding port number
			System.out.println("Can't listen on " + PORT);
			//exit the program with code 1 (error state)
			System.exit(1);
		}
		do {//keep run until a client program is connected
			Socket client = null;
			System.out.println("Listening for connection...");
			try{//try to connect to the client 
				client = serverSock.accept();
				System.out.println("New client accepted");
				
				ClientHandler handler = new ClientHandler(client);
				//oncee the connection succeed, the Client handler takes over
				handler.start();
			}catch (IOException e) {//catch the IO exception, print "Accept failed", and exit with code 1 (error state)
				System.out.println("Accept failed");
				System.exit(1);
			}
			System.out.println("Connection successful");
			System.out.println("Listening for input ...");
		}while(true);
		
	} 
	
	//Make the ClientHandler a class inside the main class as below     
	private static class ClientHandler extends Thread {         
		private Socket client;         
		private BufferedReader in;         
		private PrintWriter out; 
		
		public ClientHandler(Socket socket) {         
			//TO-DO: code similar to basic multi-client/server program
			client = socket;
			
			try//try to read input from client and output related message
			{
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				out = new PrintWriter(client.getOutputStream(),true);
			}
			catch (IOException e)// catch the input and output exception, print the stack trace of the exception
			{
				e.printStackTrace();
			}
		}
		/*
		 * README: the comunication between two clients requires two checks, which means after A indicates it is willing 
		 * to pair B, B also needs to indicates it is willing to pair with A. Then they can comunicate with each other.
		 *  
		 * If there is only one check from A to B, then B will not able to send message to A but rather just receive 
		 * messages from A since B did not indicate it is willing to comunicate with A yet.			 
		 */
		public void run() {      	
			try {                 
				String received;    
				int message = 1;    
				String newString = "";
				String name = null;
				
				do { 				
					
					//int index = 0;  
					received = in.readLine(); 
					if (message == 1) {                         
						String clientName = getName().substring(getName().length() - 1);  
						int clientNum = Integer.parseInt(clientName); 
						//add client ID and message received to the clientnames hash table                         
						clientNames.put(clientNum, received); 
						clientNumbers.put(received, clientNum);
						System.out.println(clientNames.get(clientNum) + " has joined");     
						//add client name and corresponding PrintWriter to the writers hash table   
						writers.put(clientNames.get(clientNum),out);      
						//loop through the writers hash table and broadcast to all clients   
						//that a new client has joined                         
						for (PrintWriter writer : writers.values()) {                             
							writer.println( clientNames.get(clientNum) + " has joined");   
			   				writer.println("Who do you want to communicate with?: ");

						} 
						
						message++;    
		   			}else if(message == 2){
		   				String clientName = getName().substring(getName().length() - 1);  
			   			int clientNum = Integer.parseInt(clientName);
			   			/*
			   			 * README: Assume the cient enter a right name at once. If client entered a worng name, 
			   			 * her/she will not able to enter a name again.
			   			 */
			   			if(!clientNames.contains(received)) {
			   				out.println("No such client");
			   			}else {
			   				int pairNum = clientNumbers.get(received);
				   			int random = (int)(Math.random()*(25)+1);
				   			if(key[clientNum]==0) {
				   				key[clientNum]=random;
				   				key[pairNum]=random;
				   			}
			   				name = received;
			   				out.println("You have paired with " + received);
			   				out.println("The key is " + key[clientNum]);
			   				for (PrintWriter writer : writers.values()) {                             
			   					if(writers.get(received) == writer) {
			   						writer.println("You are receiving messages from " + clientNames.get(clientNum));
			   						writer.println("The key is " + key[pairNum]);	
			   					}
			   				} 
			   			}
			   			message++;	
		   			}else {
		   				//TO-DO: Get the clientName and clientNum as before 
		   				String clientName = getName().substring(getName().length() - 1);     
						int clientNum = Integer.parseInt(clientName);
						/*
						 * reference: https://www.geeksforgeeks.org/replace-every-character-of-string-by-character-whose-ascii-value-is-k-times-more-than-it/
						 */
						for (int i = 0; i < received.length(); ++i) {   
							int ascValue = received.charAt(i); 
							char temp = received.charAt(i);
					        int kValue = key[clientNum];
					        int random = key[clientNum];
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
						//out.println("ECHO: " + received);		   				
						//Loop through the writers keySet, that is, (for String client:writers.keySet()) 
						//broadcast to all clients except this one   
						for (PrintWriter writer : writers.values()) {    
							if(writers.get(name) == writer) {
								writer.println("Encrypted message from " + clientNames.get(clientNum) + ": " + newString);
								writer.println("Decrypted message from " + clientNames.get(clientNum) + ": " + received);
							}                         
						}                     
		   				System.out.println("Encrypted message from " + clientNames.get(clientNum) + ": " + newString);
		   				newString = "";
		   			}
				} while (!received.equals("BYE"));             
			} catch (IOException e) { 
				e.printStackTrace();
			}finally{// the code for closing connection is always run no matter the exceptions are handled or not
				try//close the connection by close the socket
				{
					if(client!=null)
					{
						System.out.println("Closing down connection...");
						client.close();
					}
				}
				catch(IOException e)// catch the input and output exception, print the stack trace of the exception
				{
					e.printStackTrace();
				}
			}
		}
	}
}
