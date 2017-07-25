package com.zhuang.badfilm.model;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhuang on 2017/7/10.
 */

public class Film implements Serializable {

    private String id;
    private String name;
    private String info;
    private String image;
    private String desc;
    private String star;
    private String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /**
     * list列表中的info显示
     *
     * @return
     */
    public String getInfoShowInItem() {
        String[] infoArr = info.split(",");
        String director = infoArr[0];//导演
        String[] actorArr = infoArr[2].split("/");
        String actor;
        if(actorArr.length >= 3){
             actor = actorArr[0] + "/" + actorArr[1] + "/" + actorArr[2];//前三个演员
        }else{
            actor = infoArr[2];
        }
        return director + "\n" + actor;
    }

    public float getStarFloat(){
        if(TextUtils.isEmpty(star)){
            return 0;
        }else{
            return Float.parseFloat(star);
        }
    }

}
