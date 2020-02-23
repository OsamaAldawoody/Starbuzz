package com.hfad.starbuzz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DrinkActivity extends AppCompatActivity {

    ImageView imageDrink;
    TextView nameDrink;
    TextView descriptionDrink;
    CheckBox favouriteCheckBox;

    int id;
    public static final String TAG = "DrinkActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id = (int)getIntent().getExtras().get(TAG);

        new GetDrinks().execute(id);

//        favouriteCheckBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                new UpdateDrink().execute(id);
//            }
//        });

    }

//    private class UpdateDrink extends AsyncTask<Integer,Void,Boolean>{
//        ContentValues favourite;
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            favourite = new ContentValues();
//            favourite.put("FAVOURITE", favouriteCheckBox.isChecked());
//        }
//
//        @Override
//        protected Boolean doInBackground(Integer... drinks) {
//            int drinkId = drinks[0];
//            try{
//                SQLiteOpenHelper starBuzzHelper = new StarbuzzDatabaseHelper(DrinkActivity.this);
//                SQLiteDatabase db = starBuzzHelper.getWritableDatabase();
//                db.update("DRINK",
//                        favourite,
//                        "_id = ?",
//                        new String[]{Integer.toString(drinkId)});
//                db.close();
//                return true;
//            }catch (SQLiteException sq){
//                return false;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Boolean success) {
//            super.onPostExecute(success);
//            if (!success){
//                Toast.makeText(DrinkActivity.this, "Failed Database", Toast.LENGTH_SHORT).show();
//            }else {
//                Toast.makeText(DrinkActivity.this, "Succeed", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    private class GetDrinks extends AsyncTask<Integer,Void,Cursor>{
        SQLiteDatabase db;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Cursor doInBackground(Integer... integers) {
            int id = integers[0];

            try {
                SQLiteOpenHelper starBuzzDatabaseHelper = new StarbuzzDatabaseHelper(DrinkActivity.this);
                db = starBuzzDatabaseHelper.getReadableDatabase();
                Cursor cursor = db.query("DRINK",
                        new String[]{"NAME", "DESCRIPTION", "IMAGE_RESOURCE_ID", "FAVOURITE"},
                        "_id = ?",
                        new String[]{Integer.toString(id)},
                        null, null, null, null);

                return cursor;
            }catch (SQLiteException sq){
                Toast.makeText(DrinkActivity.this, "Empty cursor and Failed Database", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            if (cursor.moveToFirst()) {
                String drinkName = cursor.getString(0);
                String drinkDescription = cursor.getString(1);
                int drinkImage = cursor.getInt(2);
                boolean isFavourite = cursor.getInt(3) == 1;

                imageDrink = (ImageView) findViewById(R.id.drink_image);
                imageDrink.setImageResource(drinkImage);

                nameDrink = (TextView) findViewById(R.id.drink_name);
                nameDrink.setText(drinkName);

                descriptionDrink = (TextView) findViewById(R.id.drink_description);
                descriptionDrink.setText(drinkDescription);

                favouriteCheckBox = (CheckBox) findViewById(R.id.favourite_checkbox);
                favouriteCheckBox.setChecked(isFavourite);

                cursor.close();
                db.close();
            }
            Toast.makeText(DrinkActivity.this, "Succeed", Toast.LENGTH_SHORT).show();

        }
    }

}
