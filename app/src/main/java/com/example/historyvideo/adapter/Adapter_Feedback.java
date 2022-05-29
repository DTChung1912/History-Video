package com.example.historyvideo.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.historyvideo.R;
import com.example.historyvideo.model.FeedBack;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_Feedback extends ArrayAdapter<FeedBack> {
    private List<FeedBack> arrayList;
    private Context context;
    private LayoutInflater inflater;

    DatabaseReference mData;

    public Adapter_Feedback(@NonNull Context context, int resource, @NonNull List<FeedBack> objects) {
        super(context, resource, objects);
        this.arrayList = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.feedback_list_item, parent, false);
            holder.imgAvaFB = (CircleImageView) convertView.findViewById(R.id.imgavaFeedBack);
            holder.tvEmailFB = (TextView) convertView.findViewById(R.id.tvEmailFB);
            holder.contentFB = (TextView) convertView.findViewById(R.id.contentFB);
            holder.timestampFB = (TextView) convertView.findViewById(R.id.timestampFB);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        mData = FirebaseDatabase.getInstance().getReference();

        FeedBack feedBack = arrayList.get(position);
        String email = feedBack.Email;
        String at = feedBack.At;
        String content = feedBack.Content;
        String status = feedBack.Status;
        String sent = "Sent";

        holder.tvEmailFB.setText("<" + email + ">");
        holder.contentFB.setText("Message: " + content);
        holder.timestampFB.setText(at);

        if (status != null && status.equals(sent)) {
            holder.tvEmailFB.setTypeface(Typeface.DEFAULT_BOLD);
            holder.contentFB.setTypeface(Typeface.DEFAULT_BOLD);
            holder.timestampFB.setTypeface(Typeface.DEFAULT_BOLD);

            holder.tvEmailFB.setTextColor(getContext().getResources().getColor(R.color.sectionHeader));
            holder.contentFB.setTextColor(getContext().getResources().getColor(R.color.sectionHeader));
            holder.timestampFB.setTextColor(getContext().getResources().getColor(R.color.sectionHeader));
        } else if (status != null && status.equals("Seen")) {
            holder.tvEmailFB.setTypeface(Typeface.DEFAULT);
            holder.contentFB.setTypeface(Typeface.DEFAULT);
            holder.timestampFB.setTypeface(Typeface.DEFAULT);

            holder.tvEmailFB.setTextColor(getContext().getResources().getColor(R.color.gray_dark));
            holder.contentFB.setTextColor(getContext().getResources().getColor(R.color.gray_dark));
            holder.timestampFB.setTextColor(getContext().getResources().getColor(R.color.gray_dark));
        }
        mData.child("Users").orderByChild("Email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String img = ds.child("Image").getValue().toString();
                    Picasso.get()
                            .load(Uri.parse(img))
                            .into(holder.imgAvaFB);
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError databaseError) {
            }
        });

        return convertView;
    }

    public static class ViewHolder {
        public CircleImageView imgAvaFB;
        public TextView tvEmailFB, contentFB, timestampFB;
    }
}
