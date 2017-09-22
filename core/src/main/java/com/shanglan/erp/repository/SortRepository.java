package com.shanglan.erp.repository;

import com.shanglan.erp.entity.Goods;
import com.shanglan.erp.entity.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by cuishiying on 2017/7/17.
 */
@Repository
public class SortRepository {
    @Resource
    private JdbcTemplate jdbcTemplate;

    public List<Sort> findAll() {
        String sql="select * from cnoa_jxc_sort";

        List<Sort> list = jdbcTemplate.query(sql, new RowMapper<Sort>() {

            @Override
            public Sort mapRow(ResultSet resultSet, int i) throws SQLException {
                Sort sort = new Sort();
                sort.setId(resultSet.getInt("id"));
                sort.setName(resultSet.getString("name"));
                sort.setPid(resultSet.getInt("pid"));
                sort.setOrder(resultSet.getInt("order"));
                return sort;
            }
        });
        return list;
    }
}
