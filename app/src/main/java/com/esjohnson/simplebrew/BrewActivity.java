package com.esjohnson.simplebrew;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by esjoh on 1/24/2016.
 * Brew Activity is built for displaying the brew instructions for various
 * brewing styles
 * <p>
 * uses the brew class to create a UI with brew instructions for each brew class
 *
 */
public class BrewActivity extends FragmentActivity {
    //setting those private variables!!
    private String[] brewSpecs;
    private TextView mTextView;
    private ProgressBar brewProgress;
    private brew brewObj;
    private brewDBHandler brewDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brew);

        //getting intent information
        brewDB = new brewDBHandler(this,null,null,1);

        Intent passedIntent = getIntent();
        int id = passedIntent.getIntExtra("id",0);
        if(id == 0){
            //error happens here
        }
        else {
            if(id==R.id.btnAeroPress) {
                //needs to be threaded to retrieve brew object from SQLite data base and parse JSON
                //default brew values need to be set in strings.xml
                brewSpecs = new String[]{"Grind: Fine", "Amount: 17 Grams", "Bloom Time: 30 Seconds", "Brew Time: 1:30"};
            }
            if(id==R.id.btnPourOver){
                //assign new values to the following brew specs
            }
        }
        //putting brew specs into list view
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,brewSpecs);
        final ListView listView = (ListView)findViewById(R.id.setupList);
        listView.setAdapter(adapter);
        mTextView = (TextView) findViewById(R.id.textView);
        Button btnStart = (Button)findViewById(R.id.btnStartBrew);
        brewProgress = (ProgressBar)findViewById(R.id.brewProgress);
        //setting onclick listener
        btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    brewTimer(30000);//time in millis! here is where we would pass both bloom and brew
                }
            }
        );
    }

    /**
     * brew countdown timer that threads
     * to be called from single button click
     * @param countdownTime amount of time in millis that the brew needs to fully complete
     */
    public void brewTimer(final long countdownTime){//this needs to include both bloom and brew in the future
        Runnable brewRun;
        Thread brewThread;
        brewRun = new Runnable() {
            long elapsedTime = countdownTime;
            long countIndex = countdownTime; //the countdown time is here!
            long remainingTime = countdownTime;
            long startTime = System.currentTimeMillis();
            int minutes = 0;
            int seconds = 0;
            @Override
            public void run() {
                //insert timer stuff here
                brewProgress.setMax((int)countIndex/1000);
                while(remainingTime > 0) {
                    elapsedTime = System.currentTimeMillis() - startTime;
                    remainingTime = countIndex-elapsedTime;
                    seconds = (int) (remainingTime / 1000);
                    minutes = seconds / 60;
                    seconds = seconds % 60;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTextView.setText(String.format("%d:%02d", minutes, seconds));
                            brewProgress.setProgress(seconds); //updating progress bar
                        }
                    });
                }
            }
        };
        brewThread = new Thread(brewRun);
        brewThread.start();
    }
}