package com.example.githubpr.Controller.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.githubpr.Model.PullRequest;
import com.example.githubpr.R;
import com.example.githubpr.View.Adapters.PullRequestAdapter;
import com.example.githubpr.utils.NetworkChecker;
import com.example.githubpr.utils.RetrofitAPI;
import com.example.githubpr.utils.ServiceGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reflectoring.diffparser.api.DiffParser;
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
    private String repository_name;
    private String diff_url;
    private DiffParser diffParser;
    private List<Diff> difflist;
    private TextView no_pulls_tv;
    private RecyclerView recyclerView;
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
        restAPI = ServiceGenerator.createService(RetrofitAPI.class);
        setContentView(R.layout.activity_github_diffs);
        diff_url = getIntent().getStringExtra("url");
        System.out.println(diff_url);
        Call<ResponseBody> call = restAPI.getDiffs(diff_url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful() && response.body()!=null){
                    diffParser = new UnifiedDiffParser();
                    try {
                        difflist = new ArrayList<>();
                        List<Diff> difflist = diffParser.parse(response.body().bytes());
                        System.out.println(difflist.size());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}
