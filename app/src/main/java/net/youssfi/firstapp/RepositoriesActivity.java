package net.youssfi.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import net.youssfi.firstapp.api.GitHubService;
import net.youssfi.firstapp.model.GitRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RepositoriesActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repositories_layout);
        setTitle("Repositories");
        TextView textViewLogin=findViewById(R.id.textViewUserLogin);
        ListView listViewRepositories=findViewById(R.id.listViewRepositories);
        List<String> data=new ArrayList<>();
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,data);
        listViewRepositories.setAdapter(arrayAdapter);
        Intent intent=getIntent();
        String login=intent.getStringExtra(MainActivity.MESSAGE_NAME);
        Log.i("info",login);
        textViewLogin.setText(login);
        Retrofit retrofit=new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.github.com/")
                .build();
        GitHubService gitHubService=retrofit.create(GitHubService.class);
        Call<List<GitRepository>> callRepo=gitHubService.userRepositories(login);
        callRepo.enqueue(new Callback<List<GitRepository>>() {
            @Override
            public void onResponse(Call<List<GitRepository>> call, Response<List<GitRepository>> response) {
                Log.i("",call.request().url().toString());
                if(!response.isSuccessful()){
                    Log.i("",String.valueOf(response.code()));
                    return;
                }
                List<GitRepository> gitRepositories=response.body();
                for(GitRepository gitRepository:gitRepositories){
                    String content="";
                    content+=gitRepository.name+"\n";
                    content+=gitRepository.language+"\n";
                    content+=gitRepository.pushedAt+"\n";
                    content+=gitRepository.size+"\n";
                    data.add(content);
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<GitRepository>> call, Throwable t) {

            }
        });


    }
}
