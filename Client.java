package qlsach;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
	String ip;
	int port;
	 String name;
	    Socket socket;    
	    DataInputStream in;
	    DataOutputStream out;
	    public Client(String ip, int port ,Socket socket) throws IOException{
	        this.ip = ip;
	        this.port=port;
	        this.socket = socket;
	        in = new DataInputStream(socket.getInputStream());
	        out = new DataOutputStream(socket.getOutputStream());
	    }
	    public Socket getSocket() {
	        return socket;
	    }
	    public void setSocket(Socket socket) {
	        this.socket = socket;
	    }
	    public DataInputStream getIn() throws IOException {
	        return in;
	    }
	    public void setIn(DataInputStream in) {
	        this.in = in;
	    }
	    public DataOutputStream getOut() throws IOException {
	        return out;
	    }
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	
}
