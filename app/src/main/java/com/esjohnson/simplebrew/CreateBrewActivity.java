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
public class CreateBrewActivity extends FragmentActivity {
    //vars
    brewDBHandler brewDB;
    EditText brewName;
    EditText brewGrind;
    EditText brewAmount;
    EditText brewBloom;
    EditText brewStir;
    EditText brewTime;
    Button create;
    boolean itemCreated = false;

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
        brew userBrew = new brew(brewName.getText().toString(),brewGrind.getText().toString(),
                Integer.parseInt(brewAmount.getText().toString()),Long.parseLong(brewTime.getText().toString()),
                Long.parseLong(brewBloom.getText().toString()),Long.parseLong(brewStir.getText().toString()));
        itemCreated = true;
        return userBrew;
    }
}
