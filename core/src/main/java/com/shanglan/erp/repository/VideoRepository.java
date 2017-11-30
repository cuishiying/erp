package com.shanglan.erp.repository;

import com.shanglan.erp.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface VideoRepository extends JpaRepository<Video,Integer>{

    /**
     * 重置转码状态
     * @return
     */
    @Modifying
    @Query("update Video v set v.transcoding = false")
    Integer resettingTranscoding();

}
