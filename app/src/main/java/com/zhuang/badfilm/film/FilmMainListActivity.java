package com.zhuang.badfilm.film;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.zhuang.badfilm.R;
import com.zhuang.badfilm.ZActivity;
import com.zhuang.badfilm.databinding.ActivityFilmMainBinding;
import com.zhuang.badfilm.model.Film;
import com.zhuang.badfilm.model.FilmList;
import com.zhuang.badfilm.network.RetrofitHelper;
import com.zhuang.badfilm.network.ReturnDataList;
import com.zhuang.badfilm.network.service.FilmService;
import com.zhuang.badfilm.update.UpdateManager;
import com.zhuang.badfilm.user.UserMainActivity;
import com.zhuang.badfilm.utils.SharedPreferencesUtil;
import com.zhuang.badfilm.view.ZRecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilmMainListActivity extends ZActivity implements ZRecyclerView.OnLoadListener,
        ZRecyclerView.OnItemClickListener {

    ActivityFilmMainBinding binding;
    FilmMainListAdapter adapter;
    List<Film> filmList = new ArrayList<>();
    int limit = 10;
    int offset = 0;
    public static final int User = 1;//标志跳转到用户界面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_film_main);

        adapter = new FilmMainListAdapter(filmList);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setOnLoadListener(this);
        binding.recyclerView.setOnItemClickListener(this);
        getFilmList();

        //更新
        UpdateManager updateManager = new UpdateManager(this);
        updateManager.update();
    }

    /**
     * 获取电影列表
     */
    public void getFilmList() {
        FilmService service = RetrofitHelper.createService(FilmService.class);
        Call<ReturnDataList<Film>> call = service.getFilmList(limit, offset);
        call.enqueue(new Callback<ReturnDataList<Film>>() {
            @Override
            public void onResponse(Call<ReturnDataList<Film>> call, Response<ReturnDataList<Film>> response) {
                ReturnDataList<Film> list = response.body();
                //成功
                if (list.getCode() == 0) {
                    if (list.getData().size() > 0) {
                        filmList.addAll(list.getData());
                        adapter.notifyDataSetChanged();
                        offset += limit;
                    } else {
                        binding.recyclerView.loadComplete();
                    }
                }
            }

            @Override
            public void onFailure(Call<ReturnDataList<Film>> call, Throwable t) {
                Log.e("zhuang", t.toString());
            }
        });
    }

    @Override
    public void onLoad() {
        getFilmList();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, FilmDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("film", filmList.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void gotoUser(View view) {
        Intent intent = new Intent(this, UserMainActivity.class);
        startActivityForResult(intent, User);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //从设置界面回来，如果改变了主题，则修改MainActivity的主题
        if (requestCode == User && resultCode == RESULT_OK) {
            int theme = SharedPreferencesUtil.getTheme();
            int themeColor[] = {R.color.colorPrimary, R.color.blue, R.color.greenSea};
            binding.toolbar.setBackgroundResource(themeColor[theme]);
        }
    }

}
