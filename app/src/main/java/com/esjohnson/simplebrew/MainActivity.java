package com.esjohnson.simplebrew;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class MainActivity extends AppCompatActivity {

    brewDBHandler brewDB;
    ListView brewList;
    Button btnCreateBrew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setting UI components
        btnCreateBrew = (Button) findViewById(R.id.btnCreateBrew);
        brewList = (ListView) findViewById(R.id.brewList);
        //populates brew table from DB
        brewDB = new brewDBHandler(this,null,null,1);
        populateBrews();
        //setting the onclick listeners
        btnCreateBrew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateBrewActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        //adding listview click listeners
        brewList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent listIntent = new Intent(MainActivity.this,BrewActivity.class);
                listIntent.putExtra("id",id);
                Log.d("clicked item"," " + id);
                startActivity(listIntent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                populateBrews();
            }
            if(resultCode == RESULT_CANCELED){
                //did not complete form....
            }
        }
    }

    /**
     * populates the brew listview based on the database entries into table_brew
     */
    private void populateBrews(){
        String[] fromDB = new String[]{ "name" };
        int[] toView = new int[] {android.R.id.text1};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1,
                brewDB.getAllNames(),fromDB,toView); //need to update to custom cursor adapter
        brewList = (ListView) findViewById(R.id.brewList);
        brewList.setAdapter(adapter);
    }

    //creates new intent for button clicks
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
