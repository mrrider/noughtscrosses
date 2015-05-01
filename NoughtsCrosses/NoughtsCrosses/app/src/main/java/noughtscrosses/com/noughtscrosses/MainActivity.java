package noughtscrosses.com.noughtscrosses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.net.Socket;


public class MainActivity extends ActionBarActivity {
    protected static Socket socket = null;
    protected static Authorized user = null;
    protected static final int PORT = 9876;
//    protected static final String URL = "192.168.1.101";
//    protected static final String URL = "191.238.105.168";
    protected static final String URL = "noughts-crosses.cloudapp.net";
    protected static final String REGISTRATION = "registration";
    protected static final String SINGIN = "singin";
    protected static final String SINGOUT = "singout";
    protected static final String TRYCONNECT = "tryconnect";
    protected static boolean play = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(user==null){
            super.onCreate(savedInstanceState);
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            setContentView(R.layout.activity_main);
        }
        if(user!=null){
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
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
