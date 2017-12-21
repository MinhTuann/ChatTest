package com.tuanvu.chatbox;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by tuanvu on 12/20/17.
 */

public class GroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_GROUP = 0;
    private List<GroupChats> groupChats;
    private Activity context;

    public GroupAdapter(Activity context, List<GroupChats> groupChats) {
        this.context = context;
        this.groupChats = groupChats;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View group = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
        return new GroupViewHolder(group) ;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //Set view holder
        final GroupViewHolder myholder = ((GroupViewHolder)holder);

        //Get current group
        final GroupChats gpc = groupChats.get(position);

        myholder.groupMessage.setText("");
        myholder.groupName.setText(gpc.getName());
        if (URLUtil.isValidUrl(gpc.getAvatar())) {
            Picasso.with(myholder.groupAvatar.getContext())
                    .load(gpc.getAvatar())
                    .into(myholder.groupAvatar);
        }

        //Click vo group
        myholder.itemGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chat = new Intent(context, MainActivity.class);
                chat.putExtra("GROUPCHATS", gpc.getUid());
                context.startActivityForResult(chat, 1000);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM_GROUP;
    }

    @Override
    public int getItemCount() {
        return groupChats.size();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {

        public TextView groupName;
        public TextView groupMessage;
        public ImageView groupAvatar;
        public RelativeLayout itemGroup;

        public GroupViewHolder(View itemView) {
            super(itemView);

            groupName = itemView.findViewById(R.id.groupName);
            groupMessage = itemView.findViewById(R.id.groupMessage);
            groupAvatar = itemView.findViewById(R.id.groupAvatar);
            itemGroup = itemView.findViewById(R.id.itemGroup);
        }
    }
}
