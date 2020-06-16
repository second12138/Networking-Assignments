/*
 * Li, Yansong - B00755354 - CSCI3171 Assignment 3
 * Date: 2019-11-10
 * Change: the server is able to play a game with client about guessing a pokemon's name
 */
import java.net.*;
import java.util.Arrays;
import java.io.*;
public class PokemonGuessServer{
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
	//set the number of pokemons to 7 and give a random number
	int random = (int)(Math.random()*(7));
	int count=1;
	//all 7 pokemons
	String[] words = {"Rowlet","Dartrix","Decidueye","Litten","Torracat","Incineroar","Popplio","Brionne","Primarina","Pikipek","Trumbeak",
					"Toucannon","Yungoos","Gumshoos","Grubbin","Charjabug","Vikavolt","Crabrawler","Crabominable","Oricorio","Cutiefly","Ribombee"};
	//find the random pokemon
	String poke = words[random];
	//String for the message from the server
	String[]  message = new String[poke.length()];
	//set the pokemon to a array of Strings
	String[]  pokeArray = new String[poke.length()];
	//set the pokemon to a array of Chars
	char[] charPoke = new char[poke.length()];
	for(int i=0; i<poke.length();i++) {
		charPoke[i] = poke.charAt(i);
	}
	//initialize the message from server to "- - - -" with cooresponding number of characters in the pokemon'name
	for(int i=0; i<poke.length(); i++) {
		message[i] = "-";
	}
	for(int i=0; i<poke.length(); i++) {
		pokeArray[i]=String.valueOf(poke.charAt(i));
	}
	System.out.println("Message from client: " + inputLine);
	output.print(message[0]);
	for(int i=1; i<poke.length(); i++) {
		output.print(" "+message[i]);
	}
	output.println();
	inputLine = input.readLine();
	while (!inputLine.equals("Quit")) {
		System.out.println("Message from client: " + inputLine);
		//do not change the message from server when the guess is wrong
		if(poke.indexOf(inputLine)==-1) {	
			output.print(message[0]);
			for(int i=1; i<poke.length(); i++) {
				output.print(" "+message[i]);
			}
			output.println();
		// put the character into the message from server when the guess is right
		}else{
			
			int[]  index = new int[6];
			int count1=0;
			//assume that there is no such pokemon's name that one character repeats more than 6 times
			for(int i=0; i<poke.length();i++) {
				if(charPoke[i]==inputLine.charAt(0)&&count1==0) {
					index[0]=i;
					count1++;
				}else if(charPoke[i]==inputLine.charAt(0)&&count1==1) {
					index[1]=i;
					count1++;
				}else if(charPoke[i]==inputLine.charAt(0)&&count1==2) {
					index[2]=i;
					count1++;
				}else if(charPoke[i]==inputLine.charAt(0)&&count1==3) {
					index[3]=i;
					count1++;
				}else if(charPoke[i]==inputLine.charAt(0)&&count1==4) {
					index[4]=i;
					count1++;
				}else if(charPoke[i]==inputLine.charAt(0)&&count1==5) {
					index[5]=i;
				}
			}
			if(message[index[4]].equals(inputLine)&&index[5]!=0) {
				message[index[5]] = inputLine;
			}else if(message[index[3]].equals(inputLine)&&index[4]!=0){
				message[index[4]] = inputLine;
			}else if(message[index[2]].equals(inputLine)&&index[3]!=0){
				message[index[3]] = inputLine;
			}else if(message[index[1]].equals(inputLine)&&index[2]!=0){
				message[index[2]] = inputLine;
			}else if(message[index[0]].equals(inputLine)&&index[1]!=0){
				message[index[1]] = inputLine;
			}else{
				message[index[0]] = inputLine;
			}
			//gives a congratulatory message and indicate the number of tries it took to solve the puzzle when client wins
			if(Arrays.equals(message, pokeArray)) {
				output.print(message[0]);
				for(int i=1; i<poke.length(); i++) {
					output.print(" "+message[i]);
				}
				output.print("		Congratulations! You solved the puzzle with " + count + " tries!");
				output.println();
				break;
			}
			output.print(message[0]);
			for(int i=1; i<poke.length(); i++) {
				output.print(" "+message[i]);
			}
			output.println();
			
		}	
		
		inputLine = input.readLine();
		count++;
	}
	//exit when the message from client is Quit
	if (inputLine.equals("Quit")){
		System.out.println("Closing connection");
	}
	output.close();
	input.close();
	link.close();
	serverSock.close();
	}
}