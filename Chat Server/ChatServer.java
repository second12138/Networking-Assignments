import java.io.*; 
import java.net.*; 
import java.util.HashSet; 
import java.util.Hashtable;
import java.util.Map;
import java.util.Set; 
public class ChatServer {       
	//Hash table for Client Names and corresponding PrintWriter objects   
	//Hash table for Client IDs and corresponding message received  
	private static Hashtable<String, PrintWriter> writers = new Hashtable<>();      
	private static Hashtable<Integer, String> clientNames = new Hashtable<>(); 

    //TO-DO: Declare other static variables here (ServerSocket and PORT)  
	private static ServerSocket serverSock;
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
		public void run() {      
			try {                 
				String received;    
				int message = 1;                 
				do {                     
					int index = 0;                     
					received = in.readLine();                     
					if (message == 1) {                         
						String clientName = getName().substring(getName().length() - 1);  
						int clientNum = Integer.parseInt(clientName);      
						//add client ID and message received to the clientnames hash table                         
						clientNames.put(clientNum, received); 
						System.out.println(clientNames.get(clientNum) + " has joined");                              
						//add client name and corresponding PrintWriter to the writers hash table   
						writers.put(clientNames.get(clientNum),out);      
						//loop through the writers hash table and broadcast to all clients   
						//that a new client has joined                         
						for (PrintWriter writer : writers.values()) {                             
							writer.println( clientNames.get(clientNum) + " has joined");                         
						}                         
						message++;                     
		   			}else{ 
		   				//TO-DO: Get the clientName and clientNum as before 
		   				String clientName = getName().substring(getName().length() - 1);     
						int clientNum = Integer.parseInt(clientName);
						//out.println("ECHO: " + received);		   				
						//Loop through the writers keySet, that is, (for String client:writers.keySet()) 
						//broadcast to all clients except this one   
						for (PrintWriter writer : writers.values()) {    
							
							if(writers.get(clientNames.get(clientNum)) != writer) {
								writer.println("Message from " + clientNames.get(clientNum) + ": " + received);
							}                         
						}                     
		   				System.out.println("Message from " + clientNames.get(clientNum) + ": " + received);
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