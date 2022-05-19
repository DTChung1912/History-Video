package com.example.historyvideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.historyvideo.R;
import com.example.historyvideo.model.TheLoai;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_Category extends ArrayAdapter<TheLoai> {
    public List<TheLoai> theLoaiList;
    public Context context;
    public LayoutInflater inflater;


    public Adapter_Category(@NonNull Context context, int resource, @NonNull List<TheLoai> objects) {
        super(context, resource, objects);
        this.theLoaiList = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.cate_item_list,parent,false);

            holder.imgCateLogo = convertView.findViewById(R.id.imgLogo);
            holder.tvCateName = convertView.findViewById(R.id.tvCateName);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        for(int i =0; i<theLoaiList.size(); i++){
            String tenTL = theLoaiList.get(position).TenTheLoai;
            holder.tvCateName.setText(tenTL);
            if(tenTL!= null){
                switch (tenTL){
                    case "Hành Động":
                        holder.imgCateLogo.setImageDrawable(getContext().getDrawable(R.drawable.ic_action));
                        holder.imgCateLogo.setBackground(getContext().getDrawable(R.drawable.elip_button_with_opacity_action));
                        break;
                    case "Hoạt Hình":
                        holder.imgCateLogo.setImageDrawable(getContext().getDrawable(R.drawable.ic_cartoon));
                        holder.imgCateLogo.setBackground(getContext().getDrawable(R.drawable.elip_button_with_opacity_cartoon));
                        break;
                    case "Phiêu Lưu":
                        holder.imgCateLogo.setImageDrawable(getContext().getDrawable(R.drawable.ic_journey));
                        holder.imgCateLogo.setBackground(getContext().getDrawable(R.drawable.elip_button_with_opacity_adventure));
                        break;
                    case "Kinh Dị":
                        holder.imgCateLogo.setImageDrawable(getContext().getDrawable(R.drawable.ic_ghost));
                        holder.imgCateLogo.setBackground(getContext().getDrawable(R.drawable.elip_button_with_opacity_horror));
                        break;
                    case "Khoa Học Viễn Tưởng":
                        holder.imgCateLogo.setImageDrawable(getContext().getDrawable(R.drawable.ic_ufo));
                        holder.imgCateLogo.setBackground(getContext().getDrawable(R.drawable.elip_button_with_opacity_scient));
                        break;
                    case "Tài Liệu":
                        holder.imgCateLogo.setImageDrawable(getContext().getDrawable(R.drawable.ic_document));
                        holder.imgCateLogo.setBackground(getContext().getDrawable(R.drawable.elip_button_with_opacity_documentary));
                        break;
                    case "Tội Phạm":
                        holder.imgCateLogo.setImageDrawable(getContext().getDrawable(R.drawable.ic_rob));
                        holder.imgCateLogo.setBackground(getContext().getDrawable(R.drawable.elip_button_with_opacity_criminal));
                        break;
                    case "Tâm Lý":
                        holder.imgCateLogo.setImageDrawable(getContext().getDrawable(R.drawable.ic_psychology));
                        holder.imgCateLogo.setBackground(getContext().getDrawable(R.drawable.elip_button_with_opacity_psy));
                        break;
                    case "Tình Cảm":
                        holder.imgCateLogo.setImageDrawable(getContext().getDrawable(R.drawable.ic_heartbeat));
                        holder.imgCateLogo.setBackground(getContext().getDrawable(R.drawable.elip_button_with_opacity_love));
                        break;
                    case "Hài":
                        holder.imgCateLogo.setImageDrawable(getContext().getDrawable(R.drawable.ic_laughing));
                        holder.imgCateLogo.setBackground(getContext().getDrawable(R.drawable.elip_button_with_opacity_funny));
                        break;
                    case "Chiến Tranh":
                        holder.imgCateLogo.setImageDrawable(getContext().getDrawable(R.drawable.ic_soldier));
                        holder.imgCateLogo.setBackground(getContext().getDrawable(R.drawable.elip_button_with_opacity_war));
                        break;
                }
            }
        }


        return convertView;
    }

    public static class ViewHolder{
        ImageButton imgCateLogo;
        TextView tvCateName;
    }
}
