package com.example.note;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddNoteActivity extends AppCompatActivity {

    private EditText editTitle, editContent;
    private Button buttonSave;
    private NoteDatabase noteDatabase;
    private int noteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTitle = findViewById(R.id.edit_title);
        editContent = findViewById(R.id.edit_content);
        buttonSave = findViewById(R.id.button_save);

        noteDatabase = NoteDatabase.getInstance(this);


        if (getIntent().hasExtra("note_id")) {
            noteId = getIntent().getIntExtra("note_id", -1);
            String title = getIntent().getStringExtra("note_title");
            String content = getIntent().getStringExtra("note_content");
            editTitle.setText(title);
            editContent.setText(content);
            buttonSave.setText("Zapisz zmiany");
        }

        buttonSave.setOnClickListener(v -> saveNote());
    }

    private void saveNote() {
        String title = editTitle.getText().toString().trim();
        String content = editContent.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Uzupełnij tytuł i treść!", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            if (noteId == -1) {

                Note newNote = new Note(title, content);
                noteDatabase.noteDao().insert(newNote);
            } else {

                Note updatedNote = new Note(title, content);
                updatedNote.id = noteId;
                noteDatabase.noteDao().update(updatedNote);
            }

            runOnUiThread(() -> {
                Toast.makeText(this, "Notatka zapisana", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
}
