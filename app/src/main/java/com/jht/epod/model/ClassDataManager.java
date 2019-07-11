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

    public ClassData queryClassById(long id) {
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


    public long updateSelected(ClassData data) {
        updateData(new String[]{data.getId() + ""}, data.getSelected());
        return mClassDatabase.updateSelected(data);
    }

    public long updateSelected(String[] ids, int selected) {
        updateData(ids,selected);
        return mClassDatabase.updateSelected(ids, selected);
    }

    public long updateExerciseTime(ClassData data) {
        updateExerciseTimeData(data.getId(), data.getExerciseTime());
        return mClassDatabase.updateExerciseTime(data);
    }

    public long updateExerciseTime(long id, int time) {
        updateExerciseTimeData(id,time);
        return mClassDatabase.updateExerciseTime(String.valueOf(id), time);
    }

    private void updateData(String[] ids, int selected) {
        int sum = 0;
        int size = ids.length;
        for(ClassData data:DATA) {
            for(String id : ids) {
                int which = Integer.parseInt(id);
                if(data.getId() == which) {
                    data.setSelected(selected);
                    sum ++;
                    if(sum >= size) {
                        return;
                    }
                    break;
                }
            }
        }
    }

    private void updateExerciseTimeData(long id, int time) {
        for(ClassData data:DATA) {
            if(data.getId() == id) {
                data.setExerciseTime(time);
                break;
            }
        }
    }
}
