package com.example.admin.selectpictest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Administrator on 2017/10/20 0020.
 */

public class PhotoRecyclerAdapter extends RecyclerView.Adapter<PhotoRecyclerAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater mLayoutInflater;
    private List<String> result;
    private TakePhoto takePhoto;

    public PhotoRecyclerAdapter(Context context, List<String> result, TakePhoto takePhoto) {
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.result = result;
        this.takePhoto = takePhoto;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.image, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.e( "TAG","onBindViewHolder --- position: " +position+ "result.size : "+result.size());
        if (position != 9 && position == result.size()){
            Glide.with(context)
                    .load(R.mipmap.pick_phone)
                    .into(holder.image);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    takePhoto.takePhoto();
                }
            });
            holder.del.setVisibility(View.GONE);
        }else {
            Glide.with(context)
                    .load(result.get(position))
                    .centerCrop()
                    .into(holder.image);
            holder.del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    takePhoto.delPhoto(position,result.get(position));
                }
            });
            holder.del.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return result.size() == 9 ? 9 : result.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image,del;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            del = (ImageView) itemView.findViewById(R.id.del);
        }
    }

    public interface TakePhoto{
        void takePhoto();
        void delPhoto(int position, String url);
    }
}
