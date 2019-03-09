package com.example.githubpr.Controller.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.githubpr.NetworkChecker;
import com.example.githubpr.R;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

public class GithubRepos extends AppCompatActivity {

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver,connectivityIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        registerReceiver(broadcastReceiver,connectivityIntentFilter);
    }
}
