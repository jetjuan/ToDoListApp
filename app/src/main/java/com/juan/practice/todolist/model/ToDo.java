package com.juan.practice.todolist.model;

import java.util.Date;

/**
 * Created by Juan on 3/1/2017.
 */

public class ToDo {
    private int id;
    private String title;
    private String notes;
    private Date lastEdited;
    private boolean completed;

    public ToDo(int id, String title, String notes, Date lastEdited) {
        this.id = id;
        this.title = title;
        this.notes = notes;
        this.lastEdited = lastEdited;
    }

    public ToDo(){
        this.id=0;
    }

    public boolean isCompleted(){
        return completed;
    }

    public void setCompleted(boolean b){
        this.completed = b;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(Date lastEdited) {
        this.lastEdited = lastEdited;
    }
}
