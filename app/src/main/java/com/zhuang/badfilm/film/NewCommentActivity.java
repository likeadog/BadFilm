package com.zhuang.badfilm.film;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.zhuang.badfilm.BuildConfig;
import com.zhuang.badfilm.R;
import com.zhuang.badfilm.ZActivity;
import com.zhuang.badfilm.databinding.ActivityNewCommentBinding;
import com.zhuang.badfilm.model.Comment;
import com.zhuang.badfilm.model.FilmList;
import com.zhuang.badfilm.network.BaseReturnMsg;
import com.zhuang.badfilm.network.RetrofitHelper;
import com.zhuang.badfilm.network.ReturnData;
import com.zhuang.badfilm.network.service.FilmService;
import com.zhuang.badfilm.view.LoadingDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewCommentActivity extends ZActivity implements View.OnClickListener {

    ActivityNewCommentBinding binding;
    String filmId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_comment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        filmId = getIntent().getStringExtra("filmId");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        MenuItem menuItem = menu.findItem(R.id.save);
        menuItem.getActionView().findViewById(R.id.saveBtn).setOnClickListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.saveBtn) {
            addComment();
        }
    }

    /**
     * 提交评论
     */
    public void addComment() {
        String content = binding.content.getText() + "";
        float start = binding.ratingBar.getRating();
        if (start == 0) {
            Toast.makeText(this, "请打分", Toast.LENGTH_SHORT).show();
            return;
        }

        final LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.show(getSupportFragmentManager(),"loading");

        FilmService service = RetrofitHelper.createServiceWidthToken(FilmService.class);
        Call<ReturnData<Comment>> call = service.addComment(BuildConfig.APPLICATION_ID,content, filmId, start);
        call.enqueue(new Callback<ReturnData<Comment>>() {
            @Override
            public void onResponse(Call<ReturnData<Comment>> call, Response<ReturnData<Comment>> response) {
                ReturnData<Comment> returnData = response.body();
                //成功
                if (returnData.getCode() == 0) {
                    loadingDialog.setSuccess();
                    Comment iComment = returnData.getObj();
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("comment", iComment);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK,intent);
                    finish();
                }else{
                    loadingDialog.setFailure();
                }
            }

            @Override
            public void onFailure(Call<ReturnData<Comment>> call, Throwable t) {
                Log.e(TAG, t.toString());
                loadingDialog.setFailure();
            }
        });
    }
}
