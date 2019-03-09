package com.example.githubpr.View.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.githubpr.Model.PullRequest;
import com.example.githubpr.R;

import java.util.ArrayList;

public class PullRequestAdapter extends RecyclerView.Adapter<PullRequestAdapter.PullsHolder> {

    private Context context;
    private ArrayList<PullRequest> pulls;
    public PullRequestAdapter(ArrayList<PullRequest> pulls, Context context){
        this.pulls = pulls;
        this.context = context;
    }

    @NonNull
    @Override
    public PullsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View reposview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pull_request_row, viewGroup, false);
        return new PullsHolder(reposview);
    }

    @Override
    public void onBindViewHolder(@NonNull PullsHolder viewHolder, int i) {
        viewHolder.tvname.setText(this.pulls.get(i).getTitle());
    }

    @Override
    public int getItemCount() {
        return this.pulls.size();
    }

    public class PullsHolder extends RecyclerView.ViewHolder{
        public TextView tvname;
        public PullsHolder(@NonNull View itemView) {
            super(itemView);
            tvname = itemView.findViewById(R.id.pulls_name_tv);

            tvname.setOnClickListener(v -> {
                Toast.makeText(context,"Clicked" + pulls.get(getAdapterPosition()).getTitle(),Toast.LENGTH_SHORT).show();
            });
        }
    }
}
