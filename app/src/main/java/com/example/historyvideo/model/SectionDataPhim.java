package com.example.historyvideo.model;

import com.example.historyvideo.model.Phim;

import java.util.ArrayList;

public class SectionDataPhim {
    private String headerTitle;
    private ArrayList<Phim> allPhimSections;


    public SectionDataPhim(String headerTitle, ArrayList<Phim> allPhimSections) {
        this.headerTitle = headerTitle;
        this.allPhimSections = allPhimSections;
    }

    public SectionDataPhim() {
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList<Phim> getAllPhimSections() {
        return allPhimSections;
    }

    public void setAllPhimSections(ArrayList<Phim> allPhimSections) {
        this.allPhimSections = allPhimSections;
    }
}
