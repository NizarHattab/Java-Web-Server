package utilities;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class Tracker implements Runnable{

	private Thread worker;
    private final AtomicBoolean running = new AtomicBoolean(false);
    ArrayList<ClientSocket> c;
    
    public Tracker( ArrayList<ClientSocket> c)
    {
    	this.c = c;
    }

    public void start() {
        worker = new Thread(this);
        worker.start();
    }
  
    public void stop() {
        running.set(false);
    }
    
    public void interrupt() {
        running.set(false);
        worker.interrupt();
    }
 
    boolean isRunning() {
        return running.get();
    }
 
    boolean isStopped() {
        return !(running.get());
    }
    
    @Override
   	public void run() {
    	try {
			ClientSocket current = c.get(c.size()-1);
		} catch (Exception e) {
			
			return;
		}
    	
    	ArrayList<String> clients = convertToString(c);
           for(ClientSocket cs: c)
           {
        	   try {
				ObjectOutputStream t = new ObjectOutputStream(cs.socket.getOutputStream());
				t.writeObject(clients);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        /*	   try {
				current.send(cs.ip+":"+cs.port);
				System.out.println("informed: "+ cs.socket.getPort());
			} catch (IOException e) {
				e.printStackTrace();
			}*/
           }
    	
    	
           return;
    }
    
    public void resetActive(ArrayList<ClientSocket> newArr)
    {
    	this.c = newArr;
    }
    
    private ArrayList<String> convertToString(ArrayList<ClientSocket> clients)
    {
    	ArrayList<String> toReturn = new ArrayList<String>();
    	for(ClientSocket cs: clients) {
    		toReturn.add(cs.toString());
    	}
    	return toReturn;
    }
           

}
