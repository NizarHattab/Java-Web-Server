package utilities;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;

import mainApp.Activity;
import mainApp.Controller;

public class ClientSocket implements Runnable {

	private Thread worker;
	private final AtomicBoolean running = new AtomicBoolean(false);

	protected Socket socket;
	protected DataOutputStream outputStream = null;
	protected String data;
	protected InetAddress ip;
	protected int port;
	protected Receiver rec;
	protected Controller ctrl;
	BufferedReader inputStream = null ;

	public ClientSocket(Socket socket, String data,Controller ctrl) throws Exception {
		this.socket = socket;
		this.data = data;
		this.ctrl = ctrl;
		decodeMessage(data, this.ip, this.port);
		outputStream = new DataOutputStream(socket.getOutputStream());
		try {
			inputStream = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		} catch (IOException e) {
			ctrl.log(new Activity(e.getMessage()));
		}
		//System.out.println("Client " + this.socket.getPort() + " with data: " + this.ip + ":"+this.port + " has been created.");
		// rec = new Receiver(this.socket);

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
			
			String msg;
			try {
				msg = inputStream.readLine();
				System.err.println(msg);
				if(msg.contains("$close$"))
				{	
					//send("$close$");
					System.err.println(msg);
					ctrl.log(new Activity(this.socket.getPort() + " has disconnected."));
					ctrl.MainServer.mainThread.connectedClients.remove(this);
					ctrl.MainServer.mainThread.update();
					this.stop();
				}
			} catch (IOException e) {
				ctrl.log(new Activity(e.getMessage()));
			}
			
		}

	}

	

	private boolean decodeMessage(String msg, InetAddress ip, int port/*, String status*/) throws Exception {
		System.out.println(msg);
		if (msg.substring(1).startsWith("$IP=")) {
			String ips = msg.substring(5, msg.length());
			ips = ips.substring(1, ips.indexOf(":"));	
			System.out.println("74 \t ports: " + ips);
			String ports = msg.substring(msg.lastIndexOf(':') + 1, msg.lastIndexOf('#'));
			System.out.println("76 \t ports: " + ports);
			if (validatePort(Integer.parseInt(ports)))
			{
				port = Integer.parseInt(ports);	
			}
			else {
				return false;	
			}
			/*if (validatePort(Integer.parseInt(ports.substring(ports.lastIndexOf('=') + 1))))
			{
				port = Integer.parseInt(ports.substring(ports.lastIndexOf('=') + 1));	
			}
			else {
				return false;	
			}*/
			setPort(port);
			setIP(InetAddress.getByName(ips));
			return true;
			//System.out.println(port);
			/*String cmd = msg.substring(msg.lastIndexOf('#') + 1);
			if (cmd.isEmpty()) {
				status = "N";
				return;
			} else {
				status = cmd;
				System.out.println(cmd);
			}*/

		}
		return false;
	}
	
	protected void setIP(InetAddress ip)
	{
		this.ip = ip;
	}
	protected void setPort(int port)
	{
		this.port = port;
	}
	
	protected void send(String msg) throws IOException
	{
		outputStream.writeBytes(msg + '\n');
	}
	

	protected boolean validatePort(int port) {
		if (port < 0 || port > 65535) {
			return false;
		} else
			return true;
	}
	
	@Override
	public String toString()
	{
		return this.ip + ":"+this.port;
	}
}
