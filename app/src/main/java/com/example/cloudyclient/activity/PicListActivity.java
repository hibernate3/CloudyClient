package com.example.cloudyclient.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.cloudyclient.MainApplication;
import com.example.cloudyclient.R;
import com.example.cloudyclient.model.bean.PicEntity;
import com.example.photoview.Info;
import com.example.photoview.PhotoView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PicListActivity extends AppCompatActivity {

    @BindView(R.id.pics_rv)
    RecyclerView picsRv;
    @BindView(R.id.show_bg)
    ImageView showBg;
    @BindView(R.id.show_img)
    PhotoView showImg;
    @BindView(R.id.show_canvas)
    FrameLayout showCanvas;

    Info mInfo;

    AlphaAnimation in = new AlphaAnimation(0, 1);
    AlphaAnimation out = new AlphaAnimation(1, 0);

    private List<PicEntity> mPicEntities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_list);
        ButterKnife.bind(this);

        initData();

        initView();
    }

    private void initData() {
        mPicEntities = getIntent().getParcelableArrayListExtra("pic_entities");//获取传过来的图片信息数据

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
    }

    private void initView() {
        picsRv.setLayoutManager(new GridLayoutManager(this, 2));
//        picsRv.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
        picsRv.setAdapter(new PicListAdapter(PicListActivity.this));
        picsRv.setItemAnimator(new DefaultItemAnimator());

        showImg.enable();
        showImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBg.startAnimation(out);
                showImg.animaTo(mInfo, new Runnable() {
                    @Override
                    public void run() {
                        showCanvas.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    class PicListAdapter extends RecyclerView.Adapter<PicListAdapter.PicItemViewHolder> {
        private final LayoutInflater layoutInflater;
        private Context context;

        PicListAdapter(Context context) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public PicItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new PicItemViewHolder(layoutInflater.inflate(R.layout.pic_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(PicItemViewHolder holder, int position) {
            Log.d(MainApplication.TAG, "" + (int) (getResources().getDisplayMetrics().density * 150));
            if (position == 0) {
                Picasso
                        .with(context)
                        .load(new File(mPicEntities.get(position).getFileName()))
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .noFade()
//                        .resize(600, 600)
                        .into(holder.photoView);
            } else {
                Picasso
                        .with(context)
                        .load(new File(mPicEntities.get(position).getFileName()))
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .noFade()
                        .resize((int) (getResources().getDisplayMetrics().density * 150), (int) (getResources()
                                .getDisplayMetrics().density * 150))
                        .into(holder.photoView);
            }

        }

        @Override
        public int getItemCount() {
            return mPicEntities == null ? 0 : mPicEntities.size();
        }

        class PicItemViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.photo)
            PhotoView photoView;
            @BindView(R.id.cv_item_card)
            CardView cvItemCard;

            public PicItemViewHolder(final View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mInfo = photoView.getInfo();//获取原始图片的信息

                        Picasso
                                .with(context)
                                .load(new File(mPicEntities.get(getLayoutPosition()).getFileName()))
                                .into(showImg, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        showCanvas.startAnimation(in);
                                        showBg.setVisibility(View.VISIBLE);
                                        showCanvas.setVisibility(View.VISIBLE);
                                        showImg.animaFrom(mInfo);
                                    }

                                    @Override
                                    public void onError() {
                                    }
                                });
                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return false;
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (showCanvas.getVisibility() == View.VISIBLE) {
            showBg.startAnimation(out);
            showImg.animaTo(mInfo, new Runnable() {
                @Override
                public void run() {
                    showCanvas.setVisibility(View.GONE);
                }
            });
        } else {
            super.onBackPressed();
        }
    }
}
