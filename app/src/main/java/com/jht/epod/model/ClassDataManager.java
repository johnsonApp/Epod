package com.jht.epod.model;

import android.content.Context;

import com.jht.epod.db.ClassDatabase;

import java.util.ArrayList;
import java.util.List;

public class ClassDataManager {

    private static final String TAG = "ClassDataManager";

    private static ClassDataManager sClassDataManager;
    private static ClassDatabase mClassDatabase;

    public static List<ClassData> DATA;



    public ClassDataManager(Context context) {
        mClassDatabase = ClassDatabase.getInstance(context);
    }

    public synchronized static ClassDataManager getInstance(Context context) {
        if(null == sClassDataManager) {
            sClassDataManager = new ClassDataManager(context);
            mClassDatabase.getData();
        }
        return sClassDataManager;
    }

    public static ArrayList<ClassData> queryClassByType (int classType) {
        ArrayList<ClassData> list = new ArrayList<>();
        for(ClassData classData : DATA) {
            if(classType == classData.getClassType()){
                list.add(classData);
            }
        }
        return list;
    }

    public static ArrayList<ClassData> queryClassByDegree (int degree) {
        ArrayList<ClassData> list = new ArrayList<>();
        for(ClassData classData : DATA) {
            if(degree == classData.getDegree()){
                list.add(classData);
            }
        }
        return list;
    }

    public ClassData queryClassById(int id) {
        ClassData data = null;
        for(ClassData classData : DATA) {
            if(id == classData.getId()){
                data = classData;
                break;
            }
        }
        return data;
    }

    public static ArrayList<ClassData> queryClassSelected(int selected) {
        ArrayList<ClassData> list = new ArrayList<>();
        for(ClassData classData : DATA) {
            if(selected == classData.getSelected()){
                list.add(classData);
            }
        }
        return list;
    }

    public long updateSelected(ClassData data, int selected) {
        updateData(new String[]{data.getId() + ""},selected);
        return mClassDatabase.updateSelected(data);
    }

    public long updateSelected(String[] ids, int selected) {
        updateData(ids,selected);
        return mClassDatabase.updateSelected(ids, selected);
    }

    private void updateData(String[] ids, int selected) {
        for(ClassData data:DATA) {
            for(String id : ids) {
                int which = Integer.parseInt(id);
                if(data.getId() == which) {
                    data.setSelected(selected);
                }
            }
        }

    }
}
