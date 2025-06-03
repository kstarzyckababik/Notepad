package com.example.note;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    private List<Note> notes;
    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;

    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Note note);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note note = notes.get(position);
        holder.textTitle.setText(note.title);
        holder.textContent.setText(note.content);
        holder.bind(note, clickListener, longClickListener);
    }

    @Override
    public int getItemCount() {
        return notes != null ? notes.size() : 0;
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView textTitle, textContent;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.text_title);
            textContent = itemView.findViewById(R.id.text_content);
        }

        public void bind(Note note, OnItemClickListener clickListener, OnItemLongClickListener longClickListener) {
            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onItemClick(note);
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (longClickListener != null) {
                    longClickListener.onItemLongClick(note);
                }
                return true;
            });
        }
    }
}
