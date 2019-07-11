package com.jht.epod;

import android.app.Application;

import com.jht.epod.db.ClassDatabase;
import com.jht.epod.model.ClassDataManager;

public class EpodApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        ClassDatabase database = ClassDatabase.getInstance(this);
        if(!database.isAnyDateInTable()) {
            database.initDatabase();
        }
        ClassDataManager manager = ClassDataManager.getInstance(this);
    }
}
