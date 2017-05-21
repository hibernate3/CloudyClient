package com.example.cloudyclient.model.biz.retrofit.api;

import com.example.cloudyclient.model.bean.CloudyFileBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/5/21 0021.
 */

public interface ApiService {
    @GET("CloudyServer/request_files_name.action")
//    Observable<List<CloudyFileBean>> getCloudyFiles(@Query("isAbs") int Abs);
    Call<List<CloudyFileBean>> getCloudyFiles(@Query("isAbs") int Abs);//不使用RxJava
}
