public class Lab7_Ex2 extends Thread {         
	public static void main(String[] args){                
		Lab7_Ex2 thread1, thread2, thread3, thread4, thread5;                 
		thread1 = new Lab7_Ex2();                 
		thread2 = new Lab7_Ex2();                 
		thread3 = new Lab7_Ex2(); 
		thread4 = new Lab7_Ex2(); 
		thread5 = new Lab7_Ex2(); 
        thread1.start();                
        thread2.start();                 
        thread3.start();    
        thread4.start();                 
        thread5.start(); 
	}
	public void run(){                 
		int pause;          
		String[] StarWarChars={"Han Solo", "Darth Vader", "Luke Skywalker", "Chewbacca", "BB-8"}; 
		for (int i=0; i<5; i++){
			try{                                 
				System.out.println(StarWarChars[Integer.parseInt(getName().substring(getName().length()-1))] + " Throw LightSaber!");                                 
				pause = (int)(Math.random()*3000);                                                               
				sleep(pause);      
				System.out.println("\t\t\t" + StarWarChars[Integer.parseInt(getName().substring(getName().length()-1))] + " Catch LightSaber!");    
				pause = (int)(Math.random()*3000);                                                               
				sleep(pause);   
			}catch (InterruptedException e){                                 
				System.out.println(e);                         
			}                 
		}         
	} 
} 