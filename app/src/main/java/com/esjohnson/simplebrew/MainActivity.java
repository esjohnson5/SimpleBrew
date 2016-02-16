package com.esjohnson.simplebrew;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    brewDBHandler brewDB;
    ListView brewList;
    Button btnCreateBrew;
    brewCursorAdapter mainBrewListAdapter;
    OnSwipeTouchListener swipeListener;
    Cursor brewListCursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setting UI components
        btnCreateBrew = (Button) findViewById(R.id.btnCreateBrew);
        brewList = (ListView) findViewById(R.id.brewList);
        swipeListener = new OnSwipeTouchListener(this,brewList);

        //populates brew table from DB
        brewDB = new brewDBHandler(this,null,null,1);
        //populateBrews();
        brewListCursor = brewDB.getAllNames();
        mainBrewListAdapter = new brewCursorAdapter(this,brewListCursor,0);
        brewList.setAdapter(mainBrewListAdapter);
        brewList.setOnTouchListener(swipeListener);

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
                mainBrewListAdapter.changeCursor(brewDB.getAllNames());
            }
            if(resultCode == RESULT_CANCELED){
                //did not complete form....
            }
        }
    }
    //TODO use view holder here

    class brewCursorAdapter extends CursorAdapter {
        public brewCursorAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.main_brew_list, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            //Setting Text Field
            TextView textName = (TextView) view.findViewById(R.id.textName);
            String name = cursor.getString(cursor.getColumnIndex("name"));
            textName.setText(name);
            //deleting item upon button click
            final int _id = cursor.getInt(cursor.getColumnIndex("_id"));
            Button btnDelete = (Button) view.findViewById(R.id.btnDelete);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //delete method here.....
                    Log.d("click","clicked");
                    brewDB.deleteBrewById(_id);
                    mainBrewListAdapter.changeCursor(brewDB.getAllNames());
                }
            });

        }
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
