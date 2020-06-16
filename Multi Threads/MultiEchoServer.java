import java.io.*;
import java.net.*;
public class MultiEchoServer
{
	private static ServerSocket serverSock;
	//declare a not changeable variable for port number
	private static final int PORT = 1234;
	//the main method throws the IO exception
	public static void main(String[] args) throws IOException
	{
		try{// try to create a new server sockcet with the port number that was declared.
			serverSock=new ServerSocket(PORT);
		}
		catch (IOException e)//catch the input and output exception, print " Can't listen on" with the corresponding port number
		{
			System.out.println("Can't listen on " + PORT);
			//exit the program with code 1 (error state)
			System.exit(1);
		}
		do//keep run until a client program is connected
		{
			Socket client = null;
			System.out.println("Listening for connection...");
			try{//try to connect to the client 
				client = serverSock.accept();
				System.out.println("New client accepted");
				ClientHandler handler = new ClientHandler(client);
				//oncee the connection succeed, the Client handler takes over
				handler.start();
			}
			catch (IOException e)//catch the IO exception, print "Accept failed", and exit with code 1 (error state)
			{
				System.out.println("Accept failed");
				System.exit(1);
			}
			System.out.println("Connection successful");
			System.out.println("Listening for input ...");
		}while(true);
	}
}
// the ClientHandler takes the responsibility of communication once the connection with client is succeed
class ClientHandler extends Thread
{
	private Socket client;
	private BufferedReader in;
	private PrintWriter out;
	private static final int numClients = 4;
	private String[] clientNames = new String[numClients];
	public ClientHandler(Socket socket)
	{
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
	public void run()
	{
		try// try to receive messages from client
		{
			String received;
			int first=1;
			do//while the receive message is not euqal to "BYE" continue to receive messages and print it out 
			{	
					received = in.readLine();
					String clientName=getName().substring(getName().length()-1);
					int clientNum = Integer.parseInt(clientName);
					if(first == 1) {
						clientNames[clientNum] = received;
						first++;
						out.println();
					}else {
						out.println("ECHO: " + received);
						System.out.println("Message from " + clientNames[clientNum] + " :" + received);
					}
					
				
 
			}while (!received.equals("BYE"));
		}
		catch(IOException e)// catch the input and output exception, print the stack trace of the exception
		{
			e.printStackTrace();
		}
		finally// the code for closing connection is always run no matter the exceptions are handled or not
		{
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