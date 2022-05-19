package com.example.historyvideo.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;

import com.example.historyvideo.R;
import com.example.historyvideo.adapter.Adapter_All_User;
import com.example.historyvideo.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class All_UserActivity extends AppCompatActivity {
    private ListView userRecycler;
    private SearchView searchUser;
    private Adapter_All_User adapter;
    ArrayList<Users> usersList;
    DatabaseReference mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__user);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.gray_dark));
        searchUser = (SearchView)findViewById(R.id.searchUser);
        userRecycler = (ListView) findViewById(R.id.recyclerUser);

        usersList = new ArrayList<>();
        adapter = new Adapter_All_User(All_UserActivity.this, R.layout.recycler_user_item, usersList);
        userRecycler.setAdapter(adapter);

        mData = FirebaseDatabase.getInstance().getReference("Users");
        fillData();

        searchUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search_User(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search_User(newText);
                return false;
            }
        });

        userRecycler.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(All_UserActivity.this, UserInformationActivity.class);
                Users users = usersList.get(i);
                for(int a = 0; a< usersList.size(); a++){
                    intent.putExtra("dulieu", users);
                }
                startActivity(intent);
            }
        });
    }

    private void search_User(final String email) {
        Query query = mData.orderByChild("Email");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot!= null){
                    usersList.clear();
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        Users users = ds.getValue(Users.class);
                        String email_lowercase = users.Email.toLowerCase();
                        if(email_lowercase.contains(email)){
                            usersList.add(users);
                        }
                    }
                    Collections.sort(usersList, new Comparator<Users>() {
                        @Override
                        public int compare(Users users, Users t1) {
                            return users.HoTen.compareTo(t1.HoTen);
                        }
                    });
                    adapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        query.addValueEventListener(valueEventListener);
    }

    private void fillData() {
        DatabaseReference mData1 = FirebaseDatabase.getInstance().getReference("Users");
        mData1.orderByChild("HoTen").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot != null){
                    usersList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Users users = ds.getValue(Users.class);
                        usersList.add(users);
                        Collections.sort(usersList, new Comparator<Users>() {
                            @Override
                            public int compare(Users users, Users t1) {
                                return users.HoTen.compareTo(t1.HoTen);
                            }
                        });
                    }

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void clickBackToAdmin1(View view) {
        finish();
    }
}
