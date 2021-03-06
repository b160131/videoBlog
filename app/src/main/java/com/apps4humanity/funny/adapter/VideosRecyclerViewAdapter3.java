package com.apps4humanity.funny.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apps4humanity.funny.MyPlayerActivity;
import com.apps4humanity.funny.R;
import com.apps4humanity.funny.libs.AnimationUtils;
import com.apps4humanity.funny.prototype.VideosRecyclerViewPrototype;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class VideosRecyclerViewAdapter3  extends RecyclerView.Adapter<VideosRecyclerViewAdapter3.MyViewHolder3>{
    int previousPosition = 0;
    Context context;
    public ArrayList<VideosRecyclerViewPrototype> videosRecyclerViewPrototypes;

    public VideosRecyclerViewAdapter3(Context context, ArrayList<VideosRecyclerViewPrototype> videosRecyclerViewPrototypes) {
        this.context = context;
        this.videosRecyclerViewPrototypes = videosRecyclerViewPrototypes;
    }

    @Override
    public MyViewHolder3 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_item_tab3, parent, false);

        return new MyViewHolder3(view);
    }

    @Override
    public void onBindViewHolder(final VideosRecyclerViewAdapter3.MyViewHolder3 holder, final int position) {

        holder.title_tv.setText(videosRecyclerViewPrototypes.get(position).getTitle().replace(".mp4", ""));
        holder.time_tv.setText(videosRecyclerViewPrototypes.get(position).getTime());
        holder.num_views.setText(videosRecyclerViewPrototypes.get(position).getView() + " views");

        Picasso.with(context).load(Uri.parse(videosRecyclerViewPrototypes.get(position).getThumb())).fit().into(holder.thumbnail_iv);

        previousPosition = position;
        if (position > previousPosition) {
            AnimationUtils.animate(holder, true);
        } else {
            AnimationUtils.animate(holder, false);
        }
        previousPosition = position;
        final String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // it is clicked

                increaseValue(videosRecyclerViewPrototypes.get(position).getVideo_id(), videosRecyclerViewPrototypes.get(position).getView());
                Intent intent = new Intent(context, MyPlayerActivity.class);
                intent.putExtra("video_url", videosRecyclerViewPrototypes.get(position).getVid());
                context.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return videosRecyclerViewPrototypes.size();
    }

    class MyViewHolder3 extends RecyclerView.ViewHolder {

        TextView time_tv, title_tv, num_views;
        ImageView thumbnail_iv;

        public MyViewHolder3(View itemView) {
            super(itemView);
            time_tv = (TextView) itemView.findViewById(R.id.time_videoitem);
            title_tv = (TextView) itemView.findViewById(R.id.title_videoitem);
            thumbnail_iv = (ImageView) itemView.findViewById(R.id.imageview_item_thumbnail);
            num_views = (TextView) itemView.findViewById(R.id.view_video_item);
        }
    }

    public void increaseValue(String video_id, long view) {
        long new_view = view + 13;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = databaseReference.child("vids").child(video_id);
        Map<String, Object> map = new HashMap<>();
        map.put("view", new_view);
        databaseReference.updateChildren(map);

    }
}

