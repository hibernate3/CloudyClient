package com.example.cloudyclient.activity;

import android.content.Context;
import android.content.Intent;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cloudyclient.MainApplication;
import com.example.cloudyclient.R;
import com.example.cloudyclient.model.bean.PicEntity;
import com.example.cloudyclient.util.LocalStorageUtil;
import com.example.photoview.Info;
import com.example.photoview.PhotoView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PicWallActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pic_wall_rv)
    RecyclerView picWallRv;
    @BindView(R.id.show_bg)
    ImageView showBg;
    @BindView(R.id.show_img)
    PhotoView showImg;
    @BindView(R.id.show_canvas)
    FrameLayout showCanvas;

    Info photoViewInfo;//图片位置尺寸信息

    //动画效果
    AlphaAnimation in = new AlphaAnimation(0, 1);
    AlphaAnimation out = new AlphaAnimation(1, 0);

    private final int TO_PIC_MAIN = 0;
    private PicEntity picEntity = null;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.ViewHolder mViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_wall);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);//工具栏

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        picWallRv.setLayoutManager(new LinearLayoutManager(this));
        picWallRv.setAdapter(mAdapter = new PicWallAdapter(this));
        picWallRv.setItemAnimator(new DefaultItemAnimator());

        in.setDuration(300);
        out.setDuration(300);
        out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                showBg.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        showImg.enable();
        showImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBg.startAnimation(out);
                showImg.animaTo(photoViewInfo, new Runnable() {
                    @Override
                    public void run() {
                        showCanvas.setVisibility(View.GONE);
                        toolbar.setVisibility(View.VISIBLE);//恢复显示toolbar
                    }
                });
            }
        });
    }

    class PicWallAdapter extends RecyclerView.Adapter<PicWallAdapter.PicItemViewHolder> {
        private final LayoutInflater layoutInflater;
        private Context context;
        private List<String> data;

        public PicWallAdapter(Context context) {
            data = LocalStorageUtil.getAllPicPath();
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
        }


        @Override
        public PicItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PicItemViewHolder(layoutInflater.inflate(R.layout.pic_wall_item, parent, false));
        }

        @Override
        public void onBindViewHolder(PicItemViewHolder holder, int position) {
            mViewHolder = holder;

            Picasso picasso = Picasso.with(context);
            picEntity = new PicEntity(data.get(position));

            picasso
                    .load(new File(data.get(position)))
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .noFade()
                    .resize(600, 400).centerInside()
                    .into(holder.photoView);

            holder.textView.setText(picEntity.getFMake());
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return data == null ? 0 : data.size();
        }

        class PicItemViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.image_view)
            PhotoView photoView;
            @BindView(R.id.text_view)
            TextView textView;

            public PicItemViewHolder(final View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toolbar.setVisibility(View.GONE);//隐去toolbar
                        photoViewInfo = photoView.getInfo();//获取原始图片的信息

                        Picasso
                                .with(context)
                                .load(new File(data.get(getLayoutPosition())))
                                .into(showImg, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        showCanvas.startAnimation(in);
                                        showBg.setVisibility(View.VISIBLE);
                                        showCanvas.setVisibility(View.VISIBLE);
                                        showImg.animaFrom(photoViewInfo);
                                    }

                                    @Override
                                    public void onError() {
                                        toolbar.setVisibility(View.VISIBLE);//恢复显示toolbar
                                    }
                                });
                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Intent intent = new Intent(context, PicMainActivity.class);
                        intent.putExtra("pic_path", data.get(getLayoutPosition()));

                        startActivityForResult(intent, TO_PIC_MAIN);
                        return true;
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (showCanvas.getVisibility() == View.VISIBLE) {
            showBg.startAnimation(out);
            showImg.animaTo(photoViewInfo, new Runnable() {
                @Override
                public void run() {
                    showCanvas.setVisibility(View.GONE);
                }
            });
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == TO_PIC_MAIN) {
            }
        }
    }
}
