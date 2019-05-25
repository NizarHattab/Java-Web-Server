package utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class Receiver implements Runnable {

	Socket socket;
	BufferedReader inputStream = null ;
	private Thread worker;
    private final AtomicBoolean running = new AtomicBoolean(false);

	public Receiver(Socket socket) {
		this.socket = socket;
		try {
			inputStream = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
    	running.set(true);
        while (running.get()) {
        	try {
				String msg = inputStream.readLine();
				if(msg.contains("$close$"))
				{
					
				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
        	
        }
		
	}
}
