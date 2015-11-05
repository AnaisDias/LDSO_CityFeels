package Database;
import Database.MyDatabase;
// added jre1.8/lib to external JARs


import java.io.*;
import java.net.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


public class Server {

	public static MyDatabase db = new MyDatabase("root","root");
	
	public static void main(String[] args) throws IOException{
		
		if(args.length > 1){
			System.out.println("Usage: java main.Server <port_n>");
			return;
		}
		//db = new Database();
		
		int portn = Integer.parseInt(args[0]);
		HttpServer server = HttpServer.create(new InetSocketAddress(portn), 0);
		server.createContext("/project", new ProjHandler());
		server.setExecutor(null); // creates a default executor
		server.start();
		
	}
	
	static class ProjHandler implements HttpHandler {
		public void handle(HttpExchange httpE) throws IOException {
			SIA sia = new SIA(httpE);
			Thread lThread = new Thread(sia);
			lThread.start();
		}
	}
	
}