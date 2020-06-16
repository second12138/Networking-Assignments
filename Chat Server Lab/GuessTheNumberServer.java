import java.net.*;
import java.io.*;
import java.util.Date;
public class GuessTheNumberServer{
	/*
	 * @thorws IOExecption the main method throws IO Exception
	 */
	public static void main(String[] args) throws IOException{
	ServerSocket serverSock = null;//create a ServerSocket object
	try{//Indtantiate the ServerSocket object on port 50000
		serverSock = new ServerSocket(50000);
	}
	catch (IOException ie) {// In case the connection was not successful
		System.out.println("Can't listen on 50000");
		System.exit(1);
	}
	Socket link = null;
	System.out.println("Listening for connection ...");// put the server into a waiting state
	try {//if the client sends a request, then we failed to accpet the connection
		link = serverSock.accept();
	}
	catch (IOException ie) {
		System.out.println("Accept failed");
		System.exit(1);
	}
	System.out.println("Connection successful");
	System.out.println("Listening for input...");// if the connection is successful, we display a message
	//get references to streams associated with socket
	PrintWriter output = new PrintWriter(link.getOutputStream(),true);//wrap the OuputStream with a PrintWriter object
	BufferedReader input = new BufferedReader(new InputStreamReader(link.getInputStream()));//wrap the InoutSream object with a BufferedReader object
	String inputLine = input.readLine();
	int random = (int)(Math.random()*((100-0)+1));
	int number;
	System.out.println("Meaage from client: " + inputLine);
	output.println("I have a number between 0 and 100. Can you guess it?");
	inputLine = input.readLine();
	while (!inputLine.equals("Bye")) {//receiving data
		System.out.println("Message from client: " + inputLine);//sending data
		number  = Integer.parseInt(inputLine);
		if(number == random) {
			output.println("You got it!");
			break;
		}else if(number > random) {
			output.println("Guess lower");
		}else {
			output.println("Guess higher");
		}
		inputLine = input.readLine();
	}
	if (inputLine.equals("Bye")) {//receives a "Bye" message from the client
		output.println("Closing connection");
	}
	output.close();
	input.close();
	link.close();
	serverSock.close();
	}
}
