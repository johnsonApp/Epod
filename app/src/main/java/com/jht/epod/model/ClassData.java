package com.jht.epod.model;

public class ClassData {

    private long id;
    private String name;
    private int time;
    private int calorie;
    private int degree;
    private int classType;
    private String iconName;
    private int exerciseTime;
    private int selected;

    public String toString() {
        return "ClassDate ( " +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "time = " + time + ", " +
                "calorie = " + calorie + ", " +
                "degree = " + degree + ", " +
                "classType = " + classType + ", " +
                "iconName = " + iconName + ", " +
                "exerciseTime = " + exerciseTime + ", " +
                "selected = " + selected + ")";
    }

    public ClassData(){
        this.id = -1;
    }

    public ClassData(long id, String name, int time, int calorie, int degree,
                     int classType, String iconName,int exerciseTime, int selected) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.calorie = calorie;
        this.degree = degree;
        this.classType = classType;
        this.iconName = iconName;
        this.exerciseTime = exerciseTime;
        this.selected = selected;
    }

    public void setDate(ClassData classDate) {
        this.id = classDate.id;
        this.name = classDate.name;
        this.time = classDate.time;
        this.calorie = classDate.calorie;
        this.degree = classDate.degree;
        this.classType = classDate.classType;
        this.iconName = classDate.iconName;
        this.exerciseTime = classDate.exerciseTime;
        this.selected = classDate.selected;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String  name) {
        this.name = name;
    }

    public int getTime() { return time; }

    public void setTime(int time) { this.time = time; }

    public int getCalorie() {return calorie; }

    public void setCalorie(int calorie) { this.calorie = calorie; }

    public int getDegree() { return degree; }

    public void setDegree(int degree) { this.degree = degree; }

    public int getClassType() { return classType; }

    public void setClassType(int classType) { this.classType = classType; }

    public String getIconName() { return iconName; }

    public void setIconName(String iconName) { this.iconName = iconName; }

    public int getExerciseTime() { return exerciseTime; }

    public void setExerciseTime(int exerciseTime) { this.exerciseTime = exerciseTime; }

    public int getSelected() { return selected; }

    public void setSelected(int selected) { this.selected = selected; }
}
