import java.io.*;
import java.net.*;
public class EchoClient{
	/*
	 * @throws IOException the main method throws IO Expections
	 */
	public static void main(String[] args) throws IOException{
	Socket link = null;//Intialized a socket object
	PrintWriter output = null;//Initialized a PrintWriter object
	BufferedReader input = null;//Initialized a BufferedReader

	try{
	link= new Socket("127.0.0.1", 50000);//create the lik object that use the local machine IP address
	output = new PrintWriter(link.getOutputStream(), true);// set output to an output stream from the socket
	input = new BufferedReader(new InputStreamReader(link.getInputStream()));//set input tp an input stream from the socket
	}
	catch(UnknownHostException e)
	{//catch expections if the above doesn't work
		System.out.println("Unknown Host");
		System.exit(1);
	}
	catch (IOException ie) {
		System.out.println("Cannot connect to host");
		System.exit(1);
	}
	BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));//receive messages sent by the PrintWriter object
	String usrInput;
	while ((usrInput = stdIn.readLine())!=null) {
		output.println(usrInput);//send to server
		System.out.println("Echo from Server: " + input.readLine());//receives from server and display it.
	}
	output.close();
	input.close();
	stdIn.close();
	link.close();
	}
}