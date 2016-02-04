package com.esjohnson.simplebrew;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by esjoh on 2/3/2016.
 * for brewActivity list population
 */
public class brewCursorAdapter extends CursorAdapter {
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
