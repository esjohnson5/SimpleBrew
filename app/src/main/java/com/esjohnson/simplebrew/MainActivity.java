package com.esjohnson.simplebrew;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    brewDBHandler brewDB;
    ListView brewList;
    //ArrayAdapter<String> brewListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnAeroPress = (Button) findViewById(R.id.btnAeroPress);
        final Button btnPourOver = (Button) findViewById(R.id.btnPourOver);
        final Button btnCreateBrew = (Button) findViewById(R.id.btnCreateBrew);
        brewList = (ListView) findViewById(R.id.brewList);
        brewDB = new brewDBHandler(this,null,null,1);
        //brewListAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,brewDB.getAllBrewNames());
        //brewList.setAdapter(brewListAdapter);

        //setting the onclick listeners
        btnAeroPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(view, btnAeroPress.getId());
            }
        });
        btnPourOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(view, btnPourOver.getId());
            }
        });
        btnCreateBrew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateBrewActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        //populating table
        populateBrews();
        //adding listview click listeners
        brewList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                populateBrews();
               //brewListAdapter.notifyDataSetChanged();
            }
            if(resultCode == RESULT_CANCELED){
                //did not complete form....
            }
        }
    }

    private void populateBrews(){
        brewDB = new brewDBHandler(this,null,null,1);
        String[] fromDB = new String[]{ "name" };
        int[] toView = new int[] {android.R.id.text1};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1,brewDB.getAllNames(),fromDB,toView);
        brewList = (ListView) findViewById(R.id.brewList);
        brewList.setAdapter(adapter);
    }
    //creates new intent
    public void sendMessage(View view, int id){
        Intent intent = new Intent(MainActivity.this, BrewActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
