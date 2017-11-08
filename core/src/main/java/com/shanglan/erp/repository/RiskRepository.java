package com.shanglan.erp.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by cuishiying on 2017/7/14.
 */
@Repository
public class RiskRepository {
    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 初始化table
     */
    public void truncateTable(){


        String sql3 = "TRUNCATE TABLE cnoa_risk_condition;";
        String sql2 = "TRUNCATE TABLE cnoa_risk_item;";
        String sql1 = "TRUNCATE TABLE cnoa_risk_value;";
        try{
            jdbcTemplate.execute(sql1);
            jdbcTemplate.execute(sql2);
            jdbcTemplate.execute(sql3);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
