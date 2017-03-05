package com.juan.practice.todolist.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import com.juan.practice.todolist.model.ToDo;
import java.io.File;
import java.util.Date;


/**
 * Created by Juan on 3/2/2017.
 */

public class DataBase {

    private SQLiteDatabase dataBase;
    private String dbFileName = "dbToDo";
    private String dbPath = "/data/data/com.juan.practice.todolist/databases/";
    private String dbPathFull = dbPath+dbFileName;

    private static DataBase  instance = null;

    protected DataBase(){

        createDB();
    }

    public static DataBase getInstance(){
        if(instance == null){
            instance = new DataBase();
        }
        return instance;
    }

    private void createDB(){
        if(!dbExists(dbPathFull)){
            //Ensuring that database folder is created to avoid runtime exceptions/errors
            new File(dbPath).mkdir();

            dataBase = SQLiteDatabase.openOrCreateDatabase(dbPathFull, null);
            dataBase.execSQL("CREATE TABLE todo "+
                            " (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, notes TEXT,"+
                            " lastEdited NUMERIC, completed NUMERIC  );");
            dataBase.close();


        }
    }

    private boolean dbExists(String path){
        return new File(path).exists();
    }

    public boolean insert(ToDo todo){
        boolean success;
        dataBase = SQLiteDatabase.openDatabase(dbPathFull, null, SQLiteDatabase.OPEN_READWRITE);

        ContentValues cv = new ContentValues();
        cv.put("title", todo.getTitle());
        cv.put("notes", todo.getNotes());
        cv.put("lastEdited", todo.getLastEdited().getTime());
        cv.put("completed", todo.isCompleted());

        long insert =  dataBase.insert("todo", "_id", cv);

        if(insert != -1){
            success = true;
        }else{
            success = false;
        }

        dataBase.close();


        return success;
    }

    public ToDo[] selectAll(){
        dataBase = SQLiteDatabase.openDatabase(dbPathFull, null, SQLiteDatabase.OPEN_READONLY);

        Cursor dbQuery = dataBase.rawQuery("SELECT * FROM todo", null);
        dbQuery.moveToFirst();

        ToDo[] items = {};

        if(dbQuery.getCount() > 0){

            items = new ToDo[dbQuery.getCount()];

            do{
                ToDo todo = new ToDo();
                todo.setId(dbQuery.getInt(dbQuery.getColumnIndex("_id")));
                todo.setTitle(dbQuery.getString(dbQuery.getColumnIndex("title")));
                todo.setLastEdited(new Date(dbQuery.getLong(dbQuery.getColumnIndex("lastEdited"))));
                todo.setNotes(dbQuery.getString(dbQuery.getColumnIndex("notes")));
                if(dbQuery.getInt(dbQuery.getColumnIndex("completed")) == 0 ){
                    todo.setCompleted(false);
                }else{
                    todo.setCompleted(true);
                }

                items[dbQuery.getPosition()] = todo;
            }
            while(dbQuery.moveToNext());
        }


        System.out.println("TOTAL AMOUNT OF ROWS: "+ dbQuery.getCount());

        dbQuery.close();
        return items;
    }

    public ToDo select(int id){
        dataBase = SQLiteDatabase.openDatabase(dbPathFull, null, SQLiteDatabase.OPEN_READONLY);

        Cursor dbQuery = dataBase.rawQuery("SELECT * FROM todo where _id = " + id, null);
        dbQuery.moveToFirst();

        ToDo item = null;

        if(dbQuery.getCount() > 0){

            item = new ToDo();

                item.setId(dbQuery.getInt(dbQuery.getColumnIndex("_id")));
                item.setTitle(dbQuery.getString(dbQuery.getColumnIndex("title")));
                item.setNotes(dbQuery.getString(dbQuery.getColumnIndex("notes")));
                item.setLastEdited(new Date(dbQuery.getLong(dbQuery.getColumnIndex("lastEdited"))));
                if(dbQuery.getInt(dbQuery.getColumnIndex("completed")) == 0 ){
                    item.setCompleted(false);
                }else{
                    item.setCompleted(true);
                }
            }

        return item;
    }

    public int update(ToDo todo){


        dataBase = SQLiteDatabase.openDatabase(dbPathFull, null, SQLiteDatabase.OPEN_READWRITE);

        ContentValues cv = new ContentValues();
        cv.put("title", todo.getTitle());
        cv.put("notes", todo.getNotes());
        cv.put("lastEdited", todo.getLastEdited().getTime());
        cv.put("completed", todo.isCompleted());

        int result = dataBase.update("todo", cv, "_id = "+ todo.getId(), null );
        dataBase.close();


        return result;
    };

    public int delete(int id){
        dataBase = SQLiteDatabase.openDatabase(dbPathFull, null, SQLiteDatabase.OPEN_READWRITE);
        int result = dataBase.delete("todo", " _id = "+ id, null);
        dataBase.close();

        return result;
    }
}
