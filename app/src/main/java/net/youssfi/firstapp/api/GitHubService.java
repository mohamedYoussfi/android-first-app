package net.youssfi.firstapp.api;

import net.youssfi.firstapp.model.GitRepository;
import net.youssfi.firstapp.model.User;
import net.youssfi.firstapp.model.UserResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubService {
    @GET("search/users")
    Call<UserResponse> searchUsers(@Query("q") String query);
    @GET("users/{user}/repos")
    Call<List<GitRepository>> userRepositories(@Path("user") String user);
}
