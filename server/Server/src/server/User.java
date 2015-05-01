package server;

public class User {
	 	private String user;
	    private String password;
	    private int online;

	    public User(String user, String password, int online){
	        this.user = user;
	        this.password = password;
	        this.online = online;
	    }

	    public String getUser(){
	        return user;
	    }

	    public String getPassword(){
	        return password;
	    }

	    public int getOnline(){
	        return online;
	    }
}

