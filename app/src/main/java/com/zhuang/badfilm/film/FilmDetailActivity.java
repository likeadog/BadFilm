package com.zhuang.badfilm.film;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.zhuang.badfilm.R;
import com.zhuang.badfilm.ZActivity;
import com.zhuang.badfilm.databinding.ActivityFilmDetailBinding;
import com.zhuang.badfilm.databinding.HeaderFilmDetailBinding;
import com.zhuang.badfilm.login.LoginActivity;
import com.zhuang.badfilm.model.Comment;
import com.zhuang.badfilm.model.Film;
import com.zhuang.badfilm.model.FilmList;
import com.zhuang.badfilm.network.BaseReturnMsg;
import com.zhuang.badfilm.network.RetrofitHelper;
import com.zhuang.badfilm.network.ReturnDataList;
import com.zhuang.badfilm.network.service.FilmService;
import com.zhuang.badfilm.user.UserDataActivity;
import com.zhuang.badfilm.utils.SharedPreferencesUtil;
import com.zhuang.badfilm.view.ZRecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilmDetailActivity extends ZActivity implements CommentAdapter.OnViewClickListener,
        ZRecyclerView.OnLoadListener {

    ActivityFilmDetailBinding binding;
    Film film;
    int limit = 20;
    int offset = 0;
    List<Comment> commentList = new ArrayList<>();
    CommentAdapter adapter;
    private final int New_Comment = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_film_detail);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        film = (Film) getIntent().getSerializableExtra("film");
        binding.setFilm(film);

        View header = View.inflate(this, R.layout.header_film_detail, null);
        HeaderFilmDetailBinding headerFilmDetailBinding = DataBindingUtil.bind(header);
        headerFilmDetailBinding.setFilm(film);
        binding.recyclerView.addHeaderView(header);
        binding.recyclerView.setEmptyText("没有评论，抢个沙发吧！");

        adapter = new CommentAdapter(commentList);
        adapter.setOnViewClickListener(this);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setOnLoadListener(this);
        getCommentList();
    }

    /**
     * 获取评论列表
     */
    public void getCommentList() {
        FilmService service = RetrofitHelper.createService(FilmService.class);
        Call<ReturnDataList<Comment>> call = service.getCommentList(film.getId(), limit, offset);
        call.enqueue(new Callback<ReturnDataList<Comment>>() {
            @Override
            public void onResponse(Call<ReturnDataList<Comment>> call, Response<ReturnDataList<Comment>> response) {
                ReturnDataList<Comment> list = response.body();
                //成功
                if (list.getCode() == 0) {
                    if (list.getData().size() > 0) {
                        commentList.addAll(list.getData());
                        adapter.notifyDataSetChanged();
                        offset += limit;
                    } else {
                        binding.recyclerView.loadComplete();
                    }
                }
            }

            @Override
            public void onFailure(Call<ReturnDataList<Comment>> call, Throwable t) {
                Log.e(TAG, t.toString());
                binding.recyclerView.loadComplete();
            }
        });
    }

    /**
     * 删除评论
     */
    public void deleteComment(final int position) {
        Comment comment = commentList.get(position);
        FilmService service = RetrofitHelper.createServiceWidthToken(FilmService.class);
        Call<BaseReturnMsg> call = service.deleteComment(comment.getId());
        call.enqueue(new Callback<BaseReturnMsg>() {
            @Override
            public void onResponse(Call<BaseReturnMsg> call, Response<BaseReturnMsg> response) {

            }

            @Override
            public void onFailure(Call<BaseReturnMsg> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private boolean hasToken() {
        String token = SharedPreferencesUtil.getToken();
        return token != null;
    }

    public void newComment(View view) {
        if (!hasToken()) {
            gotoLogin();
        } else {
            gotoNewComment();
        }
    }

    private void gotoNewComment() {
        Intent intent = new Intent(this, NewCommentActivity.class);
        intent.putExtra("filmId", film.getId());
        startActivityForResult(intent, New_Comment);
    }

    private void gotoLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void delete(int position) {
        deleteComment(position);
        commentList.remove(position);
        if (adapter.getItemCount() > 0) {
            adapter.notifyItemRemoved(position + 1);//有了header，这里需要加1，否则报错
            adapter.notifyItemRangeChanged(position + 1, adapter.getItemCount() - position + 1);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void like() {

    }

    @Override
    public void getUser() {

    }

    @Override
    public void onLoad() {
        getCommentList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == New_Comment) {
            Comment comment = (Comment)data.getSerializableExtra("comment");
            commentList.add(0,comment);
            adapter.notifyDataSetChanged();
        }
    }
}
