package com.example.historyvideo.model;

public class FeedBack {
    public String At;
    public String Email;
    public String Content;
    public String Status;
    public String Key;

    public FeedBack() {
    }

    public FeedBack(String at, String email, String content, String status, String pushKey) {
        At = at;
        Email = email;
        Content = content;
        Status = status;
        this.Key = pushKey;
    }
}
