package noughtscrosses.com.noughtscrosses;

import android.content.Intent;
import android.os.Bundle;
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


public class FindFriend extends ActionBarActivity {


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        waiting();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);
    }

    public void search(View v){
        EditText friendField = (EditText) findViewById(R.id.findFriend);
        String friendName = friendField.toString();

        if(friendName.length() > 3){
              tryToConnect(friendName);
//            Intent intent = new Intent(this, Game.class);
//            startActivity(intent);
        }
        else{
            Toast.makeText(this, "User not found!", Toast.LENGTH_LONG).show();
        }
    }

    protected void tryToConnect(String friendName){
//        Authorized friend = new Authorized(friendName, null, -1);
        OutputStream out;
        InputStream in;
        try {
            out = MainActivity.socket.getOutputStream();
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)), true);
            pw.println(MainActivity.TRYCONNECT);
            pw.flush();
            String[] str = new String[3];
            str[0] = friendName;
            str[1] = null;
            str[2] = "-1";
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(str);
            os.flush();

            in = MainActivity.socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(in);
            int ch = ois.readInt();
            if(ch == 0){
                Toast.makeText(this, "Friend name is incorrect or he is offline", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, StartGame.class);
                startActivity(intent);
            }
            if(ch == -1){
                Toast.makeText(this, "Wait for connection please...", Toast.LENGTH_LONG).show();
                ois = new ObjectInputStream(in);
                ch = ois.readInt();
                if(ch==1){
                    Intent intent = new Intent(this, Game.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this, "Friend dismiss your invite", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, StartGame.class);
                    startActivity(intent);
                }
            }




        }
        catch (Exception e) {
            Log.e("ClientActivity", "S: Error", e);
        }
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
