package com.example.note;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private NoteDatabase noteDatabase;
    private NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        FloatingActionButton fab = findViewById(R.id.fab_add);

        adapter = new NoteAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(note -> {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            intent.putExtra("note_id", note.id);
            intent.putExtra("note_title", note.title);
            intent.putExtra("note_content", note.content);
            startActivity(intent);
        });


        adapter.setOnItemLongClickListener(note -> {
            new Thread(() -> {
                noteDatabase.noteDao().delete(note);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Usunięto notatkę", Toast.LENGTH_SHORT).show();
                    loadNotes();
                });
            }).start();
        });

        noteDatabase = NoteDatabase.getInstance(this);

        loadNotes();

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            startActivity(intent);
        });
    }

    private void loadNotes() {

        new Thread(() -> {
            List<Note> notes = noteDatabase.noteDao().getAllNotes();
            runOnUiThread(() -> adapter.setNotes(notes));
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }
}
