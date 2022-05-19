package com.example.historyvideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.historyvideo.R;
import com.example.historyvideo.model.SectionDataPhim;

import java.util.ArrayList;

public class Recyclerview_Data_Adapter extends RecyclerView.Adapter<Recyclerview_Data_Adapter.ItemRowHolder>{
    private ArrayList<SectionDataPhim> dataList;
    private Context mContext;

    public Recyclerview_Data_Adapter(Context context, ArrayList<SectionDataPhim> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_recycler_item, null,false);

        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, int i) {

        final String sectionName = dataList.get(i).getHeaderTitle();

        ArrayList singleSectionItems = dataList.get(i).getAllPhimSections();

        itemRowHolder.itemTitle.setText(sectionName);

        Section_List_Adapter itemListDataAdapter = new Section_List_Adapter(singleSectionItems, mContext);

        itemRowHolder.recycler_view_list.setHasFixedSize(true);
        itemRowHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);
        itemRowHolder.recycler_view_list.setNestedScrollingEnabled(false);

        if(i > 0){
            itemRowHolder.itemTitle.setTextColor(mContext.getResources().getColor(R.color.sectionHeader));
        }



    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        private TextView itemTitle;
        private RecyclerView recycler_view_list;


        private ItemRowHolder(View view) {
            super(view);
            this.itemTitle = (TextView) view.findViewById(R.id.itemTitle);
            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.recycler_view_list);

        }

    }

}
