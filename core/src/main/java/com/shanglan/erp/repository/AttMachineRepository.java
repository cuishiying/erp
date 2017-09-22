package com.shanglan.erp.repository;

import com.shanglan.erp.entity.AttMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 考勤机原始记录
 */
public interface AttMachineRepository extends JpaRepository<AttMachine,Integer> {



    /**
     * 查询一天打卡记录
     * @return
     */
    @Query("SELECT a FROM AttMachine a WHERE a.enrollNum = ?1 AND a.time >= ?2 AND a.time <= ?3")
    List<AttMachine> findAllMachineRecord(Integer enrollNum,Long t1,Long t2);

    @Query("SELECT a.time FROM AttMachine a WHERE a.enrollNum = ?1 AND a.time >= ?2 AND a.time <= ?3")
    List<Long> findMachineRecord(Integer enrollNum,Long t1,Long t2);

    /**
     * 判断指定时间内是否有打卡记录
     * @param t1    00:00:00
     * @param t2
     * @return
     */
    @Query("SELECT COUNT(1) FROM AttMachine a WHERE a.enrollNum = ?1 AND a.time >= ?2 AND a.time <= ?3")
    Integer isAttendance(Integer enrollNum,Long t1,Long t2);
}
