package com.esjohnson.simplebrew;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by esjoh on 1/29/2016.
 * activity to create your own brew which is stored locally in the brewDB
 *
 */
    //TODO create proper layout eventually
    //TODO add brew apparatus selection based on apparatus in DB

public class CreateBrewActivity extends FragmentActivity {
    final static int SECONDS_2_MILLIS = 1000;

    //text fields
    brewDBHandler brewDB;
    EditText brewName;
    EditText brewGrind;
    EditText brewAmount;
    EditText brewBloom;
    EditText brewStir;
    EditText brewTime;
    Button create;
    boolean itemCreated = false;

    //number vars
    int amount;
    long bloom;
    long stir;
    long brew;
    String name;
    String grind;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        //assigning all the buttons and DB
        brewName = (EditText) findViewById(R.id.brewName);
        brewGrind = (EditText) findViewById(R.id.brewGrind);
        brewAmount = (EditText) findViewById(R.id.brewAmount);
        brewBloom = (EditText) findViewById(R.id.brewBloom);
        brewStir = (EditText) findViewById(R.id.brewStir);
        brewTime = (EditText) findViewById(R.id.brewTime);
        create = (Button) findViewById(R.id.btnCreateBrew);
        brewDB = new brewDBHandler(this,null,null,1);
        //adding onclick for add button
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable brewRun; //threading the DB add and object creation
                Thread brewThread;
                brewRun = new Runnable() {
                    @Override
                    public void run() {
                        name = brewName.getText().toString();
                        grind = brewGrind.getText().toString();
                        amount = Integer.parseInt(brewAmount.getText().toString());
                        brew = Long.parseLong(brewTime.getText().toString())*SECONDS_2_MILLIS;
                        bloom = Long.parseLong(brewBloom.getText().toString())*SECONDS_2_MILLIS;
                        stir = Long.parseLong(brewStir.getText().toString())*SECONDS_2_MILLIS;
                        brewDB.addBrew(createBrewObj());
                        sendBack();
                    }
                };
                brewThread = new Thread(brewRun);
                brewThread.start();
            }
        });
    }

    /**
     * sends back result to main activity when brew has been completed
     */
    private void sendBack(){
        Intent returnIntent = getIntent();
        if(itemCreated) {
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
        if(!itemCreated) {
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
    }

    /**
     * creates a new brew object
     * @return brewObj to be sent to the DB
     */
    private brew createBrewObj(){
        brew userBrew = new brew(name,grind,amount,brew,bloom,stir);
        itemCreated = true;
        return userBrew;
    }
}
