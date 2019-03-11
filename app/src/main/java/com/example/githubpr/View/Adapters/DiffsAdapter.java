package com.example.githubpr.View.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.githubpr.R;

import java.util.List;

import io.reflectoring.diffparser.api.model.Diff;
import io.reflectoring.diffparser.api.model.Hunk;

public class DiffsAdapter extends RecyclerView.Adapter<DiffsAdapter.DiffsHolder> {

    public Context context;
    List<Diff> diffslist;

    public DiffsAdapter(Context context, List<Diff> diffslist){
        this.context = context;
        this.diffslist = diffslist;
    }

    @Override
    public DiffsAdapter.DiffsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.diff_layout,viewGroup,false);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new DiffsHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull DiffsAdapter.DiffsHolder diffsHolder, int i) {
        Diff diff = diffslist.get(i);
        diffsHolder.from_tv.setText("File Name : " +diff.getFromFileName().split("/")[1]);
        //diffsHolder.to_tv.setText(diff.getToFileName());

        for(Hunk hunk : diff.getHunks()){
            diffsHolder.hunk_layout.addView(new Hunkview(context, hunk, diff.getHeaderLines()));
        }

    }

    @Override
    public int getItemCount() {
        return diffslist.size();
    }

    public void updateDiffList(List<Diff> diffslist) {
        this.diffslist = diffslist;
        notifyDataSetChanged();
    }

    public class DiffsHolder extends RecyclerView.ViewHolder {
        TextView from_tv;
        TextView to_tv;
        LinearLayout hunk_layout;
        public DiffsHolder(@NonNull View itemView) {
            super(itemView);
            from_tv = itemView.findViewById(R.id.from_textview);
           // to_tv = itemView.findViewById(R.id.to_textview);
            hunk_layout = itemView.findViewById(R.id.container_hunk);

        }
    }
}
