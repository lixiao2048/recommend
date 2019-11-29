package com.bingo.qa.model;

import lombok.Data;

/**
 */
@Data
public class TopUser {
    private int id;
    private int userid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

}
