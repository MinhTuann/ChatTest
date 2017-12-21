package com.tuanvu.chatbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tuanvu on 12/20/17.
 */

public class ListActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    // Firebase
    private ValueEventListener getCurrentUEventLisnter;

    Users currentLoggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        init();

        if (FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            Intent login = new Intent(ListActivity.this, LoginActivity.class);
            startActivity(login);
        }

        // Lấy Uid của user hiện tại.
        API.currentUid = API.firebaseAuth.getCurrentUser().getUid();

        // EventListener với chức năng lấy Display Name của user hiện tại.
        getCurrentUEventLisnter = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    // Lấy thông tin user đã log in gán vào biến
                    currentLoggedInUser = dataSnapshot.getValue(Users.class);

                    // Hủy đăng ký sự kiện khi đã lấy được thông tin Display name của User hiện tại.
                    API.firebaseRef.child("USERS").child(API.currentUid).removeEventListener(getCurrentUEventLisnter);
                }
                catch (NullPointerException ex) {
                    Toast.makeText(ListActivity.this, ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    API.firebaseAuth.signOut();
                    Intent intent = new Intent(ListActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        // Đăng ký sự kiện để lấy thông tin của user hiện tại trên firebase
        API.firebaseRef.child("USERS").child(API.currentUid).addListenerForSingleValueEvent(getCurrentUEventLisnter);
    }

    private void init() {

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    //#region Tab-bar Manager
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MessageFragment(), "Message");
        adapter.addFragment(new OnlineFragment(), "Online");
        adapter.addFragment(new GroupFragment(), "Group");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    //#endregion

    //#region Toolbar

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m_profile:
                Intent profile = new Intent(ListActivity.this, ProfileActivity.class);
                profile.putExtra("PROFILE", currentLoggedInUser);
                startActivityForResult(profile, 0);
                break;

            case R.id.m_sign_out:
                API.firebaseAuth.signOut();
                Intent intent = new Intent(ListActivity.this, LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.m_new_group:
                API.addGroup(currentLoggedInUser, new ArrayList<Users>() {{
                    add(new Users(
                            "OR2qaxro2LdXQGGOkI48oDvIg1s1",
                            "vuminhtuan1701@gmail.com",
                            "Tutu",
                            "",
                            true));
                    add(new Users(
                            "dNd4qcuGPVea9EAfPumt7Qpt8iD3",
                            "sang@gmail.com",
                            "sang",
                            "",
                            true));
                }});
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //#endregion


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
