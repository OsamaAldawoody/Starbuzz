package com.hfad.starbuzz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DrinkCategoryActivity extends ListActivity {

    SQLiteDatabase db;
    Cursor cursor;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new GetDrinkCategory().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent i = new Intent(DrinkCategoryActivity.this,DrinkActivity.class);
        i.putExtra(DrinkActivity.TAG,(int) id);
        startActivity(i);
    }

    private class GetDrinkCategory extends AsyncTask<Void,Void,Cursor>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listView = getListView();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            try {
                SQLiteOpenHelper starBuzzDatabaseHelper = new StarbuzzDatabaseHelper(DrinkCategoryActivity.this);

                db = starBuzzDatabaseHelper.getReadableDatabase();
                cursor = db.query("DRINK",
                        new String[]{"_id","NAME"},
                        null, null, null, null, null);

                return cursor;

            }catch (SQLiteException sq){
                Toast toast = Toast.makeText(DrinkCategoryActivity.this, "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
                return null;
            }

        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            CursorAdapter adapter = new SimpleCursorAdapter(DrinkCategoryActivity.this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"NAME"},
                    new int[]{android.R.id.text1},
                    0);

            listView.setAdapter(adapter);
        }
    }
}
