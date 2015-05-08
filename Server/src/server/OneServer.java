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
						p.println("c");
				        p.flush();
						p.println("online");
				        p.flush();
						Socket s = Server.sockets.get(friendName);
						OutputStream outF = s.getOutputStream();
						PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outF)), true);
						pw.println("play");
						System.out.println("I will send to anoter cient");
				        pw.flush();
				        Server.plays.put(s, socket);
				        Server.plays.put(socket, s);
						Realisation.readDB();
				        break;
					}
					else{
						System.out.println("he is " + online);
						PrintWriter p = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)), true);
						p.println("c");
				        p.flush();
						p.println("ofline");
				        p.flush();
						Realisation.readDB();
				        break;
					}
				case "game":
					Socket s = Server.plays.get(socket);
					OutputStream sOut = s.getOutputStream();
					InputStream sIn = s.getInputStream();
					System.out.println("yeah!!!1");
					BufferedReader brb = new BufferedReader(new InputStreamReader(in));
					String strop = brb.readLine();
					System.out.println("strop = " + strop);
					switch (strop) {
						case "tl":
							System.out.println("TL pressed");
							PrintWriter p = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sOut)), true);
							p.println("tl");
					        p.flush();
							break;
							
						case "tc":
							System.out.println("TС pressed");
							PrintWriter pс = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sOut)), true);
							pс.println("tс");
					        pс.flush();
							break;
//						case "tr":
//							System.out.println("TR pressed");
//							PrintWriter ptr = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sOut)), true);
//							ptr.println("tr");
//							ptr.flush();
//							break;
//							
//						case "cl":
//							System.out.println("CL pressed");
//							PrintWriter cl = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sOut)), true);
//							cl.println("cl");
//					        cl.flush();
//							break;
//							
//						case "cc":
//							System.out.println("cc pressed");
//							PrintWriter cc = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sOut)), true);
//							cc.println("cc");
//					        cc.flush();
//							break;
//						case "cr":
//							System.out.println("cr pressed");
//							PrintWriter cr = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sOut)), true);
//							cr.println("cr");
//							cr.flush();
//							break;
//
//						case "bl":
//							System.out.println("bl pressed");
//							PrintWriter bl = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sOut)), true);
//							bl.println("bl");
//					        bl.flush();
//							break;
//							
//						case "bc":
//							System.out.println("bc pressed");
//							PrintWriter bc = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sOut)), true);
//							bc.println("bc");
//							bc.flush();
//							break;
//						case "br":
//							System.out.println("br pressed");
//							PrintWriter br = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sOut)), true);
//							br.println("br");
//							br.flush();
//							break;

						default:
							break;
					}
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
