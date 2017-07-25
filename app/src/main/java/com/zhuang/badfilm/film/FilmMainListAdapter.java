package com.zhuang.badfilm.film;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zhuang.badfilm.R;
import com.zhuang.badfilm.databinding.ItemFilmBinding;
import com.zhuang.badfilm.model.Film;

import java.util.List;

/**
 * Created by zhuang on 2017/7/10.
 */

public class FilmMainListAdapter extends RecyclerView.Adapter<FilmMainListAdapter.FilmViewHolder> {

    private List<Film> mList;

    public FilmMainListAdapter(List<Film> list) {
        this.mList = list;
    }

    @Override
    public FilmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemFilmBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_film,
                parent,
                false);
        return new FilmViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(FilmViewHolder holder, int position) {
        final Film note = mList.get(position);
        holder.bind(note);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class FilmViewHolder extends RecyclerView.ViewHolder {
        private ItemFilmBinding binding;

        public FilmViewHolder(ItemFilmBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Film film) {
            binding.setFilm(film);
            binding.executePendingBindings();
        }
    }
}
