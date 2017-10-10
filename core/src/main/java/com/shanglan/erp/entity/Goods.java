package com.shanglan.erp.entity;

import com.shanglan.erp.base.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.persistence.*;

/**
 * Created by cuishiying on 2017/7/14.
 */
@Entity
@Table(name = "cnoa_jxc_goods")
public class Goods extends BaseEntity{

    private static final long serialVersionUID = 344356902788050285L;

    private Integer goodOrder;
    private Boolean checked;
    private String goodsname;//存货名称
    private Integer manager;//管理员id
    private String unit;//单位
    private String standard;//规格型号
    private Integer sid;//货品分类id
    private double price;//单价
    @Column(unique = true)
    private String goodsCode;//存货编码（货品编码）

    private String goodsdm;//存货代码
    private String usedate;//启用日期
    private String subcode;//存货大类编码（存货编码-二级分类）
    private String abc;//abc分类
    private String unitgroupcode;//计量单位组编码
    private String unitcode;//主计量单位编码

    private String openingInventoryQuantity;//期初结存数量
    private String openingBalance;//期初结存金额
    private Integer storageId;//
    private String unitgroupname;//计量单位组名称

    public Integer getGoodOrder() {
        return goodOrder;
    }

    public void setGoodOrder(Integer goodOrder) {
        this.goodOrder = goodOrder;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public Integer getManager() {
        return manager;
    }

    public void setManager(Integer manager) {
        this.manager = manager;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsdm() {
        return goodsdm;
    }

    public void setGoodsdm(String goodsdm) {
        this.goodsdm = goodsdm;
    }

    public String getUsedate() {
        return usedate;
    }

    public void setUsedate(String usedate) {
        this.usedate = usedate;
    }

    public String getSubcode() {
        return subcode;
    }

    public void setSubcode(String subcode) {
        this.subcode = subcode;
    }

    public String getAbc() {
        return abc;
    }

    public void setAbc(String abc) {
        this.abc = abc;
    }

    public String getUnitgroupcode() {
        return unitgroupcode;
    }

    public void setUnitgroupcode(String unitgroupcode) {
        this.unitgroupcode = unitgroupcode;
    }

    public String getUnitcode() {
        return unitcode;
    }

    public void setUnitcode(String unitcode) {
        this.unitcode = unitcode;
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

    public Integer getStorageId() {
        return storageId;
    }

    public void setStorageId(Integer storageId) {
        this.storageId = storageId;
    }

    public String getUnitgroupname() {
        return unitgroupname;
    }

    public void setUnitgroupname(String unitgroupname) {
        this.unitgroupname = unitgroupname;
    }
}
