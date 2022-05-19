package com.example.historyvideo.model;

public class QuangCao {
    public String linkAnh;
    public String idPhim;

    public QuangCao(String linkAnh, String idPhim) {
        this.linkAnh = linkAnh;
        this.idPhim = idPhim;
    }

    public QuangCao() {
    }

    public String getLinkAnh() {
        return linkAnh;
    }

    public void setLinkAnh(String linkAnh) {
        this.linkAnh = linkAnh;
    }

    public String getIdPhim() {
        return idPhim;
    }

    public void setIdPhim(String idPhim) {
        this.idPhim = idPhim;
    }
}
