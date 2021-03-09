package net.youssfi.firstapp.model;

import com.google.gson.annotations.SerializedName;

public class GitRepository {
    public long id;
    public String name;
    @SerializedName("pushed_at")
    public String pushedAt;
    public int size;
    public String language;
}
