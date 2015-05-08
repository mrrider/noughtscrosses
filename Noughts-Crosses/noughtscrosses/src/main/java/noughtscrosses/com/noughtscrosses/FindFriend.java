package noughtscrosses.com.noughtscrosses;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;


public class FindFriend extends ActionBarActivity {
    String friendName = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_find_friend);
    }

    public void search(View v) throws InterruptedException {
        EditText friendField = (EditText) findViewById(R.id.findFriend);
        friendName = friendField.getText().toString();

        if(friendName.length() > 3){
            Thread t = tryToConnect();
            t.setDaemon(true);
            t.start();
            t.join();
            if(!MainActivity.play) {
                Toast.makeText(this, "User not found!", Toast.LENGTH_LONG).show();

            }
            else{
                Intent intent = new Intent(this, Game.class);
                startActivity(intent);
            }
        }
        else{
            Toast.makeText(this, "Incorrect login!", Toast.LENGTH_LONG).show();
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, StartGame.class);
        startActivity(intent);
    }

    protected Thread tryToConnect(){

        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(MainActivity.out)), true);
                    pw.println(MainActivity.TRYCONNECT);
                    pw.flush();

                    String[] str = new String[3];
                    str[0] = friendName;
                    str[1] = null;
                    str[2] = "-1";
                    ObjectOutputStream os = new ObjectOutputStream(MainActivity.out);
                    os.writeObject(str);
                    os.flush();
                    Log.d("Server send " , "sfafsdasfdfadfsdgdsfgsfgfiodhafhdsjafhjkdhafjkhdjfajkdnajfnjansjknjsdnajfndjnjnfjdnjgnajdnnadsfjdnjknfskankanfdnugnfruguneunrnjdnagjnjkdnafjbndjnfandjfnjkdnjfndjnfjndsjknfjndjnfjsnjnfjknfgjksn");
                    BufferedReader brb = new BufferedReader(new InputStreamReader(MainActivity.in));
                    String answer = brb.readLine();
                    Log.d("Server answered - " , answer);
                    if(answer.equals("online")){
                        MainActivity.play = true;
                        MainActivity.firstPlayer = true;
                    }
                    else{
                        MainActivity.play = false;

                    }

                }
                catch (Exception e) {
                    Log.e("ClientActivity", "S: Error", e);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_find_friend, menu);
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
