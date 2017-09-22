package com.shanglan.erp.dto;

import com.shanglan.erp.entity.Category;

import java.util.List;

/**
 * Created by cuishiying on 2017/7/14.
 */
public class CategoryTreeDTO {
    private Category topCategory;
    private List<Category> subCategory;

    public Category getTopCategory() {
        return topCategory;
    }

    public void setTopCategory(Category topCategory) {
        this.topCategory = topCategory;
    }

    public List<Category> getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(List<Category> subCategory) {
        this.subCategory = subCategory;
    }
}
