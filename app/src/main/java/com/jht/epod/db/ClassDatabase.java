package com.jht.epod.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.UiThread;
import android.util.Log;

import com.jht.epod.model.ClassData;
import com.jht.epod.model.ClassDataManager;
import com.jht.epod.utils.Utils;

import java.io.UTFDataFormatException;
import java.util.ArrayList;
import java.util.List;

public class ClassDatabase {

    private static final String TAG = "ClassDatabase";

    private static final String DB_NAME = "class.db";
    private static final int VERSION = 1;

    private static ClassDatabase sClassDatabase;
    private SQLiteDatabase mSQLiteDatabase;
    private DatabaseHelper mDatabaseHelper;

    public ClassDatabase(Context context) {
        mDatabaseHelper = new DatabaseHelper(context, DB_NAME, null, VERSION);
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
    }

    public synchronized static ClassDatabase getInstance(Context context){
        if(null == sClassDatabase){
            sClassDatabase = new ClassDatabase(context);
        }
        return sClassDatabase;
    }

    public void getData() {
        ClassDataManager.DATA = new ArrayList<>();

        Cursor cursor = mSQLiteDatabase.query(Utils.TABLE_NAME,null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do{
                ClassData temp = new ClassData();
                temp.setId(cursor.getLong(cursor.getColumnIndex(Utils.ID)));
                temp.setName(cursor.getString(cursor.getColumnIndex(Utils.NAME)));
                temp.setTime(cursor.getInt(cursor.getColumnIndex(Utils.TIME)));
                temp.setCalorie(cursor.getInt(cursor.getColumnIndex(Utils.CALORIE)));
                temp.setClassType(cursor.getInt(cursor.getColumnIndex(Utils.CLASSTYPE)));
                temp.setDegree(cursor.getInt(cursor.getColumnIndex(Utils.DEGREE)));
                temp.setIconName(cursor.getString(cursor.getColumnIndex(Utils.ICONNAME)));
                temp.setSelected(cursor.getInt(cursor.getColumnIndex(Utils.SELECTED)));

                Log.i(TAG,"getdate loading date " + temp.toString());
                ClassDataManager.DATA.add(temp);
            }while (cursor.moveToNext());

        }
        if(null != cursor) {
            cursor.close();
        }
    }

    public long insertDate(ClassData date){
        ContentValues values = new ContentValues();
        values.put(Utils.NAME, date.getName());
        values.put(Utils.TIME, date.getTime());
        values.put(Utils.CALORIE, date.getCalorie());
        values.put(Utils.DEGREE, date.getDegree());
        values.put(Utils.CLASSTYPE, date.getClassType());
        values.put(Utils.ICONNAME, date.getIconName());
        values.put(Utils.SELECTED, date.getSelected());
        long id = mSQLiteDatabase.insert(Utils.TABLE_NAME, null, values);
        date.setId(id);
        return id;
    }

    public long updateSelected(ClassData date){
        ContentValues values = new ContentValues();
        values.put(Utils.SELECTED, date.getSelected());
        return mSQLiteDatabase.update(Utils.TABLE_NAME, values, "ID = ?", new String[]{date.getId() + ""});
    }

    public long updateSelected(String[] ids, int selected){
        ContentValues values = new ContentValues();
        values.put(Utils.SELECTED, selected);
        return mSQLiteDatabase.update(Utils.TABLE_NAME, values, "ID = ?", ids);
    }


    public void initDatabase(){
        String[] name = new String[] {"收腹训练" , "肩部与手臂训练", "翘臀训练"};
        int[] type = new int[] {Utils.TYPE_CORE, Utils.TYPE_ARM, Utils.TYPE_HIP};
        String[] iconName = new String[] {"class_core_small", "class_arm_small", "class_hip_small"};
        int[] degree = new int[] {Utils.DEGREE_JUNIOR, Utils.DEGREE_MEDIUM, Utils.DEGREE_SENIOR};
        ClassData date = new ClassData();
        int length = 3;
        for(int i = 0; i < length; i ++) {
            date.setName(name[i]);
            date.setTime(10);
            date.setCalorie(80);
            date.setClassType(type[i]);
            date.setIconName(iconName[i]);
            date.setSelected(0);
            for(int j = 0; j < length; j++){
                date.setDegree(degree[j]);
                insertDate(date);
                Log.i(TAG,"initDatabase insert date " + date.toString());
            }
        }

    }

    public boolean isAnyDateInTable(){
        boolean hasDate = true;
        Cursor cursor = mSQLiteDatabase.query(Utils.TABLE_NAME,null, null, null, null, null, null);
        if(null == cursor || !cursor.moveToFirst()){
            hasDate = false;
        }
        if(null != cursor){
            cursor.close();
        }
        Log.i(TAG,"isAnyDateInTable " + hasDate);
        return hasDate;
    }
}
