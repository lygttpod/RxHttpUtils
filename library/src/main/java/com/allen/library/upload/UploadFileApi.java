package com.allen.library.upload;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * Created by allen on 2017/6/15.
 * <p>
 *
 * @author Allen
 *         文件上传
 */

public interface UploadFileApi {


    /**
     * 上传多个文件
     *
     * @param uploadUrl 地址
     * @param files      文件
     * @return ResponseBody
     */
    @Multipart
    @POST
    Observable<ResponseBody> uploadFiles(@Url String uploadUrl,
                                         @Part List<MultipartBody.Part> files);
}
