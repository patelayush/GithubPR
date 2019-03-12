package com.example.githubpr.Controller.Activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.githubpr.Model.Repository;
import com.example.githubpr.R;
import com.example.githubpr.View.Adapters.RepositoryAdapter;
import com.example.githubpr.utils.NetworkChecker;
import com.example.githubpr.utils.RetrofitAPI;
import com.example.githubpr.utils.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

public class GithubRepos extends AppCompatActivity {

    private ArrayList<Repository> repositories;
    private RetrofitAPI restAPI;
    private RecyclerView recyclerView;
    private RepositoryAdapter repositoryAdapter;
    private ProgressDialog progress ;
    int pagenumber = 1;
    private IntentFilter connectivityIntentFilter = new IntentFilter(CONNECTIVITY_ACTION);
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!NetworkChecker.getInstance().isNetworkAvailable(context)) {
                Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repos_github);
        restAPI = ServiceGenerator.createService(RetrofitAPI.class);
        progress=new ProgressDialog(this);
        recyclerView = findViewById(R.id.repos_recycler);
        Call<ArrayList<Repository>> call = restAPI.getRespositories(pagenumber);
        getSupportActionBar().setTitle("Google Repositories");
        call.enqueue(new Callback<ArrayList<Repository>>() {
            @Override
            public void onResponse(Call<ArrayList<Repository>> call, Response<ArrayList<Repository>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    repositories = response.body();
                    setUpAdapter();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Repository>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastposition = 0;
                lastposition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                if(lastposition == repositories.size()-1 && repositories.size() % 30 == 0){
                    pagenumber++;
                    loadmorerepositories();
                }
            }
        });
    }

    private void showProgressDialog(boolean b) {
        if(b){
            progress.setMessage("Loading More Repositories");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setProgress(0);
            progress.show();
        }
        else
            progress.dismiss();
    }

    private void loadmorerepositories() {
        while(ServiceGenerator.getDispatcher().runningCallsCount()!=0);
        showProgressDialog(true);
        Call<ArrayList<Repository>> call = restAPI.getRespositories(pagenumber);
       call.enqueue(new Callback<ArrayList<Repository>>() {
            @Override
            public void onResponse(Call<ArrayList<Repository>> call, Response<ArrayList<Repository>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    showProgressDialog(false);
                    repositories.addAll(response.body());
                    repositoryAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Repository>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Failure in fetching",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpAdapter() {
        repositoryAdapter = new RepositoryAdapter(repositories,getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(repositoryAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver,connectivityIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}
