package com.shanglan.erp.entity;

import com.shanglan.erp.base.BaseEntity;

import javax.persistence.*;

/**
 * 需考勤人员
 */
@Entity
@Table(name = "cnoa_att_mgr_joinatt")
public class AttMgrJoinatt{

    private static final long serialVersionUID = -2870411479301063133L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uid;
    private String truename;
    @Column(name="deptId")
    private Integer deptId;
    @Column(name="isJoinAtt")
    private Boolean isJoinAtt;
    @Column(name="machineId")
    private Integer machineId;
    @Column(name="importKey")
    private Integer importKey;
    private Integer ykkq;
    private Integer pc;
    private Integer phone;
    @Column(name="addressId")
    private Integer addressId;
    @Column(name="checkNotice")
    private Boolean checkNotice;
    @Column(name="deviceId")
    private Integer deviceId;
    private String deptname;

    private Integer cid;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public Boolean getJoinAtt() {
        return isJoinAtt;
    }

    public void setJoinAtt(Boolean joinAtt) {
        isJoinAtt = joinAtt;
    }

    public Integer getMachineId() {
        return machineId;
    }

    public void setMachineId(Integer machineId) {
        this.machineId = machineId;
    }

    public Integer getImportKey() {
        return importKey;
    }

    public void setImportKey(Integer importKey) {
        this.importKey = importKey;
    }

    public Integer getYkkq() {
        return ykkq;
    }

    public void setYkkq(Integer ykkq) {
        this.ykkq = ykkq;
    }

    public Integer getPc() {
        return pc;
    }

    public void setPc(Integer pc) {
        this.pc = pc;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Boolean getCheckNotice() {
        return checkNotice;
    }

    public void setCheckNotice(Boolean checkNotice) {
        this.checkNotice = checkNotice;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }
}
