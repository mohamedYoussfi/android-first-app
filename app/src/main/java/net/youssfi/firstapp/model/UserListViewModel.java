package net.youssfi.firstapp.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.youssfi.firstapp.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListViewModel extends ArrayAdapter<User> {
    private List<User> users=new ArrayList<>();
    private int resource;
    public UserListViewModel(@NonNull Context context, int resource, List<User> users) {
        super(context, resource,users);
        this.resource=resource;
        this.users=users;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView=convertView;
        if(listView==null){
            listView= LayoutInflater.from(getContext()).inflate(resource,parent,false);
        }
        CircleImageView imageViewAvatar=listView.findViewById(R.id.imageViewAvatar);
        TextView textViewLogin=listView.findViewById(R.id.textViewLogin);
        TextView textViewScore=listView.findViewById(R.id.textViewScore);
        textViewLogin.setText(users.get(position).getLogin());
        textViewScore.setText(String.valueOf(users.get(position).getScore()));
        try {

            URL url=new URL(users.get(position).getAvatarUrl());
            //Log.i("",url.toString());
            Bitmap bitmap= BitmapFactory.decodeStream(url.openStream());
            imageViewAvatar.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  listView;
    }
}
