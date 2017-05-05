package com.example.cloudyclient.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.ExifInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cloudyclient.R;
import com.example.cloudyclient.activity.PicMainActivity;
import com.example.cloudyclient.model.bean.PicInfoBean;
import com.example.cloudyclient.util.LocalStorageUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangyuhang on 17-5-5.
 */

public class PicWallAdapter extends RecyclerView.Adapter<PicWallAdapter.PicItemViewHolder> {
    private static final String TAG = "myLog";
    private final LayoutInflater mLayoutInflater;
    private static Context mContext;
    private List<String> mData;

    public PicWallAdapter(Context context) {
        mData = LocalStorageUtil.getAllPicPath();
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    @Override
    public PicItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PicItemViewHolder(mLayoutInflater.inflate(R.layout.pic_wall_item, parent, false));
    }

    @Override
    public void onBindViewHolder(PicItemViewHolder holder, int position) {
        Picasso picasso = Picasso.with(mContext);
        PicInfoBean picInfoBean = null;

        try {
            ExifInterface exifInterface = new ExifInterface(mData.get(position));
            picInfoBean = new PicInfoBean(exifInterface);

        } catch (IOException e) {
            e.printStackTrace();
        }

        picasso
                .load(new File(mData.get(position)))
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .noFade()
                .resize(600, 400).centerInside()
                .into(((PicItemViewHolder) holder).imageView);

        ((PicItemViewHolder) holder).textView.setText(picInfoBean.getFMake());
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class PicItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_view)
        ImageView imageView;
        @BindView(R.id.text_view)
        TextView textView;

        public PicItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, PicMainActivity.class));
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    textView.setBackgroundResource(android.R.color.holo_red_light);
                    return true;
                }
            });
        }
    }
}
