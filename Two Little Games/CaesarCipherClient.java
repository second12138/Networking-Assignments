/*
 * Li, Yansong - B00755354 - CSCI3171 Assignment 3
 * Date: 2019-11-10
 * Change: Made a change that the messages from server will not show again after the server indicated the key
 * , which is the same as the screenshot showed in the assginment instruction.
 */
import java.io.*;
import java.net.*;
public class CaesarCipherClient{
	public static void main(String[] args) throws IOException{
	Socket link = null;
	PrintWriter output = null;
	BufferedReader input = null;

	try{
	link= new Socket("127.0.0.1", 50000);
	output = new PrintWriter(link.getOutputStream(), true);
	input = new BufferedReader(new InputStreamReader(link.getInputStream()));
	}
	catch(UnknownHostException e)
	{
		System.out.println("Unknown Host");
		System.exit(1);
	}
	catch (IOException ie) {
		System.out.println("Cannot connect to host");
		System.exit(1);
	}
	BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
	String usrInput;
	int i=0;
	while ((usrInput = stdIn.readLine())!=null) {
		output.println(usrInput);
		//the message from server one shows once
		if(i==0) {
		System.out.println("Echo from Server: " + input.readLine());
		i++;
		}
	}
	output.close();
	input.close();
	stdIn.close();
	link.close();
	}
}