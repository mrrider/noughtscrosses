package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;

public class Server {
		public static final int PORT = 9876;
		public static HashMap<String, Socket> sockets = new HashMap<String, Socket>();
		public static HashMap<Socket, Socket> plays = new HashMap<Socket, Socket>();
		public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException{
			ServerSocket s = new ServerSocket(PORT);
			Realisation.conn();
	        Realisation.createDB();
	       
			System.out.println("Start server.");
			
			try{
				while (true){
					Socket socket = s.accept();
					try{
						new OneServer(socket);
						Realisation.readDB();
					}catch (IOException e){
						socket.close();
					}
				}
			}finally{
				s.close();
				Realisation.closeDB();
			}
		}


}
