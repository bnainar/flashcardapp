package com.example.flashcarddb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

class CardObj {
    public String name;
    public int id;
    CardObj(int id, String name){
        this.name = name;
        this.id = id;
    }
    @NonNull
    @Override
    public String toString() {
        return name;
    }
}

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.example.flashcarddb.EXTRA_ID";
    SQLiteDatabase db;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void openAddActivity() {
        Intent intent = new Intent(this, AddCardActivity.class);
        startActivity(intent);
    }

    private void openViewActivity(String id) {
        Intent intent = new Intent(this, ViewCardActivity.class);
        intent.putExtra(EXTRA_ID, id);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        ExtendedFloatingActionButton fab = (ExtendedFloatingActionButton) findViewById(R.id.addCardEfab);
        fab.setOnClickListener(v -> openAddActivity());

        listview = (ListView) findViewById(R.id.list_view);

        db = openOrCreateDatabase("FlashCardDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS cards(id INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR NOT NULL, body VARCHAR NOT NULL);");
//        db.execSQL("DELETE FROM cards");
//        db.execSQL("INSERT INTO cards(title, body) VALUES ('What is the definition of \"ephemeral\"?', '\"Ephemeral\" refers to something that lasts for a short period of time.');");
//        db.execSQL("INSERT INTO cards(title, body) VALUES ('What year did World War II end?', 'World War II ended in 1945.');");
//        db.execSQL("INSERT INTO cards(title, body) VALUES ('Name the largest desert in the world.', 'The largest desert in the world is the Sahara Desert.');");
//        db.execSQL("INSERT INTO cards(title, body) VALUES ('Who is the protagonist in the novel \"To Kill a Mockingbird\"?', ' The protagonist is Jean Louise \"Scout\" Finch.');");
//        db.execSQL("INSERT INTO cards(title, body) VALUES ('Name the process by which plants convert sunlight into energy.', 'The process is called photosynthesis.');");
//        db.execSQL("INSERT INTO cards(title, body) VALUES ('Define the word \"exacerbate.\"', '\"Exacerbate\" means to make a situation or problem worse.');");
        showAllCards();
    }

    public void showAllCards() {
        Cursor c = db.rawQuery("SELECT id, title FROM cards", null);
        if (c.getCount() == 0) {
            showMessage("Error", "No cards found");
            return;
        }
        ArrayList<CardObj> objarr = new ArrayList<>();
//        StringBuilder buffer=new StringBuilder();
        while (c.moveToNext()) {
            CardObj cardobj = new CardObj(Integer.parseInt(c.getString(0)), c.getString(1));
            objarr.add(cardobj);
        }
//        showMessage("All cards", buffer.toString());
//        String[] titlesArray = new String[titles.size()];
//        titles.toArray(titlesArray);
//        showMessage("Array", titlesArray[0]);
        ArrayAdapter<CardObj> adapter = new ArrayAdapter<>(this, R.layout.activity_list_view, R.id.textView, objarr);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener((parent, view, pos, id) -> {
//                showMessage("Clicked", "item clicked is id:" + objarr.get(pos).id+"  name: " + objarr.get(pos).name);
            openViewActivity(String.valueOf(objarr.get(pos).id));
        });
        c.close();
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//        return true;
//    }
}