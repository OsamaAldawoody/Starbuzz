package com.hfad.starbuzz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;
    Cursor favouriteCursor;
    ListView favouriteListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        AdapterView.OnItemClickListener adapter = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    Intent i = new Intent(MainActivity.this,DrinkCategoryActivity.class);
                    startActivity(i);
                }
            }
        };

        ListView listView = findViewById(R.id.options);
        listView.setOnItemClickListener(adapter);


        favouriteListView = findViewById(R.id.favourite_list);
        try{
            SQLiteOpenHelper starBuzzHelper = new StarbuzzDatabaseHelper(this);
            db = starBuzzHelper.getReadableDatabase();
            favouriteCursor = db.query("DRINK",new String[]{"_id","NAME"},"FAVOURITE = ?",
                    new String[]{Integer.toString(1)},
                    null,null,null);
            CursorAdapter cursorAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,favouriteCursor,
                    new String[]{"NAME"},new int[]{android.R.id.text1},0);
            favouriteListView.setAdapter(cursorAdapter);
        }catch (SQLiteException sq){
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
        favouriteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this,DrinkActivity.class);
                i.putExtra(DrinkActivity.TAG,(int) id);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SQLiteOpenHelper starBuzzDatabaseHelper = new StarbuzzDatabaseHelper(this);
        SQLiteDatabase db = starBuzzDatabaseHelper.getReadableDatabase();
        Cursor newCursor = db.query("DRINK",new String[]{"_id","NAME"},
                "FAVOURITE = 1",null,null,null,null);
        CursorAdapter adapter = (CursorAdapter) favouriteListView.getAdapter();
        adapter.changeCursor(newCursor);
        favouriteCursor = newCursor;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        favouriteCursor.close();
        db.close();
    }
}
