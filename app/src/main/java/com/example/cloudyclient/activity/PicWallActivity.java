package com.example.cloudyclient.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cloudyclient.R;
import com.example.cloudyclient.activity.dialog.PicSearchDialog;
import com.example.cloudyclient.model.bean.PicEntity;
import com.example.cloudyclient.model.biz.LocalStorageManager;
import com.example.cloudyclient.model.biz.ScreenPropManager;
import com.example.cloudyclient.model.biz.ToastUtil;
import com.example.photoview.Info;
import com.example.photoview.PhotoView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PicWallActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.pic_wall_rv)
    RecyclerView picWallRv;
    @BindView(R.id.show_bg)
    ImageView showBg;
    @BindView(R.id.show_img)
    PhotoView showImg;
    @BindView(R.id.show_canvas)
    FrameLayout showCanvas;

    Info photoViewInfo;//图片位置尺寸信息

    private enum WALL_VIEW_TYPE_ENUM {
        TYPE_1_COLUMN,//单列
        TYPE_2_COLUMN//双列
    }

    private int wallViewType = WALL_VIEW_TYPE_ENUM.TYPE_1_COLUMN.ordinal();

    //动画效果
    AlphaAnimation in = new AlphaAnimation(0, 1);
    AlphaAnimation out = new AlphaAnimation(1, 0);

    private final int TO_PIC_MAIN = 0;
    private PicEntity picEntity = null;

    private PicWallAdapter mAdapter;
    private RecyclerView.ViewHolder mViewHolder;

    public static boolean isFiltered = false;//照片是否被检索过滤

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_wall);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        setSupportActionBar(toolbar);//工具栏
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_action_sync:
                        break;
                    case R.id.menu_action_switch:
                        switchWallView();
                        break;
                }
                return true;
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
    }

    @OnClick({R.id.fab, R.id.show_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab:
                if (!isFiltered) {
                    new PicSearchDialog(fab, mAdapter).show(getFragmentManager(), "");
                } else {
                    abandonFilter();
                }
                break;
            case R.id.show_img:
                showBg.startAnimation(out);
                showImg.animaTo(photoViewInfo, new Runnable() {
                    @Override
                    public void run() {
                        showCanvas.setVisibility(View.GONE);
                        toolbar.setVisibility(View.VISIBLE);//恢复显示toolbar
                    }
                });

                break;
            default:
                break;
        }

    }

    public class PicWallAdapter extends RecyclerView.Adapter<PicWallAdapter.PicItemViewHolder> {
        private final LayoutInflater layoutInflater;
        private Context context;
        private List<String> data;

        public PicWallAdapter(Context context) {
            data = LocalStorageManager.getAllPicPath();//默认夹在目录下所有照片
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
                    .config(Bitmap.Config.RGB_565)
                    .noFade()
                    .fit()
                    .into(holder.photoView);

            holder.textView.setText(picEntity.getFMake() + "\r\r\r光圈:" + picEntity.getFFNumber()
                    + "\r\r\r快门:" + picEntity.getFExposureTime()
                    + "\r\r\rISO:" + picEntity.getFISOSpeedRatings());
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
            @BindView(R.id.cv_item_card)
            CardView cvItemCard;
            @BindView(R.id.image_view)
            PhotoView photoView;
            @BindView(R.id.text_view)
            TextView textView;

            public PicItemViewHolder(final View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);

                //设置单个CardView的高度
                if (wallViewType == WALL_VIEW_TYPE_ENUM.TYPE_1_COLUMN.ordinal()) {

                    RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) cvItemCard
                            .getLayoutParams();

                    param.height = ScreenPropManager.screenWidth_px * 3 / 4 - param.getMarginStart() - param
                            .getMarginEnd();

                    cvItemCard.setLayoutParams(param);
                } else if (wallViewType == WALL_VIEW_TYPE_ENUM.TYPE_2_COLUMN.ordinal()) {
                    RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) cvItemCard
                            .getLayoutParams();

                    param.height = ScreenPropManager.screenWidth_px / 2 - param.getMarginStart() - param
                            .getMarginEnd();

                    cvItemCard.setLayoutParams(param);
                }

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toolbar.setVisibility(View.GONE);//隐去toolbar
                        photoViewInfo = photoView.getInfo();//获取原始图片的信息

                        //显示大图
                        Picasso
                                .with(context)
                                .load(new File(data.get(getLayoutPosition())))
                                .config(Bitmap.Config.RGB_565)
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

        public void setData(List<String> data) {
            this.data = data;
            notifyDataSetChanged();
        }
    }

    //取消过滤
    private void abandonFilter() {
        isFiltered = false;
        mAdapter.setData(LocalStorageManager.getAllPicPath());
        fab.setImageResource(android.R.drawable.ic_menu_search);
        ToastUtil.showToast(PicWallActivity.this, "已显示所有照片");
        Snackbar.make(fab, "已显示所有照片", Snackbar.LENGTH_LONG).show();
    }

    private void switchWallView() {
        if (wallViewType == WALL_VIEW_TYPE_ENUM.TYPE_1_COLUMN.ordinal()) {
            wallViewType = WALL_VIEW_TYPE_ENUM.TYPE_2_COLUMN.ordinal();

//            picWallRv.setLayoutManager(new GridLayoutManager(PicWallActivity.this, 2));
            picWallRv.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper
                    .VERTICAL));
            picWallRv.setAdapter(mAdapter);
        } else {
            wallViewType = WALL_VIEW_TYPE_ENUM.TYPE_1_COLUMN.ordinal();
            picWallRv.setLayoutManager(new LinearLayoutManager(this));
            picWallRv.setAdapter(mAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pic_wall, menu);
        return true;
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
        } else if (isFiltered) {
            abandonFilter();
        } else {
            setResult(RESULT_OK);
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
