package noughtscrosses.com.noughtscrosses;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;


public class StartGame extends ActionBarActivity {

    private String name;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        if (MainActivity.user != null && MainActivity.socket != null) {
            pass = MainActivity.user.getPassword();
            name = MainActivity.user.getUser();
            setContentView(R.layout.activity_start_game);
            Thread tr = waiting();
            tr.setDaemon(true);
            tr.start();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void findFriend(View v) {
        Intent intent = new Intent(this, FindFriend.class);
        startActivity(intent);
    }

    public void check(View v) throws InterruptedException {
        waiting();
    }

    public Thread waiting(){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                boolean done = false;
                BufferedReader br;
                try{
                    br = new BufferedReader(new InputStreamReader(MainActivity.in));
                    do{
                        Log.d("Op = ", "dsfadfadfdsfafsgdgfafd");
                        String op = br.readLine();
                        Log.d("Op = ", op);
                        if(op.equals("play")){
                            MainActivity.play = true;
                            Intent intent = new Intent(StartGame.this, Game.class);
                            startActivity(intent);
                            done = true;
                        }
                    }while(!done);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    public void logOut(View v) {
        String[] str = new String[3];
        try{
            str[0] = name;
            str[1] = null;
            str[2] = null;
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(MainActivity.out)), true);
            pw.println(MainActivity.SINGOUT);
            pw.flush();
            ObjectOutputStream os = new ObjectOutputStream(MainActivity.out);
            os.writeObject(str);
            os.flush();
            MainActivity.user = null;
            MainActivity.socket.close();
            MainActivity.socket = null;
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
    public void onBackPressed() {
        Toast.makeText(this, "Press 'Log out' to exit",
                Toast.LENGTH_SHORT).show();
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


