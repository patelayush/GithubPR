package com.example.githubpr.View.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.githubpr.Controller.Activities.GithubPulls;
import com.example.githubpr.Model.Repository;
import com.example.githubpr.R;

import java.util.ArrayList;

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.Reposholder> {

    private Context context;
    private ArrayList<Repository> repositories;
    public RepositoryAdapter(ArrayList<Repository> repositories, Context context){
        this.repositories = repositories;
        this.context = context;
    }

    @NonNull
    @Override
    public Reposholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View reposview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.repositiory_row, viewGroup, false);
        return new Reposholder(reposview);
    }

    @Override
    public void onBindViewHolder(@NonNull Reposholder viewHolder, int i) {
        viewHolder.tvname.setText(this.repositories.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return this.repositories.size();
    }

    public class Reposholder extends RecyclerView.ViewHolder{
        public TextView tvname;
        public Reposholder(@NonNull View itemView) {
            super(itemView);
            tvname = itemView.findViewById(R.id.repos_name_tv);

            tvname.setOnClickListener(v -> {
                context.startActivity(new Intent(context, GithubPulls.class).
                        putExtra("name",repositories.get(getAdapterPosition()).getName()));
            });
        }
    }
}
