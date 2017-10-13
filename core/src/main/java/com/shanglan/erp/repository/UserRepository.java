package com.shanglan.erp.repository;

import com.shanglan.erp.entity.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserRepository {
    @Resource
    private JdbcTemplate jdbcTemplate;
    public User findUserByUsernameAndtruename(String username, String truename){
        String sql="select * from cnoa_main_user u where u.username=? and u.truename=?";
        User user = null;
        try{
            user = jdbcTemplate.queryForObject(sql, new Object[]{username, truename}, new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet resultSet, int i) throws SQLException {
                    User u = new User();
                    u.setDeptId(resultSet.getInt("deptId"));
                    u.setUid(resultSet.getInt("uid"));
                    u.setUsername(resultSet.getString("username"));
                    u.setTruename(resultSet.getString("truename"));
                    return u;
                }
            });
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        return user;
    }

    public User findByUid(Integer uid) {
        String sql="select * from cnoa_main_user u where u.uid=?";

        User user = jdbcTemplate.queryForObject(sql, new Object[]{uid}, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User u = new User();
                u.setDeptId(resultSet.getInt("deptId"));
                u.setUid(resultSet.getInt("uid"));
                u.setUsername(resultSet.getString("username"));
                u.setTruename(resultSet.getString("truename"));
                return u;
            }
        });
        return user;
    }

}
