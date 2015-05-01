package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Realisation {
	public static Connection conn;
	public static Statement statmt;
	public static ResultSet resSet;
	
	// --------ПОДКЛЮЧЕНИЕ К БАЗЕ ДАННЫХ--------
	public static void conn() throws ClassNotFoundException, SQLException {
		conn = null;
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:base.db");
	}

	// --------Создание таблицы--------
	public static void createDB() throws ClassNotFoundException, SQLException {
		statmt = conn.createStatement();
		statmt.execute("CREATE TABLE if not exists 'users' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text, 'pass' text, 'online' INT);");
	}

	// --------Заполнение таблицы--------
	public static void addUser(String name, String pass, int online ) throws SQLException {
		statmt.execute("INSERT INTO 'users' ('name', 'pass', 'online') VALUES ('"+name+"', '"+pass+"', "+online+"); ");
	}
	
	public static boolean singIn(String name, String pass) throws SQLException {
		resSet = null;
		resSet = statmt.executeQuery("SELECT * FROM users WHERE name='"+name+"' AND pass='"+pass+"';");
		String login = null;
		String password = null;
		while(resSet.next()){
			login = resSet.getString("name");
			password = resSet.getString("pass");
		}
		if(login!=null && password != null){
			statmt.executeUpdate("UPDATE users set online='1' WHERE name='"+name+"';");
			return true;
		}
		return false;
	}
	
	public static boolean checkOnline(String name) throws SQLException {
		resSet = null;
		resSet = statmt.executeQuery("SELECT * FROM users WHERE name='"+name+"' AND online='1';");
		String login = null;
		int online = -2;
		while(resSet.next()){
			login = resSet.getString("name");
			online = resSet.getInt("online");
		}
		if(login!=null && online == 1){
			return true;
		}
		return false;
	}
	
	public static void singOut(String name) throws SQLException {
		resSet = null;
		resSet = statmt.executeQuery("SELECT * FROM users WHERE name='"+name+"';");
		String login = null;
		while(resSet.next()){
			login = resSet.getString("name");
		}
		if(login!=null){
			statmt.executeUpdate("UPDATE users set online='0' WHERE name='"+name+"';");
		}
	}
	
	public static boolean checkForRegistration(String login) throws SQLException{
		resSet = null;
		resSet = statmt.executeQuery("SELECT * FROM users WHERE name='"+login+"';");
		String name = null;
		while(resSet.next()){
			name = resSet.getString("name");
		}
		System.out.println(name + " - result of search");
		if(name==null){
			return true;
		}
		return false;
	}

	// -------- Вывод таблицы--------
	public static void readDB() throws ClassNotFoundException, SQLException {
		resSet = statmt.executeQuery("SELECT * FROM users");

		while (resSet.next()) {
			int id = resSet.getInt("id");
			String name = resSet.getString("name");
			String password = resSet.getString("pass");
			int online = resSet.getInt("online");
			System.out.println("ID = " + id);
			System.out.println("name = " + name);
			System.out.println("password = " + password);
			System.out.println("online = " + online);
			System.out.println();
		}

		System.out.println("Table end");
	}

	// --------Закрытие--------
	public static void closeDB() throws ClassNotFoundException, SQLException {
		statmt.close();
		conn.close();
		resSet.close();

		System.out.println("Close");
	}

}
