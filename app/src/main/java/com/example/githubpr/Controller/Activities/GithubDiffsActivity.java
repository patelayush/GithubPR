package com.example.githubpr.Controller.Activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.githubpr.Model.PullRequest;
import com.example.githubpr.R;
import com.example.githubpr.View.Adapters.DiffsAdapter;
import com.example.githubpr.utils.NetworkChecker;
import com.example.githubpr.utils.RetrofitAPI;
import com.example.githubpr.utils.ServiceGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reflectoring.diffparser.api.UnifiedDiffParser;
import io.reflectoring.diffparser.api.model.Diff;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

public class GithubDiffsActivity extends AppCompatActivity {

    private ArrayList<PullRequest> pulls;
    private RetrofitAPI restAPI;
    private String pull_title;
    private String diff_url;
    private UnifiedDiffParser diffParser;
    private List<Diff> difflist;
    private TextView files_changed_tv;
    private RecyclerView recyclerView;
    private DiffsAdapter diffsAdapter;
    private ProgressDialog progress;
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
        setContentView(R.layout.activity_github_diffs);
        restAPI = ServiceGenerator.createService(RetrofitAPI.class);
        progress=new ProgressDialog(this);
        diff_url = getIntent().getStringExtra("url");
        pull_title = getIntent().getStringExtra("pull");
        getSupportActionBar().setTitle(pull_title);
        difflist = new ArrayList<>();
        recyclerView = findViewById(R.id.diffs_recyclerview);
        files_changed_tv = findViewById(R.id.files_changed_tv);
        showProgressDialog(true);
        setUpAdapter();
        Call<ResponseBody> call = restAPI.getDiffs(diff_url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful() && response.body()!=null){
                    diffParser = new UnifiedDiffParser();
                    try {
                        difflist = new ArrayList<>();
                        difflist = diffParser.parse(response.body().bytes());
                        diffsAdapter.updateDiffList(difflist);
                        files_changed_tv.setText("Files Changed: " + difflist.size());
                        showProgressDialog(false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Failure in fetching",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgressDialog(boolean b) {
        if(b){
            progress.setMessage("Loading");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.setProgress(0);
            progress.show();
        }
        else
            progress.dismiss();
    }

    private void setUpAdapter() {

        diffsAdapter = new DiffsAdapter(GithubDiffsActivity.this,difflist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(diffsAdapter);
        diffsAdapter.notifyDataSetChanged();
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
