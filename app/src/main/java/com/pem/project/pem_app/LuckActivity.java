package com.pem.project.pem_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.View;
import android.widget.TextView;

import java.util.Random;


public class LuckActivity extends Activity {
    private TextView luckText;
    private ImageButton path1, path2, path3;
    private Button done;
    private Random r;
    private int chance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luck);

        luckText = (TextView)findViewById(R.id.luckText);
        path1 = (ImageButton)findViewById(R.id.luckPath1);
        path2 = (ImageButton)findViewById(R.id.luckPath2);
        path3 = (ImageButton)findViewById(R.id.luckPath3);
        done = (Button)findViewById(R.id.luckDone);

        View.OnClickListener askFate = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playLucky();
            }
        };

        path1.setOnClickListener(askFate);
        path2.setOnClickListener(askFate);
        path3.setOnClickListener(askFate);
    }

    public void playLucky(){
        //randomly generate result
        r = new Random();
        chance = r.nextInt(10) + 1;
            if(chance<=5){
            //50% lucky - gain of key
                luckText.setText("\nBy the wayside of the path you chose, you found a key!");

            }else if(chance<=8){
            //30% neutral - no pain & no gain
                luckText.setText("\nYou continue your way along the chosen path.");

            }else{
            //20% bad luck - loss of key
                luckText.setText("\nJust around the corner, a raging rhino appears and starts to charge after you with heavy stomps! \n\n You manage to escape up a tree just in time, but unfortunately you lost a key on the way.");
            }
        chance = 0;
        //later open result fragment with accept button to return to game screen ;)
        path1.setVisibility(View.INVISIBLE);
        path2.setVisibility(View.INVISIBLE);
        path3.setVisibility(View.INVISIBLE);
        done.setEnabled(true);
        done.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_luck, menu);
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
