package com.example.historyvideo.model;

import java.io.Serializable;

public class Users implements Serializable {
    public String HoTen;
    public String Email;
    public String SDT;
    public String Image;
    public String Last_Active;
    public String Status;

    public Users() {
    }

    public Users(String hoTen, String email, String SDT, String image, String last_Active, String status) {
        HoTen = hoTen;
        Email = email;
        this.SDT = SDT;
        Image = image;
        Last_Active = last_Active;
        Status = status;
    }

}
