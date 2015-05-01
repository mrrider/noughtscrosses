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
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;


public class Registration extends ActionBarActivity {


    public void waiting(){
        InputStream in;
        OutputStream out;
        ObjectOutputStream oout;
        ObjectInputStream ob;
        boolean connect = false;
        try{
            in = MainActivity.socket.getInputStream();
            out = MainActivity.socket.getOutputStream();
            ob = new ObjectInputStream(in);
            oout = new ObjectOutputStream(out);
            while(true){
                connect = ob.readBoolean();
                if(connect){
                    FireMissilesDialogFragment dialog = new FireMissilesDialogFragment();
                    dialog.show(getFragmentManager(), "dialog");
                    dialog.wait(10000);
                    oout.writeBoolean(MainActivity.play);
                    oout.flush();
                    Intent intent = new Intent(this, Game.class);
                    startActivity(intent);
                }
            }
        }catch (Exception e) {
            Log.e("ClientActivity", "S: Error", e);
        }

    }

    private String name;
    private String pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(MainActivity.user==null) {

            super.onCreate(savedInstanceState);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            if(MainActivity.socket == null)
                try {
                    MainActivity.socket = new Socket(MainActivity.URL, MainActivity.PORT);
                    waiting();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            setContentView(R.layout.activity_registration);
        }else{
            Intent intent = new Intent(this, StartGame.class);
            startActivity(intent);
        }
    }

    public void registrate(View v){
        EditText loginw = (EditText) findViewById(R.id.loginRegField);
        EditText passw = (EditText) findViewById(R.id.passRegField);

        name = loginw.getText().toString();
        pass = passw.getText().toString();
        if(name.length() > 3 && pass.length() > 3) {
            tryToRegistrate();
        }
        else if(name.length() < 3){
            Toast.makeText(this, "You input too short login!", Toast.LENGTH_LONG).show();
        }
        else if(pass.length() < 3){
            Toast.makeText(this, "You input too short password!", Toast.LENGTH_LONG).show();
        }

    }

    protected void tryToRegistrate(){
        OutputStream out;
        InputStream in;
        String[] str = new String[3];
        try {
            out = MainActivity.socket.getOutputStream();
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)), true);

            pw.println(MainActivity.REGISTRATION);
            pw.flush();

            str[0] = name;
            str[1] = pass;
            str[2] = "1";

            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(str);
            os.flush();

            in = MainActivity.socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(in);
            int ch = ois.readInt();

            if (ch == 1) {
                Intent intent = new Intent(this, StartGame.class);
                startActivity(intent);
                MainActivity.user = new Authorized(name, pass, 1);
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



//    public class ClientThread implements Runnable {
//        private OutputStream out;
//        private InputStream in;
//        Authorized user = new Authorized(name, pass, online);
//        String[] str = new String[3];
//        public void run() {
//            Socket socket;
//            try {
//                Log.d("ClientActivity", "C: Connecting...");
//                socket =  new Socket(MainActivity.URL, MainActivity.PORT);
//                    try {
//                        out = socket.getOutputStream();
//                        in = socket.getInputStream();
//                        Log.d("ClientActivity", "C: Sending command.");
//                        str[0] = user.getUser();
//                        str[1] = user.getPassword();
//                        str[2] = Integer.toString(user.getOnline());
//                        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)), true);
//                        pw.println(MainActivity.REGISTRATION);
//                        pw.flush();
//
//                        ObjectOutputStream os = new ObjectOutputStream(out);
//                        os.writeObject(str);
//                        os.flush();
//
//                        ObjectInputStream ois = new ObjectInputStream(in);
//                        ch = ois.readInt();
//                        if (ch == 1) {
//                            Intent intent = new Intent(Registration.this, StartGame.class);
//                            startActivity(intent);
//                            MainActivity.user = new Authorized(name, pass, online);
//                            check = true;
//                        } else if (ch == 0) {
//                            Intent intent = new Intent(Registration.this, Registration.class);
//                            startActivity(intent);
//                            MainActivity.user = null;
//                        }
//                        Log.d("ClientActivity", "C: Sent. - " + ch);
//
//                    } catch (Exception e) {
//                        Log.e("ClientActivity", "S: Error", e);
//                    }
//
//                socket.close();
//                Log.d("ClientActivity", "C: Closed.");
//            } catch (Exception e) {
//                Log.e("ClientActivity", "C: Error", e);
//            }
//        }
//    }
}
