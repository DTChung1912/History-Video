package com.example.historyvideo.model;

import java.io.Serializable;

public class Phim implements Serializable {
    private String idPhim;
    private String tenPhim;
    private String linkPhim;
    private String linksub;
    private String posterPhim;
    private String theloaiPhim;
    private String motaPhim;
    private String dienvienPhim;
    private long soluotXem;

    public Phim() {
    }

    public Phim(String idPhim, String tenPhim, String linkPhim, String linksub, String posterPhim, String theloaiPhim, String motaPhim, String dienvienPhim, long soluotXem) {
        this.idPhim = idPhim;
        this.tenPhim = tenPhim;
        this.linkPhim = linkPhim;
        this.linksub = linksub;
        this.posterPhim = posterPhim;
        this.theloaiPhim = theloaiPhim;
        this.motaPhim = motaPhim;
        this.dienvienPhim = dienvienPhim;
        this.soluotXem = soluotXem;
    }

    public String getIdPhim() {
        return idPhim;
    }

    public void setIdPhim(String idPhim) {
        this.idPhim = idPhim;
    }

    public String getTenPhim() {
        return tenPhim;
    }

    public void setTenPhim(String tenPhim) {
        this.tenPhim = tenPhim;
    }

    public String getLinkPhim() {
        return linkPhim;
    }

    public void setLinkPhim(String linkPhim) {
        this.linkPhim = linkPhim;
    }

    public String getLinksub() {
        return linksub;
    }

    public void setLinksub(String linksub) {
        this.linksub = linksub;
    }

    public String getPosterPhim() {
        return posterPhim;
    }

    public void setPosterPhim(String posterPhim) {
        this.posterPhim = posterPhim;
    }

    public String getTheloaiPhim() {
        return theloaiPhim;
    }

    public void setTheloaiPhim(String theloaiPhim) {
        this.theloaiPhim = theloaiPhim;
    }

    public String getMotaPhim() {
        return motaPhim;
    }

    public void setMotaPhim(String motaPhim) {
        this.motaPhim = motaPhim;
    }

    public String getDienvienPhim() {
        return dienvienPhim;
    }

    public void setDienvienPhim(String dienvienPhim) {
        this.dienvienPhim = dienvienPhim;
    }

    public long getSoluotXem() {
        return soluotXem;
    }

    public void setSoluotXem(long soluotXem) {
        this.soluotXem = soluotXem;
    }
}
