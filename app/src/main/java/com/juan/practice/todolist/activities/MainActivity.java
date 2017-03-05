package com.juan.practice.todolist.activities;

import android.content.DialogInterface;
import android.content.Intent;
//import android.icu.text.SimpleDateFormat;
import java.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.juan.practice.todolist.R;
import com.juan.practice.todolist.db.DataBase;
import com.juan.practice.todolist.model.ToDo;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fabNewTodo;
    private LinearLayout scrollContent;
    private DataBase db;
    private ToDo[] currentToDos;

    private View.OnClickListener fabNewTodoClick  = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, EditTodoActivity.class);
            intent.putExtra("todoID", 0);
            startActivity(intent);
        }
    };

    private void refreshContentLayout(){
        scrollContent = (LinearLayout) findViewById(R.id.scrollContent);
        scrollContent.removeAllViews();

        db = DataBase.getInstance();
        currentToDos = db.selectAll();

        if(!(currentToDos==null)){
            for(int i = 0; i<currentToDos.length; i++){
                View myItem = getLayoutInflater().inflate(R.layout.todo_item, null);
                final int itemID = currentToDos[i].getId();


                TextView txtTitle = (TextView) myItem.findViewById(R.id.txtTitle);
                TextView txtLastUpdate = (TextView) myItem.findViewById(R.id.txtLastUpdated);

                SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
                String date = sdf.format(currentToDos[i].getLastEdited());

                txtTitle.setText( currentToDos[i].getTitle());
                txtLastUpdate.setText(date);


                Button editBtn = (Button) myItem.findViewById(R.id.editButton);
                editBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, EditTodoActivity.class);
                        intent.putExtra("todoID", itemID);
                        startActivity(intent);
                    }
                });

                Button deleteBtn = (Button) myItem.findViewById(R.id.deleteButton);
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

                        alert.setTitle("WARNING");
                        alert.setMessage("You are about to delete a ToDo! Do you want to continue?");
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.delete(itemID);

                                Toast toast = Toast.makeText( getApplicationContext(), "ToDo deleted.", Toast.LENGTH_LONG );
                                toast.show();
                                //TODO create a method that refreshes
                                refreshContentLayout();
                            }
                        });
                        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        alert.show();
                    }
                });


                scrollContent.addView(myItem);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabNewTodo = (FloatingActionButton) findViewById(R.id.fab_new_todo);
        fabNewTodo.setOnClickListener(fabNewTodoClick);

        //Just to ensure that the DB is created
        db.getInstance();
    }

    @Override
    public void onResume(){
        super.onResume();
        refreshContentLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
