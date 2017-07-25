package com.zhuang.badfilm.film;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhuang.badfilm.R;
import com.zhuang.badfilm.databinding.ItemCommentBinding;
import com.zhuang.badfilm.model.Comment;
import com.zhuang.badfilm.view.LikeButton;

import java.util.List;

/**
 * Created by zhuang on 2017/7/20.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> mList;
    OnViewClickListener onViewClickListener;

    public CommentAdapter(List<Comment> list) {
        this.mList = list;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemCommentBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_comment,
                parent,
                false);
        return new CommentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        final Comment comment = mList.get(position);
        holder.bind(comment,position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        private ItemCommentBinding binding;

        public CommentViewHolder(ItemCommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final Comment comment,final int position) {
            binding.setComment(comment);
            binding.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onViewClickListener.delete(position);
                }
            });
            binding.like.setOnLikeClickListener(new LikeButton.OnLikeClickListener(){
                @Override
                public void likeClick() {
                    comment.addLikeCount();
                    comment.setHasLike(true);
                    onViewClickListener.like();
                }
            });

            binding.executePendingBindings();
        }
    }

    public void setOnViewClickListener(OnViewClickListener onViewClickListener) {
        this.onViewClickListener = onViewClickListener;
    }

    public interface OnViewClickListener{
        void delete(int position);//删除
        void like();//点赞
        void getUser();//获取用户信息
    }

}