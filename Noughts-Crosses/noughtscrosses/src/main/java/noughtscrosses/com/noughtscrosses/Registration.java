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

import java.io.BufferedWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;


public class Registration extends ActionBarActivity {

    private String name;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_registration);

        if(MainActivity.user!=null) {
            Intent intent = new Intent(this, StartGame.class);
            startActivity(intent);
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void registrate(View v){
        EditText loginw = (EditText) findViewById(R.id.loginRegField);
        EditText passw = (EditText) findViewById(R.id.passRegField);

        name = loginw.getText().toString();
        pass = passw.getText().toString();

        if(name.length() > 3 && pass.length() > 3) {
            tryToRegistrate();
        }
        else if(name.length() <= 3){
            Toast.makeText(this, "You input too short login!", Toast.LENGTH_LONG).show();
        }
        else if(pass.length() <= 3){
            Toast.makeText(this, "You input too short password!", Toast.LENGTH_LONG).show();
        }

    }

    protected void tryToRegistrate(){
        String[] str = new String[3];
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(MainActivity.out)), true);
            pw.println(MainActivity.REGISTRATION);
            pw.flush();

            str[0] = name;
            str[1] = pass;
            str[2] = "1";

            ObjectOutputStream os = new ObjectOutputStream(MainActivity.out);
            os.writeObject(str);
            os.flush();

            ObjectInputStream ois = new ObjectInputStream(MainActivity.in);
            int ch = ois.readInt();

            if (ch == 1) {
                MainActivity.user = new Authorized(name, pass, 1);
                Intent intent = new Intent(this, StartGame.class);
                startActivity(intent);
            } else if (ch == 0) {
                Toast.makeText(this, "This login is already in use", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, Registration.class);
                startActivity(intent);
                MainActivity.user = null;
                MainActivity.socket.close();
                MainActivity.socket = null;
            }

        } catch (Exception e) {
            Log.e("ClientActivity", "S: Error", e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
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
