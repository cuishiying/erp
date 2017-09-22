package com.shanglan.erp.repository;

import com.shanglan.erp.entity.Category;
import com.shanglan.erp.entity.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by cuishiying on 2017/7/13.
 */
public interface ErpStorageRepository extends JpaRepository<Storage,Integer> {

    Storage findByStoragename(String name);

}
