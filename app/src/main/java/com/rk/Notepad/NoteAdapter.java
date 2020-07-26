package com.rk.Notepad;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    Context context;
    List<NoteModel> list;

    public NoteAdapter(Context context, List<NoteModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_recycler_view,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {

        String title = list.get(position).getTitle();
        String date = list.get(position).getDate();
        String time = list.get(position).getTime();
        holder.txtTitle.setText(title);
        holder.txtDate.setText(date);
        holder.txtTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtTitle, txtDate, txtTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtNoteTitle);
            txtDate = itemView.findViewById(R.id.txtNoteDate);
            txtTime = itemView.findViewById(R.id.txtNoteTime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,ShowDetail.class);
                    intent.putExtra("Id",list.get(getAdapterPosition()).getId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }
}
