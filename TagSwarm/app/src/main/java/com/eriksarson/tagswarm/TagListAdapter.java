package com.eriksarson.tagswarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class TagListAdapter extends RecyclerView.Adapter<TagListAdapter.TagViewHolder>
                                implements View.OnClickListener {

    private Context mContext;

    public class TagViewHolder extends RecyclerView.ViewHolder {
        private final TextView tagItemView;
        LinearLayout linearLayout_item;
        public TagViewHolder(View itemView) {
            super(itemView);
            tagItemView = itemView.findViewById(R.id.textView);
            linearLayout_item = itemView.findViewById(R.id.linearLayout_item);
        }
    }

    private final LayoutInflater mInflater;
    private List<Tag> mTags = Collections.emptyList(); // Cached copy of tags

    TagListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new TagViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TagViewHolder holder, int position) {
        holder.linearLayout_item.setTag(position);
        holder.linearLayout_item.setOnClickListener(this);
        if (mTags != null) {
            Tag current = mTags.get(position);
            holder.tagItemView.setText(current.getName());
        } else {
            // Covers the case of data not being ready yet.
            holder.tagItemView.setText("No Name");
        }
    }

    void setTags(List<Tag> tags) {
        mTags = tags;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mTags has not been updated (means initially, it's null, and we can't return null)
    @Override
    public  int getItemCount() {
        if (mTags != null)
            return mTags.size();
        else return 0;
    }

    @Override
    public void onClick(View v){
        int position = (int) v.getTag();

        switch (v.getId()) {
            case R.id.linearLayout_item:
                Intent intent = new Intent(mContext, ViewTagActivity.class);
                // Use Parcelable here to pass Tag
                intent.putExtra(Tag.TAG_EXTRA, mTags.get(position));
                ((Activity)mContext).startActivity(intent);
                break;
        }

    }
}
