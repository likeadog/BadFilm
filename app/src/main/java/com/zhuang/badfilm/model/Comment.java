package com.zhuang.badfilm.model;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by zhuang on 2017/7/20.
 * 评论
 */

public class Comment implements Serializable {

    private String id;
    private String parentid;
    private String subjectid;
    private String content;
    private String star;
    private String name;
    private String uid;//用户id
    private String time;
    private String avatar;//用户头像
    private String likeCount = "8";
    private boolean hasLike;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(String subjectid) {
        this.subjectid = subjectid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isHasLike() {
        return hasLike;
    }

    public void setHasLike(boolean hasLike) {
        this.hasLike = hasLike;
    }

    public float getStarFloat() {
        if (TextUtils.isEmpty(star)) {
            return 0;
        } else {
            return Float.parseFloat(star);
        }
    }

    public void addLikeCount() {
        if (likeCount == null) {
            likeCount = "1";
        }else{
            Integer count = Integer.parseInt(likeCount);
            count++;
            likeCount = count+"";
        }
    }
}
