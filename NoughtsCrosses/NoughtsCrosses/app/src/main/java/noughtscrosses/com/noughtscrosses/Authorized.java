package noughtscrosses.com.noughtscrosses;

/**
 * Created by mrrider on 18.04.15.
 */
public class Authorized {
    private String user;
    private String password;
    private int online;

    public Authorized(String user, String password, int online){
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
