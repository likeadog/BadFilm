package com.zhuang.badfilm.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuang.badfilm.R;

/**
 * Created by zhuang on 2017/7/25.
 */

public class LikeButton extends LinearLayout {

    ImageView icon;
    TextView likeCount;
    TextView addText;
    boolean hasLike;//是否已经点赞过
    OnLikeClickListener likeClickListener;

    public LikeButton(Context context) {
        super(context);
        init(context);
    }

    public LikeButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LikeButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        inflate(context, R.layout.like_button, this);
        icon = (ImageView) findViewById(R.id.icon);
        likeCount = (TextView) findViewById(R.id.likeCount);
        addText = (TextView) findViewById(R.id.addText);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasLike) {
                    addText.setVisibility(View.VISIBLE);
                    setLikeCount();
                    setColor();
                    setAnimtor();
                    if (likeClickListener != null) {
                        likeClickListener.likeClick();
                    }
                    hasLike = true;
                } else {
                    Toast.makeText(context, "您已经点赞过", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //设置点赞数
    public void setLikeCountText(String text) {
        if (text != null) {
            likeCount.setText(text);
        }
    }

    //是否点赞过
    public void setHasLike(boolean ihasLike) {
        this.hasLike = ihasLike;
        if (hasLike) {
            setColor();
        }else{
            setColorGray();
        }
    }

    private void setColor() {
        icon.setImageResource(R.drawable.ic_like_red);
        likeCount.setTextColor(Color.parseColor("#d81e06"));
    }

    private void setColorGray() {
        icon.setImageResource(R.drawable.ic_like_gray);
        likeCount.setTextColor(Color.parseColor("#8a8a8a"));
    }

    private void setLikeCount() {
        if (TextUtils.isEmpty(likeCount.getText())) {
            likeCount.setText("1");
        } else {
            int count = Integer.parseInt(likeCount.getText().toString());
            count++;
            likeCount.setText(count + "");
        }
    }

    private void setAnimtor() {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(addText, View.ALPHA, 0f, 1f);
        alpha.setDuration(300);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(addText, View.SCALE_X, 0.2f, 1.5f);
        scaleX.setDuration(500);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(addText, View.SCALE_Y, 0.2f, 1.5f);
        scaleY.setDuration(500);
        ObjectAnimator alpha1 = ObjectAnimator.ofFloat(addText, View.ALPHA, 1f, 0f);
        alpha1.setDuration(200);
        alpha1.setStartDelay(300);
        alpha.start();
        scaleX.start();
        scaleY.start();
        alpha1.start();
    }

    public void setOnLikeClickListener(OnLikeClickListener likeClickListener) {
        this.likeClickListener = likeClickListener;
    }

    public interface OnLikeClickListener {
        void likeClick();
    }
}
