package com.hfad.starbuzz;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class StarbuzzDatabaseHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "starbuzz";
    private static int DB_VERSION = 2;


    public StarbuzzDatabaseHelper( Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db,0,DB_VERSION);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db,oldVersion,newVersion);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }


    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion){

        if (newVersion == 2){
            db.execSQL("DROP TABLE DRINK");
            db.execSQL("CREATE TABLE DRINK ("
                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT, "
                    + "DESCRIPTION TEXT, "
                    + "IMAGE_RESOURCE_ID INTEGER);");
            db.execSQL("ALTER TABLE DRINK ADD COLUMN FAVOURITE NUMERIC;");
            createDrink(db,"latte","A couple of espresso shots with steamed milk",R.drawable.latte);
            createDrink(db,"Cappuccino","Espresso, hot milk, and a steamed milk foam",R.drawable.filter);
            createDrink(db,"Filter","Highest quality beans roasted and brewed fresh",R.drawable.filter);

        }
    }

    private static void createDrink(SQLiteDatabase db,String name,String description,int image_resource){
        ContentValues latte = new ContentValues();
        latte.put("NAME",name);
        latte.put("DESCRIPTION",description);
        latte.put("IMAGE_RESOURCE_ID",image_resource);

        db.insert("DRINK",null,latte);

    }
}
