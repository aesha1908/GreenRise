package com.example.greenrise_sgp;


import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.browse.MediaBrowser;
import android.media.session.MediaSession;
import android.net.Uri;
import android.support.v4.media.MediaBrowserCompat;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MediatorLiveData;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder>{
    List<VideoModel> videoModelList;

    public VideoAdapter(List<VideoModel> videoModelList) {
        this.videoModelList = videoModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowvideo,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setVideoView(videoModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return videoModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        VideoView videoView;
        ProgressBar pb;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView=itemView.findViewById(R.id.VideoView);
            pb=itemView.findViewById(R.id.progressBar);

        }
        public void setVideoView(VideoModel v)
        {
            videoView.setVideoPath(v.link);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    pb.setVisibility(View.GONE);
                    mediaPlayer.start();
                    float videoRatio=mediaPlayer.getVideoWidth()/(float)mediaPlayer.getVideoHeight();
                    float screenRatio=videoView.getWidth()/(float) videoView.getHeight();
                    float scale=videoRatio/screenRatio;
                    if(scale>=1f)
                        videoView.setScaleX(scale);
                    else
                        videoView.setScaleY(1f/scale);
                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });

        }
    }
}
