package noughtscrosses.com.noughtscrosses;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;


public class Game extends ActionBarActivity {
    private int counter = 1;
    private boolean tl = false;
    private boolean tc = false;
    private boolean tr = false;
    private boolean cl = false;
    private boolean cc = false;
    private boolean cr = false;
    private boolean bl = false;
    private boolean bc = false;
    private boolean br = false;
    private boolean win = false;
    private boolean turn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_game);




            if (MainActivity.firstPlayer) {
                TextView vw = (TextView) findViewById(R.id.turn);
                vw.setText("Your turn");
                turn = true;

            } else {
                TextView vw = (TextView) findViewById(R.id.turn);
                vw.setText("Oponent turn");
            }
            check();


    }


    public void check(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                    BufferedReader br;
                    try {
                        br = new BufferedReader(new InputStreamReader(MainActivity.in));
                        while(!win){

                            String opr = br.readLine();

                            switch (opr) {
                                case "tl":
                                    counter++;
                                    tl = true;
                                    turn = true;
                                    Log.d("turn", Boolean.toString(turn));
                                    final ImageButton im = (ImageButton) findViewById(R.id.tl);
                                    im.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(counter%2!=0)
                                                im.setBackgroundResource(R.drawable.cross);
                                            else
                                                im.setBackgroundResource(R.drawable.nough);
                                        }
                                    });

                                    final TextView trn = (TextView) findViewById(R.id.turn);
                                    trn.post(new Runnable() {
                                        @Override
                                        public void run() {
                                                trn.setText("Your turn");
                                        }
                                    });
                                    break;
                                case "tc":
                                    counter++;
                                    tc = true;
                                    turn = true;
                                    Log.d("turn", Boolean.toString(turn));
                                    final ImageButton imtc = (ImageButton) findViewById(R.id.tc);
                                    imtc.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(counter%2!=0)
                                                imtc.setBackgroundResource(R.drawable.cross);
                                            else
                                                imtc.setBackgroundResource(R.drawable.nough);
                                        }
                                    });

                                    final TextView trntc = (TextView) findViewById(R.id.turn);
                                    trntc.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            trntc.setText("Your turn");
                                        }
                                    });
                                    break;
                                default:
                                    break;
                            }
                        }
                    } catch (Exception e) {
                        Log.e("ClientActivity", "S: Error", e);
                    }
            }
        }).start();
    }



    public void tlC(View v){
        if(!tl && turn){
            ImageButton im = (ImageButton) findViewById(R.id.tl);
            if(counter%2==0)
                im.setBackgroundResource(R.drawable.cross);
            else
                im.setBackgroundResource(R.drawable.nough);
            counter++;
            tl = true;
            turn = false;
            TextView vw = (TextView) findViewById(R.id.turn);
            vw.setText("Oponent turn");
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(MainActivity.out)), true);
            pw.println(MainActivity.GAME);
            pw.flush();
            pw.println("tl");
            pw.flush();
        }

    }

    public void tcC(View v){
        if(!tc && turn){
            ImageButton im = (ImageButton) findViewById(R.id.tc);
            if(counter%2==0)
                im.setBackgroundResource(R.drawable.cross);
            else
                im.setBackgroundResource(R.drawable.nough);
            counter++;
            tc = true;
            turn = false;
            TextView vw = (TextView) findViewById(R.id.turn);
            vw.setText("Oponent turn");
            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(MainActivity.out)), true);
            pw.println(MainActivity.GAME);
            pw.flush();
            pw.println("tc");
            pw.flush();
        }

    }


//    public void trC(View v){
//        if(!tr && turn){
//            ImageButton im = (ImageButton) findViewById(R.id.tr);
//            if(counter%2==0)
//                im.setBackgroundResource(R.drawable.cross);
//            else
//                im.setBackgroundResource(R.drawable.nough);
//            counter++;
//            tr = true;
//            turn = false;
//            TextView vw = (TextView) findViewById(R.id.turn);
//            vw.setText("Oponent turn");
//            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(MainActivity.out)), true);
//            pw.println(MainActivity.GAME);
//            pw.flush();
//            pw.println("tl");
//            pw.flush();
//        }
//
//    }
//
//    public void clC(View v){
//        if(!cl && turn){
//            ImageButton im = (ImageButton) findViewById(R.id.cl);
//            if(counter%2==0)
//                im.setBackgroundResource(R.drawable.cross);
//            else
//                im.setBackgroundResource(R.drawable.nough);
//            counter++;
//            cl = true;
//            turn = false;
//            TextView vw = (TextView) findViewById(R.id.turn);
//            vw.setText("Oponent turn");
//            PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(MainActivity.out)), true);
//            pw.println(MainActivity.GAME);
//            pw.flush();
//            pw.println("tc");
//            pw.flush();
//        }
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
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
