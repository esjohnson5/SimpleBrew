package com.esjohnson.simplebrew;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
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
 *
 */
public class BrewActivity extends FragmentActivity {
    //setting those private variables!!
    private TextView textTimer;
    private ProgressBar brewProgress;
    private brewDBHandler brewDB;
    private ListView specList;
    private Cursor brewTimeCursor;
    private long bloomTime;
    private brewTimer brewTimer;
    private Thread brewThread;
    private long stirTime;
    private long brewTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brew);

        brewDB = new brewDBHandler(this,null,null,1);
        specList = (ListView)findViewById(R.id.specList);
        textTimer = (TextView) findViewById(R.id.textTimer);


        //getting extras from intent
        Intent passedIntent = getIntent();
        long id = passedIntent.getLongExtra("id", 0);

        //query and threading for DB brew times
        brewTimeCursor = brewDB.getAllSpecs(id);
        Runnable brewTimeRunnable = new Runnable() {
            @Override
            public void run() {
                bloomTime = brewTimeCursor.getLong(brewTimeCursor.getColumnIndex("bloomtime"));
                stirTime = brewTimeCursor.getLong(brewTimeCursor.getColumnIndex("stirtime"));
                brewTime = brewTimeCursor.getLong(brewTimeCursor.getColumnIndex("brewtime"));
            }
        };
        Thread brewTimeThread = new Thread(brewTimeRunnable);
        brewTimeThread.start();

        //setting up cursor adapter and list view
        brewCursorAdapter specListAdapter = new brewCursorAdapter(this, brewDB.getAllSpecs(id),0);
        specList.setAdapter(specListAdapter);

        //TODO create correct total brew time
        //TODO update progress bar correctly based on bloom,stir,and brew time

        //setting buttons and progress bar

        final Button btnStart = (Button)findViewById(R.id.btnStartBrew);
        brewProgress = (ProgressBar)findViewById(R.id.brewProgress);
        brewProgress.setMax(((int)bloomTime+(int)stirTime+(int)brewTime)/1000);
        brewProgress.setProgress(brewProgress.getMax());
        //setting onclick listener
        btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnStart.setEnabled(false);
                    brewTimer = new brewTimer();
                    brewTimer.execute((int) bloomTime,(int) stirTime,(int) brewTime);//async task with countdowntimer
                    //thinking I pass in an array of integers with the three times so the progress
                    //can update properly, then the bloom+stir+brew, update progress with stir+brew+seconds
                    //as counts down, add the correct times

                }
            }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        brewTimer.cancel(true);
    }

    class brewTimer extends AsyncTask<Integer, Integer, String> {

        private long startTime = System.currentTimeMillis();
        private int minutes = 0;
        private int seconds = 0;
        private int bloom;
        private int stir;
        private int brew;
        //TODO fix this method to correctly count down!!
        @Override
        protected String doInBackground(Integer... params) {
            long elapsedTime;
            long countIndex = params[0]; //the countdown time is here!
            long remainingTime = params[0];
            bloom = params[0]/1000;
            stir = params[1]/1000;
            brew = params[2]/1000;

            while (bloom > 0) {
                elapsedTime = System.currentTimeMillis() - startTime;
                remainingTime = countIndex - elapsedTime;
                bloom = (int) remainingTime;
                seconds = (int) (remainingTime / 1000);
                minutes = seconds / 60;
                seconds = seconds % 60;
                publishProgress(seconds+stir+brew);
                if(isCancelled())
                    break;
            }
            //stir time
            //TODO
            while (stir > 0) {
                elapsedTime = System.currentTimeMillis() - startTime;
                remainingTime = countIndex - elapsedTime;
                bloom = (int) remainingTime;
                seconds = (int) (remainingTime / 1000);
                minutes = seconds / 60;
                seconds = seconds % 60;
                publishProgress(seconds+stir+brew);
                if(isCancelled())
                    break;
            }
            //brew time
            //TODO
            while (stir > 0) {
                elapsedTime = System.currentTimeMillis() - startTime;
                remainingTime = countIndex - elapsedTime;
                bloom = (int) remainingTime;
                seconds = (int) (remainingTime / 1000);
                minutes = seconds / 60;
                seconds = seconds % 60;
                publishProgress(seconds+stir+brew);
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
}




/**
 * Created by esjoh on 2/3/2016.
 * for brewActivity list population
 */
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
        textBloom.setText(String.valueOf(bloom));
        textStir.setText(String.valueOf(stir));
        textBrew.setText(String.valueOf(brew));
    }
}


