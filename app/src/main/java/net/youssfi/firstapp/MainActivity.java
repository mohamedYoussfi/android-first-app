package net.youssfi.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.internal.GsonBuildConfig;

import net.youssfi.firstapp.api.GitHubService;
import net.youssfi.firstapp.model.User;
import net.youssfi.firstapp.model.UserListViewModel;
import net.youssfi.firstapp.model.UserResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
     List<User> data=new ArrayList<>();
     public static final String MESSAGE_NAME="user.login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.firstApp();
        //this.gitRepoApp();
    }

    private void firstApp(){

        final EditText editTextAmount=findViewById(R.id.editTextAmount);
        Button buttonCompute=findViewById(R.id.buttonCompute);
        final TextView textViewResult=findViewById(R.id.textViewResult);
        ListView listViewResults=findViewById(R.id.listViewResults);
        final List<String> data=new ArrayList<>();
        final ArrayAdapter<String> stringArrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,data);
        listViewResults.setAdapter(stringArrayAdapter);
        buttonCompute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double amount=Double.parseDouble(editTextAmount.getText().toString());
                double result=amount*65;
                textViewResult.setText(String.valueOf(result));
                data.add(amount+"=>"+result);
                stringArrayAdapter.notifyDataSetChanged();
                editTextAmount.setText("");
            }
        });
    }
    private void gitRepoApp(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main2);
        EditText editTextQuery=findViewById(R.id.editTextQuery);
        Button  button=findViewById(R.id.buttonSearch);
        ListView listViewUsers=findViewById(R.id.listViewUsres);
        // ArrayAdapter<String> stringArrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,data);
        UserListViewModel listViewModel=new UserListViewModel(this,R.layout.user_item_layout,data);
        listViewUsers.setAdapter(listViewModel);
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GitHubService gitHubService=retrofit.create(GitHubService.class);

        button.setOnClickListener((view)->{
            String query=editTextQuery.getText().toString();
            Log.e("mee","click");
            Call<UserResponse> call=gitHubService.searchUsers(query);
            call.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    Log.e("request",call.request().url().toString());
                    if(!response.isSuccessful()){
                        Log.e("error",""+response.code());
                        return;
                    }
                    UserResponse usersResponse=response.body();
                    Log.e("",""+usersResponse.getTotalCount());
                    data.clear();
                    for(User u: usersResponse.getUsers()){
                        data.add(u);
                    }
                    listViewModel.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    Log.e("mee","error");
                    t.printStackTrace();
                }
            });
        });

        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String login=data.get(position).getLogin();
                Intent intent=new Intent(getApplicationContext(), RepositoriesActivity.class);
                intent.putExtra(MESSAGE_NAME,login);
                startActivity(intent);
            }
        });
    }
}
