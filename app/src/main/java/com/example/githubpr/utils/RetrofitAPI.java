package com.example.githubpr.utils;

import com.example.githubpr.Controller.Activities.GithubRepos;
import com.example.githubpr.Model.Repository;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitAPI {

    @GET("users/google/repos")
    Call<ArrayList<Repository>> getRespositories();
}
