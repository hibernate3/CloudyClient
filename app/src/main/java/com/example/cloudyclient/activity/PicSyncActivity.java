package com.example.cloudyclient.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.cloudyclient.MainApplication;
import com.example.cloudyclient.R;
import com.example.cloudyclient.model.bean.CloudyFileBean;
import com.example.cloudyclient.model.biz.retrofit.api.ApiService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PicSyncActivity extends AppCompatActivity {
    private static final String ENDPOINT = "http://192.168.1.101:8080";

    @BindView(R.id.sync_pb)
    ProgressBar syncPb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_sync);
        ButterKnife.bind(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<CloudyFileBean>> call = apiService.getCloudyFiles(0);
        call.enqueue(new Callback<List<CloudyFileBean>>() {
            @Override
            public void onResponse(Call<List<CloudyFileBean>> call, Response<List<CloudyFileBean>> response) {
                syncPb.setVisibility(View.INVISIBLE);

                List<CloudyFileBean> list = response.body();
                for (int i = 0; i < list.size(); i++) {
                    Log.d(MainApplication.TAG, list.get(i).getFileName());
                }
            }

            @Override
            public void onFailure(Call<List<CloudyFileBean>> call, Throwable t) {
                Log.e(MainApplication.TAG, t.toString());
            }
        });
    }
}
