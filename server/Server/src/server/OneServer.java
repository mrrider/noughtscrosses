package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;

public class OneServer extends Thread {
	public static HashMap<String, Socket> sockets = new HashMap<String, Socket>();;
	private Socket socket;
	InputStream in;
	ObjectInputStream objIn;
	BufferedReader br;
	OutputStream out;
	ObjectOutputStream objOut;

	public OneServer(Socket s) throws IOException {
		socket = s;
		in = socket.getInputStream();
		br = new BufferedReader(new InputStreamReader(in));
		out = socket.getOutputStream();
		start();
	}

	public void run() {
		while (true)
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
						sockets.put(reg[0], socket);
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
						sockets.put(inName, socket);
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
					break;
					
				case "tryconnect":
					objIn = new ObjectInputStream(in);
					String[] tryConnect = new String[3];
					tryConnect = (String[]) objIn.readObject();
					String friendName = tryConnect[0];
					boolean online = false;
					online = Realisation.checkOnline(friendName);
				
					if(online){
						objOut = new ObjectOutputStream(out);
						objOut.writeInt(-1);
						objOut.flush();
						Socket s = sockets.get(friendName);
						OutputStream o = s.getOutputStream();
						ObjectOutputStream ob = new ObjectOutputStream(o);
						ob.writeBoolean(true);
						ob.flush();
						InputStream in = s.getInputStream();
						ObjectInputStream ib = new ObjectInputStream(in);
						boolean play = false;
						for(int i = 0; i < 5000; i++){
							play = ib.readBoolean();
							Thread.sleep(10);
						}
						if(play){
							objOut = new ObjectOutputStream(out);
							objOut.writeInt(1);
							objOut.flush();
						}
						else{
							objOut = new ObjectOutputStream(out);
							objOut.writeInt(-1);
							objOut.flush();
						}
					}else{
						objOut = new ObjectOutputStream(out);
						objOut.writeInt(0);
						objOut.flush();
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
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

	}

}
