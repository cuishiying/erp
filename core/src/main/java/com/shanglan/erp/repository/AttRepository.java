package com.shanglan.erp.repository;

import com.shanglan.erp.dto.AttForm;
import com.shanglan.erp.entity.AttJoinAttRecord;
import com.shanglan.erp.entity.Goods;
import com.shanglan.erp.entity.Sort;
import com.shanglan.erp.utils.JavaUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Repository
public class AttRepository {
    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 所有的打卡记录
     * @return
     */
    public List<AttJoinAttRecord> findAll() {
        String sql="SELECT j.truename,j.importKey,m.timeStr FROM cnoa_att_mgr_joinatt AS j LEFT JOIN cnoa_att_machine AS m ON j.importKey=m.enrollNum AND j.machineId=m.machineId WHERE m.upToOA = 0 AND m.isNew = 1 AND j.importKey > 0 ORDER BY m.time";

        List<AttJoinAttRecord> list = jdbcTemplate.query(sql, new RowMapper<AttJoinAttRecord>() {

            @Override
            public AttJoinAttRecord mapRow(ResultSet resultSet, int i) throws SQLException {
                AttJoinAttRecord joinAtt = new AttJoinAttRecord();
                joinAtt.setTruename(resultSet.getString("truename"));
                joinAtt.setImportKey(resultSet.getString("importKey"));
                joinAtt.setTimeStr(resultSet.getString("timeStr"));
                return joinAtt;
            }
        });
        return list;
    }

    /**
     * 根据用户和日期查找记录
     * @param uid
     * @return
     */
    public List<AttJoinAttRecord> findByUidAndDate(Integer uid,long date){
        String sql="SELECT c.truename,c.date,c.time FROM cnoa_att_user_checktime AS c WHERE c.uid = ? AND c.date = ?";


        List<AttJoinAttRecord> list = jdbcTemplate.query(sql,new Object[]{uid,date},new RowMapper<AttJoinAttRecord>() {

            @Override
            public AttJoinAttRecord mapRow(ResultSet resultSet, int i) throws SQLException {
                AttJoinAttRecord joinAtt = new AttJoinAttRecord();
                joinAtt.setTruename(resultSet.getString("truename"));
                joinAtt.setDate(resultSet.getString("date"));
                joinAtt.setTime(resultSet.getString("time"));
                return joinAtt;
            }
        });
        return list;

    }

    /**
     * 根据用户和日期查找记录
     * @param uid
     * @return
     */
    public List<AttJoinAttRecord> findOne(Integer uid, long date){
        String sql="SELECT c.truename,c.date,c.time,j.importKey,c.cname,c.cnum,ac.oneStime,ac.oneEtime FROM cnoa_att_user_checktime AS c,cnoa_att_mgr_joinatt AS j,cnoa_att_classes ac WHERE c.uid = j.uid AND ac.name = c.cname AND c.uid = ? AND c.date = ?";


        List<AttJoinAttRecord> list = jdbcTemplate.query(sql,new Object[]{uid,date},new RowMapper<AttJoinAttRecord>() {

            @Override
            public AttJoinAttRecord mapRow(ResultSet resultSet, int i) throws SQLException {
                AttJoinAttRecord joinAtt = new AttJoinAttRecord();
                joinAtt.setImportKey(resultSet.getString("importKey"));
                joinAtt.setCname(resultSet.getString("cname"));
                joinAtt.setCnum(resultSet.getInt("cnum"));
                joinAtt.setTruename(resultSet.getString("truename"));
                joinAtt.setDate(resultSet.getString("date"));
                joinAtt.setTime(resultSet.getString("time"));
                joinAtt.setOneStime(resultSet.getString("oneStime"));
                joinAtt.setOneEtime(resultSet.getString("oneEtime"));
                return joinAtt;
            }
        });
        return list;

    }

    /**
     * 统计报表
     * @return
     */
    public List<AttForm> statisticalForms(Integer year,Integer month){

        Calendar now = Calendar.getInstance();
        int nowYear = now.get(Calendar.YEAR);
        int nowMonth = now.get(Calendar.MONTH) + 1;


        String firstDayOfMonth = JavaUtils.getFirstDayOfMonth(year, month);
        String lastDayOfMonth = JavaUtils.getLastDayOfMonth(year, month);
        int count = JavaUtils.getMonthLastDay(year, month);

        String sql = "SELECT a.truename,a.dept,SUM(a.attendanceDay) AS rs FROM cnoa_att_record AS a WHERE a.date >= ? AND a.date <= ? GROUP BY a.truename,a.dept ORDER BY a.dept";

        List<AttForm> list = jdbcTemplate.query(sql,new Object[]{firstDayOfMonth,lastDayOfMonth},new RowMapper<AttForm>() {

            @Override
            public AttForm mapRow(ResultSet resultSet, int i) throws SQLException {
                AttForm attForm = new AttForm();
                attForm.setTruename(resultSet.getString("truename"));
                attForm.setDept(resultSet.getString("dept"));
                attForm.setWorkAttendance(resultSet.getFloat("rs"));
                attForm.setFullWorkAttendance(count);
                if(year==nowYear&&month==nowMonth){
                    Float r = JavaUtils.getIndexOfCurrentMonth() - resultSet.getFloat("rs");
                    attForm.setAbsence(r);
                }else{
                    attForm.setAbsence(count-resultSet.getFloat("rs"));
                }
                return attForm;
            }
        });
        return list;
    }


}
