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
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.githubpr.Model.PullRequest;
import com.example.githubpr.R;
import com.example.githubpr.View.Adapters.PullRequestAdapter;
import com.example.githubpr.utils.NetworkChecker;
import com.example.githubpr.utils.RetrofitAPI;
import com.example.githubpr.utils.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

public class GithubPulls extends AppCompatActivity {

    private ArrayList<PullRequest> pulls;
    private RetrofitAPI restAPI;
    private String repository_name;
    private TextView no_pulls_tv;
    private RecyclerView recyclerView;
    private ProgressDialog progress;
    int pagenumber = 1;
    private PullRequestAdapter pullRequestAdapter;
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
        setContentView(R.layout.activity_github_pulls);

        repository_name = getIntent().getStringExtra("name");
        restAPI = ServiceGenerator.createService(RetrofitAPI.class);
        recyclerView = findViewById(R.id.pulls_recycler);
        no_pulls_tv = findViewById(R.id.no_pulls_tv);
        getSupportActionBar().setTitle(repository_name);

        Call<ArrayList<PullRequest>> call = restAPI.getPullrequests(repository_name,pagenumber); // only fetch open pull requests.
        call.enqueue(new Callback<ArrayList<PullRequest>>() {
            @Override
            public void onResponse(Call<ArrayList<PullRequest>> call, Response<ArrayList<PullRequest>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    pulls = response.body();
                    if(pulls.size()!=0)
                        setUpAdapter();
                    else
                        no_pulls_tv.setText("No open Pull Requests for " + Html.fromHtml("<b>" + repository_name + "</b>"));


                }
            }

            @Override
            public void onFailure(Call<ArrayList<PullRequest>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastposition = 0;
                lastposition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                if(lastposition == pulls.size()-1 && pulls.size() % 30 == 0){
                    pagenumber++;
                    loadmorerepositories();
                }
            }
        });
    }

    private void showProgressDialog(boolean b) {
        if(b){
            progress=new ProgressDialog(this);
            progress.setMessage("Loading More Pull Requests");
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
        Call<ArrayList<PullRequest>> call = restAPI.getPullrequests(repository_name,pagenumber); // only fetch open pull requests.
        call.enqueue(new Callback<ArrayList<PullRequest>>() {
            @Override
            public void onResponse(Call<ArrayList<PullRequest>> call, Response<ArrayList<PullRequest>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    showProgressDialog(false);
                    pulls.addAll(response.body());
                    pullRequestAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PullRequest>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
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

    private void setUpAdapter() {
        pullRequestAdapter = new PullRequestAdapter(pulls,getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(pullRequestAdapter);
        recyclerView.setVisibility(View.VISIBLE);
        no_pulls_tv.setVisibility(View.GONE);
    }
}
