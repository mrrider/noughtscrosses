package noughtscrosses.com.noughtscrosses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.BufferedWriter;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;


public class StartGame extends ActionBarActivity {

    private String name;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (MainActivity.user != null && MainActivity.socket != null) {
            super.onCreate(savedInstanceState);
            pass = MainActivity.user.getPassword();
            name = MainActivity.user.getUser();
            setContentView(R.layout.activity_start_game);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void findFriend(View v) {
        Intent intent = new Intent(this, FindFriend.class);
        startActivity(intent);
    }


    public void logOut(View v) {
        OutputStream out;
        String[] str = new String[3];
        try{
            out = MainActivity.socket.getOutputStream();
            str[0] = name;
            str[1] = null;
            str[2] = null;
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)), true);
            pw.println(MainActivity.SINGOUT);
            pw.flush();
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(str);
            os.flush();
            MainActivity.user = null;
        }catch (Exception e) {
            Log.e("ClientActivity", "S: Error", e);
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void m(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_game, menu);
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


