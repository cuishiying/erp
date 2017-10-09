package com.shanglan.erp.entity;

import com.shanglan.erp.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 收发存汇总
 */
@Entity
@Table(name = "cnoa_jxc_collect")
public class Collect extends BaseEntity{
    private static final long serialVersionUID = -5867032945832980286L;
    private String storageName;//仓库名称
    private String goodsCode;//存货编码
    private String storageCode;//存货代码
    private String goodsname;//存货名称
    private String standard;//规格型号
    private String unit;//主计量单位
    private String goodsSubCode;//存货大类编码
    private String inventoryClassificationName ;//存货分类名称
    private String openingInventoryQuantity;//期初结存数量
    private String openingBalance;//期初结存金额
    private String stockInCount;//总计入库数量
    private String stockInBalance;//总计入库金额
    private String stockOutCount;//总计出库数量
    private String stockOutBalance;//总计出库金额
    private String endingInventoryQuantity;//期末结存数量
    private String endingBalance;//期末结存金额
    private Integer goodsId;
    private Integer storageId;
    private Integer chongkuCount;
    @Column(name = "uFlowId")
    private Integer uFlowId;

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getStorageCode() {
        return storageCode;
    }

    public void setStorageCode(String storageCode) {
        this.storageCode = storageCode;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getGoodsSubCode() {
        return goodsSubCode;
    }

    public void setGoodsSubCode(String goodsSubCode) {
        this.goodsSubCode = goodsSubCode;
    }

    public String getInventoryClassificationName() {
        return inventoryClassificationName;
    }

    public void setInventoryClassificationName(String inventoryClassificationName) {
        this.inventoryClassificationName = inventoryClassificationName;
    }

    public String getOpeningInventoryQuantity() {
        return openingInventoryQuantity;
    }

    public void setOpeningInventoryQuantity(String openingInventoryQuantity) {
        this.openingInventoryQuantity = openingInventoryQuantity;
    }

    public String getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(String openingBalance) {
        this.openingBalance = openingBalance;
    }

    public String getStockInCount() {
        return stockInCount;
    }

    public void setStockInCount(String stockInCount) {
        this.stockInCount = stockInCount;
    }

    public String getStockInBalance() {
        return stockInBalance;
    }

    public void setStockInBalance(String stockInBalance) {
        this.stockInBalance = stockInBalance;
    }

    public String getStockOutCount() {
        return stockOutCount;
    }

    public void setStockOutCount(String stockOutCount) {
        this.stockOutCount = stockOutCount;
    }

    public String getStockOutBalance() {
        return stockOutBalance;
    }

    public void setStockOutBalance(String stockOutBalance) {
        this.stockOutBalance = stockOutBalance;
    }

    public String getEndingInventoryQuantity() {
        return endingInventoryQuantity;
    }

    public void setEndingInventoryQuantity(String endingInventoryQuantity) {
        this.endingInventoryQuantity = endingInventoryQuantity;
    }

    public String getEndingBalance() {
        return endingBalance;
    }

    public void setEndingBalance(String endingBalance) {
        this.endingBalance = endingBalance;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getStorageId() {
        return storageId;
    }

    public void setStorageId(Integer storageId) {
        this.storageId = storageId;
    }

    public Integer getChongkuCount() {
        return chongkuCount;
    }

    public void setChongkuCount(Integer chongkuCount) {
        this.chongkuCount = chongkuCount;
    }

    public Integer getuFlowId() {
        return uFlowId;
    }

    public void setuFlowId(Integer uFlowId) {
        this.uFlowId = uFlowId;
    }
}
