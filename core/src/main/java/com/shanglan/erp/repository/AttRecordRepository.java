package com.shanglan.erp.repository;

import com.shanglan.erp.dto.AttForm;
import com.shanglan.erp.entity.AttRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttRecordRepository extends JpaRepository<AttRecord,Integer> {


    /**
     * 统计月度出勤天数
     */
    @Query("SELECT SUM(attendanceDay) FROM AttRecord a WHERE a.uid = ?1 AND a.date >= ?2 AND a.date <= ?3")
    Integer countAttendanceDay(Integer uid,String firstDay,String lastDay);


    /**
     * 获取一月统计数据
     */
    @Query("SELECT a FROM AttRecord a WHERE a.uid = ?1 AND a.date >= ?2 AND a.date <= ?3 ORDER BY a.date ASC ")
    List<AttRecord> findMonthAttendance(Integer uid, String firstDay, String lastDay);

    /**
     * 查询某员工某天是否有考勤记录
     * @param uid
     * @param date
     * @return
     */
    @Query("SELECT a FROM AttRecord a WHERE a.uid = ?1 AND a.date = ?2")
    AttRecord findAttRecordByUidAndDate(Integer uid,String date);

    AttRecord findByUid(Integer uid);

}
