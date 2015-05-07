package noughtscrosses.com.noughtscrosses;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class MainActivity extends ActionBarActivity {


    protected static Socket socket = null;
    protected static InputStream in = null;
    protected static OutputStream out = null;

    protected static Authorized user = null;
    protected static final int PORT = 9876;
//    protected static final String MYURL = "192.168.1.99";
//    protected static final String URL = "191.238.105.168";
    protected static final String MYURL = "nzr-server.cloudapp.net";

    protected static final String REGISTRATION = "registration";
    protected static final String SINGIN = "singin";
    protected static final String SINGOUT = "singout";
    protected static final String TRYCONNECT = "tryconnect";
    protected static boolean play = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        try {
            socket = new Socket(MYURL, PORT);
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(user!=null){
            Intent intent = new Intent(this, StartGame.class);
            startActivity(intent);
        }
    }

    public void singInClick(View v){
        if(user==null){
            Intent intent = new Intent(this, SingInActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, StartGame.class);
            startActivity(intent);
        }

    }

    public void singUpClick(View v){
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                    Log.d("ClientActivity", "EXIT!!!!!!!!!!!!!");
                }
            }, 3 * 1000);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
