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


public class SingInActivity extends ActionBarActivity {
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_sing_in);
        if (MainActivity.user != null) {
            Intent intent = new Intent(this, StartGame.class);
            startActivity(intent);
        }
    }

    String pass;
    String login;

    public void singIn(View v) {
        EditText passw = (EditText) findViewById(R.id.passField);
        EditText loginw = (EditText) findViewById(R.id.loginField);

        pass = passw.getText().toString();
        login = loginw.getText().toString();

        if (login.length() > 3 && pass.length() > 3) {
            tryToSingIn();
        } else if (login.length() <= 3) {
            Toast.makeText(this, "You input invalid login!", Toast.LENGTH_LONG).show();
        } else if (pass.length() <= 3) {
            Toast.makeText(this, "You input invalid password!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sing_in, menu);
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

    protected void tryToSingIn() {
        String[] str = new String[3];
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(MainActivity.out)), true);
            pw.println(MainActivity.SINGIN);
            pw.flush();

            str[0] = login;
            str[1] = pass;
            str[2] = "1";
            ObjectOutputStream os = new ObjectOutputStream(MainActivity.out);
            os.writeObject(str);
            os.flush();

            ObjectInputStream ois = new ObjectInputStream(MainActivity.in);
            int ch = ois.readInt();

            if (ch == 1) {
                MainActivity.user = new Authorized(login, pass, 1);
                Intent intent = new Intent(this, StartGame.class);
                startActivity(intent);
                MainActivity.user = new Authorized(login, pass, 1);
            } else if (ch == 0) {
                MainActivity.user = null;
                MainActivity.socket.close();
                MainActivity.socket = null;
                Toast.makeText(this, "This login is not registered", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.e("ClientActivity", "S: Error", e);
        }
    }
}

