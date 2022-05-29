package com.example.historyvideo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.historyvideo.R;
import com.example.historyvideo.model.User_Log;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Adapter_User_Log extends ArrayAdapter<User_Log> {
    List<User_Log> logList;
    Context context;
    LayoutInflater inflater;

    public Adapter_User_Log(@NonNull Context context, int resource, @NonNull List<User_Log> objects) {
        super(context, resource, objects);
        this.logList = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.user_log_list_item, parent, false);
            holder.imgMovie = (ImageView) convertView.findViewById(R.id.imgMovie);
            holder.tvdateTime = (TextView) convertView.findViewById(R.id.tvdateTime);
            holder.tvMovieName = (TextView) convertView.findViewById(R.id.tvMovieName);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String dateTime = logList.get(position).getDateTime();
        String movieName = logList.get(position).getMovieWatched();

        holder.tvdateTime.setText(dateTime);
        holder.tvMovieName.setText("Phim: " + movieName);

        return convertView;
    }

    public static class ViewHolder {
        ImageView imgMovie;
        TextView tvdateTime;
        TextView tvMovieName;
    }
}
