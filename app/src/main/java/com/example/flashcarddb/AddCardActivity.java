package com.example.flashcarddb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

public class AddCardActivity extends AppCompatActivity {
    CoordinatorLayout layout;
    EditText titleET;
    EditText bodyET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarcreate);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationOnClickListener(v -> {
            // back button pressed
//                showMessage("Back Clicked", "");
            finish();
        });

        titleET = (EditText) findViewById(R.id.editTextTitle);
        bodyET = (EditText) findViewById(R.id.editTextContent);
        layout = (CoordinatorLayout) findViewById(R.id.addcardlayout);

        Button createButton = (Button) findViewById(R.id.addbutton);
        createButton.setOnClickListener(v -> {
            String title = titleET.getText().toString();
            String body = bodyET.getText().toString();
            if(title.trim().equals("") ||  body.trim().equals("")){
                Snackbar snackbar = Snackbar.make(layout, "Title and Body should not be empty", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
            else{
                try {
                    SQLiteDatabase db = openOrCreateDatabase("FlashCardDB", Context.MODE_PRIVATE, null);
                    ContentValues values = new ContentValues();
                    values.put("title", title);
                    values.put("body", body);
                    db.insert("cards", null, values);
                    db.close();
                    finish();
                } catch (Exception e) {
                    showMessage("SQL error", e.getMessage());
                }
            }

        });
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}