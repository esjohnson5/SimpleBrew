package com.esjohnson.simplebrew;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by esjoh on 1/24/2016.
 * Brew Activity is built for displaying the brew instructions for various
 * brewing styles
 * <p>
 * uses the brew class to create a UI with brew instructions for each brew class
 */
public class BrewActivity extends FragmentActivity {
    //setting those private variables!!
    private TextView textTimer;
    private ProgressBar brewProgress;
    private brewDBHandler brewDB;
    private ListView specList;
    private Cursor brewTimeCursor;
    private brewTimer brewTimer;
    private Button btnStart;
    private long bloomTime;
    private long stirTime;
    private long brewTime;
    private boolean brewClicked=false;

    final static int MILLIS_2_SECONDS = 1000;

    //TODO dynamic text field in timer that changes based on progress of brew
    //TODO center time in progress bar
    //TODO fix brew specs display, change from list view to...something better

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brew);

        //getting extras from intent
        Intent passedIntent = getIntent();
        long id = passedIntent.getLongExtra("id", 0);

        //creating all private vars
        brewDB = new brewDBHandler(this,null,null,1);
        specList = (ListView)findViewById(R.id.specList);
        textTimer = (TextView) findViewById(R.id.textTimer);
        brewTimeCursor = brewDB.getBrewRowById(id);
        brewProgress = (ProgressBar)findViewById(R.id.brewProgress);
        btnStart = (Button)findViewById(R.id.btnStartBrew);

        //query and threading for DB brew times
        getBrewTimerSpecs();

        brewCursorAdapter specListAdapter = new brewCursorAdapter(this, brewDB.getBrewRowById(id),0);
        specList.setAdapter(specListAdapter);

        //setting onclick listener
        btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnStart.setEnabled(false);
                    brewProgress.setMax(((int) bloomTime + (int) stirTime + (int) brewTime)/1000);
                    brewProgress.setProgress(((int) bloomTime + (int) stirTime + (int) brewTime)/1000);
                    brewTimer = new brewTimer();
                    brewTimer.execute((int) bloomTime,(int) stirTime,(int) brewTime);
                    brewClicked=true;
                }
            }
        );
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(brewClicked) {
            brewTimer.cancel(true);
        }
    }

    private void getBrewTimerSpecs(){
        Runnable brewTimeRunnable = new Runnable() {
            @Override
            public void run() {
                bloomTime = brewTimeCursor.getLong(brewTimeCursor.getColumnIndex("bloomtime"));
                stirTime = brewTimeCursor.getLong(brewTimeCursor.getColumnIndex("stirtime"));
                brewTime = brewTimeCursor.getLong(brewTimeCursor.getColumnIndex("brewtime"));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textTimer.setText(String.format("%d:%02d", ((bloomTime/1000)/60), (bloomTime/1000)));
                    }
                });
            }
        };
        Thread brewTimeThread = new Thread(brewTimeRunnable);
        brewTimeThread.start();
    }

    /**
     * Async Task used to create custom brew timer
     * brew timer integrates bloom,stir,and brew time into one seamless timer
     * that updates a total brew time progress bar
     */
    class brewTimer extends AsyncTask<Integer, Integer, String> {

        private long startTime = System.currentTimeMillis();
        private int minutes = 0;
        private int seconds = 0;
        private int bloom;
        private int stir;
        private int brew;

        @Override
        protected String doInBackground(Integer... params) {
            long elapsedTime;
            long countIndex = params[0]; //the countdown time is here!
            long remainingTime;
            bloom = params[0]/1000;
            stir = params[1]/1000;
            brew = params[2]/1000;
            //bloom time
            while (bloom > 0) {
                elapsedTime = System.currentTimeMillis() - startTime;
                remainingTime = countIndex - elapsedTime;
                bloom = (int) remainingTime;
                seconds = (int) (remainingTime / 1000);
                minutes = seconds / 60;
                seconds = seconds % 60;
                publishProgress(bloom/1000+stir+brew);
                if(isCancelled())
                    break;
            }
            //stir time
            countIndex=stir*1000;
            startTime=System.currentTimeMillis();
            while (stir > 0) {
                elapsedTime = System.currentTimeMillis() - startTime;
                remainingTime = countIndex - elapsedTime;
                stir = (int) remainingTime;
                seconds = (int) (remainingTime / 1000);
                minutes = seconds / 60;
                seconds = seconds % 60;
                publishProgress(stir/1000+brew);
                if(isCancelled())
                    break;
            }
            //brew time
            countIndex=brew*1000;
            startTime=System.currentTimeMillis();
            while (brew > 0) {
                elapsedTime = System.currentTimeMillis() - startTime;
                remainingTime = countIndex - elapsedTime;
                brew = (int) remainingTime;
                seconds = (int) (remainingTime / 1000);
                minutes = seconds / 60;
                seconds = seconds % 60;
                publishProgress(brew/1000);
                if(isCancelled())
                    break;
            }
            return "timer done";
        }
        @Override
        protected void onCancelled(){
        }

        @Override
        protected void onProgressUpdate (Integer...values){
            textTimer.setText(String.format("%d:%02d", minutes, seconds));
            brewProgress.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute (String s){
            textTimer.setText("done");
        }
    }

    /**
     * Created by esjoh on 2/3/2016.
     * Cursor Adapter for brewActivity list population!
     */

    //TODO use view holder here

    class brewCursorAdapter extends CursorAdapter {
        public brewCursorAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.brew_spec_layout, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView textName = (TextView) view.findViewById(R.id.textName);
            TextView textBloom = (TextView) view.findViewById(R.id.textBloom);
            TextView textStir = (TextView) view.findViewById(R.id.textStir);
            TextView textBrew = (TextView) view.findViewById(R.id.textBrew);

            String name = cursor.getString(cursor.getColumnIndex("name"));
            long bloom = cursor.getLong(cursor.getColumnIndex("bloomtime"));
            long stir = cursor.getLong(cursor.getColumnIndex("stirtime"));
            long brew = cursor.getLong(cursor.getColumnIndex("brewtime"));

            textName.setText(name);
            textBloom.setText(String.valueOf(bloom/MILLIS_2_SECONDS));
            textStir.setText(String.valueOf(stir/MILLIS_2_SECONDS));
            textBrew.setText(String.valueOf(brew/MILLIS_2_SECONDS));
        }
    }

}



