package com.zhuang.badfilm.network.service;

import com.zhuang.badfilm.model.Comment;
import com.zhuang.badfilm.model.Film;
import com.zhuang.badfilm.model.FilmList;
import com.zhuang.badfilm.network.BaseReturnMsg;
import com.zhuang.badfilm.network.ReturnData;
import com.zhuang.badfilm.network.ReturnDataList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zhuang on 2017/7/12.
 */

public interface FilmService {

    @GET("movie/list")
    Call<ReturnDataList<Film>> getFilmList(@Query("limit") int limit, @Query("offset") int offset);

    @GET("comment/add")
    Call<ReturnData<Comment>> addComment(@Query("appid") String appid, @Query("content") String content, @Query("subjectid") String subjectid, @Query("star") float star);

    @GET("comment/movie")
    Call<ReturnDataList<Comment>> getCommentList(@Query("subjectid") String subjectid,@Query("limit") int limit, @Query("offset") int offset);

    @GET("comment/del")
    Call<BaseReturnMsg> deleteComment(@Query("ids") String id);

}
