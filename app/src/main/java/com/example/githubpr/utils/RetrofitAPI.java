package com.example.githubpr.utils;

import com.example.githubpr.Model.PullRequest;
import com.example.githubpr.Model.Repository;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface RetrofitAPI {

    @GET("users/google/repos")
    Call<ArrayList<Repository>> getRespositories();

    @GET("repos/google/{repos_name}/pulls")
    Call<ArrayList<PullRequest>> getPullrequests(@Path("repos_name") String repos_name);

    @GET
    Call<ResponseBody> getDiffs(@Url String diffUrl);
}
