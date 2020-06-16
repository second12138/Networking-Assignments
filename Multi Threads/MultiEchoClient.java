import java.io.*;
import java.net.*;
public class MultiEchoClient
{
	private static InetAddress host;
	//declare a not changeable variable for port number
	private static final int PORT = 1234;
	private static Socket link;
	private static BufferedReader in;
	private static PrintWriter out;
	private static BufferedReader keyboard;
	//the main method throws all the exceptions
	public static void main(String[] args) throws Exception
	{
		try//construct a new socket,a new print writer, and two new buffered readers. Get the address of the local host
		{
			InetAddress host = InetAddress.getLocalHost();
			link = new Socket(host, PORT);
		    //link = new Socket("127.0.0.1",PORT);
			in = new BufferedReader(new InputStreamReader(link.getInputStream()));
			out = new PrintWriter(link.getOutputStream(), true);
			keyboard = new BufferedReader(new InputStreamReader(System.in));
			
			String message, response;
			int first = 1;
			do// while the input from the client is not "BYE" continue to read the input and give response
			{	
				if(first == 1) {
					System.out.print("Enter name: ");
					first++;
					message = keyboard.readLine();
					out.println(message);
					response = in.readLine();
				}else {
					System.out.print("Enter message (BYE to exit): ");
					message = keyboard.readLine();
					out.println(message);
					response = in.readLine();
					System.out.println(response);
				}

				
			}while(!message.equals("BYE"));
		}
		catch(UnknownHostException e)// catch the unknown host exception, print "Unkown host" and exit with code 1 (error state)
		{
			System.out.println("Unknown host");
			System.exit(1);
		}
		catch(IOException e)// catch the input and output exception, print "Cannot connect to host" and exit with code 1 (error state)
		{
			System.out.println("Cannot connect to host");
			System.exit(1);
		}
		finally// the code for closing connection is always run no matter the exceptions are handled or not
		{
			try//close the connection by close the socket
			{
				if (link!=null)
				{
					System.out.println("Closing down connection ...");
					link.close();
				}
			}
			catch(IOException e)// catch the input and output exception, print the stack trace of the exception
			{
				e.printStackTrace();
			}
		}
	}
}