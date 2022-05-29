package com.example.historyvideo.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.historyvideo.R;
import com.example.historyvideo.model.Users;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_All_User extends ArrayAdapter<Users> {
    private List<Users> usersList;
    private Context context;
    private LayoutInflater inflater;

    public Adapter_All_User(@NonNull Context context, int resource, @NonNull List<Users> objects) {
        super(context, resource, objects);
        this.usersList = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.recycler_user_item, parent, false);
            holder.imageView = convertView.findViewById(R.id.userImage);
            holder.green_dot = convertView.findViewById(R.id.green_dot);
            holder.tvHoTen = convertView.findViewById(R.id.tvuserName);
            holder.tvEmail = convertView.findViewById(R.id.tvuserEmail);
            holder.tvStatus = convertView.findViewById(R.id.tvState);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Users users = usersList.get(position);
        Uri image = Uri.parse(users.Image);

        Picasso.get()
                .load(image)
                .placeholder(R.drawable.person)
                .into(holder.imageView);

        String ten = usersList.get(position).HoTen;
        String email = usersList.get(position).Email;
        String state = usersList.get(position).Status;
        String last_active = usersList.get(position).Last_Active;
        String sdt = usersList.get(position).SDT;
        String img = usersList.get(position).Image;

        holder.tvHoTen.setText(ten);
        holder.tvEmail.setText("<" + email + ">");
        if (state.equals("Offline")) {
            holder.tvStatus.setText(state);
            holder.tvStatus.setTextColor(getContext().getResources().getColor(R.color.offline));
            holder.green_dot.setVisibility(View.INVISIBLE);
        } else {
            holder.tvStatus.setText(state);
            holder.tvStatus.setTextColor(getContext().getResources().getColor(R.color.online));
            holder.green_dot.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    public static class ViewHolder {
        public CircleImageView imageView;
        public CircleImageView green_dot;
        public TextView tvHoTen, tvEmail, tvStatus;
    }
}
