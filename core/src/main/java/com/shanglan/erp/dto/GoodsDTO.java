package com.shanglan.erp.dto;

import com.shanglan.erp.entity.Category;
import com.shanglan.erp.entity.Goods;

/**
 * Created by cuishiying on 2017/7/15.
 */
public class GoodsDTO {
    private Goods goods;//货品
    private Integer openingInventoryQuantity;//期初结存数量
    private Integer openingBalance;//期初结存金额
    private Integer stockInCount;//总计入库数量
    private Integer stockInBalance;//总计入库金额
    private Integer stockOutCount;//总计出库数量
    private Integer stockOutBalance;//总计出库金额
    private Integer endingInventoryQuantity;//期末结存数量
    private Integer endingBalance;//期末结存金额
    private String storage;//仓库
    private Category subCategory;//所属分类

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public Integer getOpeningInventoryQuantity() {
        return openingInventoryQuantity;
    }

    public void setOpeningInventoryQuantity(Integer openingInventoryQuantity) {
        this.openingInventoryQuantity = openingInventoryQuantity;
    }

    public Integer getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(Integer openingBalance) {
        this.openingBalance = openingBalance;
    }

    public Integer getStockInCount() {
        return stockInCount;
    }

    public void setStockInCount(Integer stockInCount) {
        this.stockInCount = stockInCount;
    }

    public Integer getStockInBalance() {
        return stockInBalance;
    }

    public void setStockInBalance(Integer stockInBalance) {
        this.stockInBalance = stockInBalance;
    }

    public Integer getStockOutCount() {
        return stockOutCount;
    }

    public void setStockOutCount(Integer stockOutCount) {
        this.stockOutCount = stockOutCount;
    }

    public Integer getStockOutBalance() {
        return stockOutBalance;
    }

    public void setStockOutBalance(Integer stockOutBalance) {
        this.stockOutBalance = stockOutBalance;
    }

    public Integer getEndingInventoryQuantity() {
        return endingInventoryQuantity;
    }

    public void setEndingInventoryQuantity(Integer endingInventoryQuantity) {
        this.endingInventoryQuantity = endingInventoryQuantity;
    }

    public Integer getEndingBalance() {
        return endingBalance;
    }

    public void setEndingBalance(Integer endingBalance) {
        this.endingBalance = endingBalance;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public Category getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(Category subCategory) {
        this.subCategory = subCategory;
    }
}
