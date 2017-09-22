package com.shanglan.erp.repository;

import com.shanglan.erp.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by cuishiying on 2017/7/13.
 */
public interface CategoryRepository extends JpaRepository<Category,Integer> {

    /**
     * 查找所有一级分类
     * @return
     */
    @Query("select c from Category c where c.parentId is null")
    List<Category> findTopCategory();

    /**
     * 查找所有二级分类
     * @return
     */
    @Query("select c from Category c where c.parentId=?1")
    List<Category> findSubCategory(Integer parentId);


    Category findByName(String name);
    Category findByCode(String code);

    @Modifying
    @Transactional
    @Query("delete from Category c where  c.id = ?1 or c.parentId = ?1")
    void deleteCategories(Integer id);

}
