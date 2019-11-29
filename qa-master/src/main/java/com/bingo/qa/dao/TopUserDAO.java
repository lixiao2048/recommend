package com.bingo.qa.dao;

import com.bingo.qa.model.TopUser;
import com.bingo.qa.model.User;
import org.apache.ibatis.annotations.*;

/**
 */
@Mapper
public interface TopUserDAO {

    String TABLE_NAME = " top_user ";
    String INSERT_FIELDS = "userid ";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;


    /**
     * 添加优秀的用户
     * @param topUser
     */
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") values(#{userid})"})
    void addUser(TopUser topUser);

    /**
     * 根据排名id查询用户id
     * @param id
     * @return topUser
     */
    @Select({"select", SELECT_FIELDS, " from ", TABLE_NAME, " where id = #{id}"})
    TopUser selectById(int id);


    /**
     * 根据id删除用户
     * @param id
     */
    @Delete({"delete from ", TABLE_NAME, " where id = #{id}"})
    void deleteById(int id);

}
