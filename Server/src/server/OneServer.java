package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;

public class OneServer extends Thread {
	private Socket socket;
	InputStream in;
	ObjectInputStream objIn;
	BufferedReader br;
	OutputStream out;
	ObjectOutputStream objOut;
	boolean work = false;

	public OneServer(Socket s) throws IOException {
		socket = s;
		in = socket.getInputStream();
		br = new BufferedReader(new InputStreamReader(in));
		out = socket.getOutputStream();
		work = true;
		start();
	}

	public void run() {
		while (work)
			try {
				String operation = br.readLine();
				System.out.println(operation);
				if(operation == null){
					break;
				}
				switch (operation) {
				case "registration":
					objIn = new ObjectInputStream(in);
					objOut = new ObjectOutputStream(out);
					String[] reg = new String[3];
					reg = (String[]) objIn.readObject();
					boolean check = false;
					check = Realisation.checkForRegistration(reg[0]);
					if (check) {
						objOut.writeInt(1);
						objOut.flush();
						Realisation.addUser(reg[0], reg[1], 1);
						Realisation.readDB();
						Server.sockets.put(reg[0], socket);
					} else {
						objOut.writeInt(0);
						objOut.flush();
					}
					objOut.flush();
					break;

				case "singin":
					objIn = new ObjectInputStream(in);
					objOut = new ObjectOutputStream(out);
					String[] sing = new String[3];
					sing = (String[]) objIn.readObject();
					String inName = sing[0];
					String inPass = sing[1];
					Realisation.singIn(inName, inPass);
					Realisation.readDB();
					if (Realisation.singIn(inName, inPass)) {
						objOut.writeInt(1);
						Server.sockets.put(inName, socket);
						objOut.flush();
						
					} else {
						objOut.writeInt(0);
						objOut.flush();
				
					}
					break;

				case "singout":
					objIn = new ObjectInputStream(in);
					String[] singout = new String[3];
					singout = (String[]) objIn.readObject();
					String userOut = singout[0];
					Realisation.singOut(userOut);
					Realisation.readDB();
					Server.sockets.remove(singout[0]);
					socket.close();
					socket = null;
					work = false;
					break;
					
				case "tryconnect":
					System.out.println("I am trying to connect!!!");
					objIn = new ObjectInputStream(in);
					String[] tryConnect = new String[3];
					tryConnect = (String[]) objIn.readObject();
					String friendName = tryConnect[0].toString();
					System.out.println("I am searching for friend - " + friendName);
					boolean online = false;
					online = Realisation.checkOnline(friendName);
					System.out.println("he is " + online);
					if(online){
						PrintWriter p = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)), true);
						p.println("online");
				        p.flush();
						Socket s = Server.sockets.get(friendName);
						OutputStream outF = s.getOutputStream();
						PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outF)), true);
						pw.println("play");
						System.out.println("I will send to anoter cient");
				        pw.flush();
//						InputStream inF = s.getInputStream();
//						ObjectInputStream ib = new ObjectInputStream(inF);
//						boolean play = false;
//						play = ib.readBoolean();
//						if(play){
//							objOut = new ObjectOutputStream(out);
//							objOut.writeInt(1);
//							objOut.flush();
//						}
//						else{
//							objOut = new ObjectOutputStream(out);
//							objOut.writeInt(-1);
//							objOut.flush();
//						}
					}
//					}else{
//						objOut = new ObjectOutputStream(out);
//						objOut.writeInt(0);
//						objOut.flush();
//					}
					
					break;
				
				default:
					break;
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
//			catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} 

	}

}
