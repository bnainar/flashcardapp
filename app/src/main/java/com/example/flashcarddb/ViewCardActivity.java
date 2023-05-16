package com.example.flashcarddb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class ViewCardActivity extends AppCompatActivity {
    SQLiteDatabase db;
    String cardTitle;
    String cardBody;
    TextView viewTitle;
    TextView viewBody;
    CoordinatorLayout layout;
    Button delButton;
    Button updateButton;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_card);
        layout = (CoordinatorLayout) findViewById(R.id.viewcardlayout);
        viewTitle = (TextView) findViewById(R.id.viewtitle);
        viewBody = (TextView) findViewById(R.id.viewbody);

        Intent intent = getIntent();
        id = intent.getStringExtra(MainActivity.EXTRA_ID);
        db = openOrCreateDatabase("FlashCardDB", Context.MODE_PRIVATE, null);
        Cursor viewCursor = db.rawQuery("SELECT * FROM cards WHERE id='" + id + "'", null);
        while (viewCursor.moveToNext()) {
            cardTitle = viewCursor.getString(1);
            cardBody = viewCursor.getString(2);
            viewTitle.setText(cardTitle);
            viewBody.setText(cardBody);
        }
        viewCursor.close();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbarview);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationOnClickListener(v -> finish());

        updateButton = (Button) findViewById(R.id.updatebutton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDialog();
            }
        });

        delButton = (Button) findViewById(R.id.delbutton);
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delDialog();
            }
        });
    }


    private void updateDialog() {
//        AlertDialog.Builder ud = new AlertDialog.Builder(this);
        MaterialAlertDialogBuilder ud = new MaterialAlertDialogBuilder(this, com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered);

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.activity_update_card, null);
        ud.setView(dialogView);

        TextInputEditText titleET = (TextInputEditText) dialogView.findViewById(R.id.updateTitleEditText);
        TextInputEditText bodyET = (TextInputEditText) dialogView.findViewById(R.id.updateBodyEditText);

        titleET.setText(cardTitle);
        bodyET.setText(cardBody);

        ud.setCancelable(true);
        ud.setIcon(R.drawable.edit_48px);
        ud.setTitle("Update card");
        ud.setPositiveButton("Update", (DialogInterface.OnClickListener) (dialog, which) -> {
            String newTitle = String.valueOf(titleET.getText());
            String newBody = String.valueOf(bodyET.getText());
            if (newTitle.equals(cardTitle) && newBody.equals(cardBody)) {
                Snackbar snackbar = Snackbar.make(layout, "Change title or content to update", Snackbar.LENGTH_SHORT);
                snackbar.show();
            } else {
                try {
                    ContentValues values = new ContentValues();
                    values.put("title", newTitle);
                    values.put("body", newBody);
                    // success
                    if (db.update("cards", values, "id=" + id, null) == 1) {
                        cardTitle = newTitle;
                        cardBody = newBody;
                        viewTitle.setText(cardTitle);
                        viewBody.setText(cardBody);
                        Snackbar snackbar = Snackbar.make(layout, "Card updated successfully", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    } else {
                        Snackbar snackbar = Snackbar.make(layout, "Something went wrong", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                } catch (Exception e) {
                    Snackbar snackbar = Snackbar.make(layout, "SQL error: " + e.getMessage(), Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
            dialog.dismiss();
        });
        ud.setNegativeButton("Cancel", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });
        ud.show();
    }


    public void delDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered);
        builder.setCancelable(true);
        builder.setIcon(R.drawable.delete_48px);
        builder.setTitle("Delete card");
        builder.setMessage("Do you want to delete this card? This action is irreversible");
        builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            try {
                if (db.delete("cards", "id=" + id, null) == 1) {
                    dialog.dismiss();
                    finish();
                } else {
                    throw new Exception("Unable to delete the card");
                }
            } catch (Exception e) {
                Snackbar snackbar = Snackbar.make(layout, Objects.requireNonNull(e.getMessage()), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
            }
        });
        builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });
        builder.show();
    }
}