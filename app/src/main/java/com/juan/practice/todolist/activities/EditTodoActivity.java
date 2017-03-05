package com.juan.practice.todolist.activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.juan.practice.todolist.R;
import com.juan.practice.todolist.db.DataBase;
import com.juan.practice.todolist.model.ToDo;

import java.util.Date;

public class EditTodoActivity extends AppCompatActivity {

    private Button saveButton;
    private Button cancelButton;
    private EditText editTodoTitle;
    private EditText editTodoNotes;
    private CheckBox checkBoxCompleted;
    private ToDo currentTodo;
    private String todoTitle;

    private View.OnClickListener saveButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            todoTitle = editTodoTitle.getText().toString();
            if(!todoTitle.isEmpty() ){
                String notes = editTodoNotes.getText().toString();

                if(currentTodo==null){
                    currentTodo = new ToDo();
                }

                currentTodo.setTitle(todoTitle);
                if( notes.isEmpty()){
                    currentTodo.setNotes("");
                }else{
                    currentTodo.setNotes(notes);
                }
                currentTodo.setCompleted(checkBoxCompleted.isChecked());
                currentTodo.setLastEdited(new Date());

                if(currentTodo.getId()==0){
                    DataBase.getInstance().insert(currentTodo);
                }else{
                    DataBase.getInstance().update(currentTodo);
                    System.out.println("TODO UPDATED");
                }


                Toast toast = Toast.makeText( getApplicationContext(), "ToDo Saved.", Toast.LENGTH_SHORT );
                toast.show();

                finish();


            }else{
                final AlertDialog.Builder alert = new AlertDialog.Builder(EditTodoActivity.this);
                alert.setTitle("Warning");
                alert.setMessage("Please, provide a title.");
                alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
            }

        }
    };

    private View.OnClickListener cancelButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast toast = Toast.makeText( getApplicationContext(), "Changes discarded.", Toast.LENGTH_SHORT );
            toast.show();
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_todo);

        saveButton = (Button) findViewById(R.id.btnSave);
        cancelButton = (Button) findViewById(R.id.btnCancel);
        editTodoTitle = (EditText) findViewById(R.id.editTodoTitle);
        editTodoNotes = (EditText) findViewById(R.id.editTodoNotes);
        checkBoxCompleted = (CheckBox) findViewById(R.id.checkCompleted);

        saveButton.setOnClickListener(saveButtonClick);
        cancelButton.setOnClickListener(cancelButtonClick);

        int todoID = getIntent().getExtras().getInt("todoID");
        if(todoID!=0){
            currentTodo = DataBase.getInstance().select(todoID);
            editTodoTitle.setText(currentTodo.getTitle());
            editTodoNotes.setText(currentTodo.getNotes());
            checkBoxCompleted.setChecked(currentTodo.isCompleted());
        }


    }


}
