package com.tuanvu.chatbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by tuanvu on 12/20/17.
 */

public class MessageFragment extends Fragment {

    private RecyclerView recyclerView;

    // Firebase
    private ValueEventListener getCurrentUEventLisnter;

    Users currentLoggedInUser;

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_message, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.listMessage);

        // Lấy Uid của user hiện tại.
        API.currentUid = API.firebaseAuth.getCurrentUser().getUid();

        // EventListener với chức năng lấy Display Name của user hiện tại.
        getCurrentUEventLisnter = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    // Lấy thông tin user đã log in gán vào biến
                    currentLoggedInUser = dataSnapshot.getValue(Users.class);

                    // Lay cac tin nhan tu firebase
                    loadGroupFromFirebase();

                    // Hủy đăng ký sự kiện khi đã lấy được thông tin Display name của User hiện tại.
                    API.firebaseRef.child("USERS").child(API.currentUid).removeEventListener(getCurrentUEventLisnter);
                }
                catch (NullPointerException ex) {
                    Toast.makeText(getActivity(), ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    API.firebaseAuth.signOut();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        // Đăng ký sự kiện để lấy thông tin của user hiện tại trên firebase
        API.firebaseRef.child("USERS").child(API.currentUid).addListenerForSingleValueEvent(getCurrentUEventLisnter);

        return rootView;
    }

    public void loadGroupFromFirebase() {
        API.firebaseRef.child("USERS").child(currentLoggedInUser.getUid()).child("GROUPS")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<GroupChats> list = new ArrayList<>();
                GroupAdapter chatAdapter = new GroupAdapter(getActivity(), list);
                LinearLayoutManager chatLayoutManager = new LinearLayoutManager(getActivity());

                for (DataSnapshot db : dataSnapshot.getChildren()) {
                    // Lay tung message
                    GroupChats groupChats = db.getValue(GroupChats.class);
                    // Cho tung message vao danh sach
                    list.add(groupChats);
                }

                // Set orientation
                chatLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                recyclerView.setAdapter(chatAdapter);
                recyclerView.setLayoutManager(chatLayoutManager);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}