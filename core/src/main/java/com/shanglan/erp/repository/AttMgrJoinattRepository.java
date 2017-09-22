package com.shanglan.erp.repository;

import com.shanglan.erp.entity.AttMgrJoinatt;
import com.shanglan.erp.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**
 * 参加考勤人员
 */
public interface AttMgrJoinattRepository extends JpaRepository<AttMgrJoinatt,Integer> {

    /**
     * 查询所有考勤人员
     * @return
     */
    @Query("SELECT a FROM AttMgrJoinatt a WHERE a.isJoinAtt = true AND a.machineId = 1 AND a.importKey > 0 ORDER BY a.deptId ASC ")
    List<AttMgrJoinatt> findAllJoinatt();

    AttMgrJoinatt findByUid(Integer uid);
}
