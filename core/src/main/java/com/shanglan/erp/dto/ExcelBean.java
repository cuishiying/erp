package com.shanglan.erp.dto;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import java.io.Serializable;

/**
 * Created by cuishiying on 2017/6/13.
 */
public class ExcelBean implements Serializable {
    private String headTextName;//列头（标题）名
    private String propertyName;//对应字段名
    private Integer cols;//合并列单元格数  （水平）
    private Integer rows;//合并行单元格数（竖直）
    private XSSFCellStyle cellStyle;

    public ExcelBean(){

    }
    public ExcelBean(String headTextName, String propertyName){
        this.headTextName = headTextName;
        this.propertyName = propertyName;
    }

    public ExcelBean(String headTextName, String propertyName, Integer cols) {
        super();
        this.headTextName = headTextName;
        this.propertyName = propertyName;
        this.cols = cols;
    }

    public ExcelBean(String headTextName, String propertyName, Integer rows, Integer cols) {
        this.headTextName = headTextName;
        this.propertyName = propertyName;
        this.cols = cols;
        this.rows = rows;
    }

    public String getHeadTextName() {
        return headTextName;
    }

    public void setHeadTextName(String headTextName) {
        this.headTextName = headTextName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Integer getCols() {
        return cols;
    }

    public void setCols(Integer cols) {
        this.cols = cols;
    }

    public XSSFCellStyle getCellStyle() {
        return cellStyle;
    }

    public void setCellStyle(XSSFCellStyle cellStyle) {
        this.cellStyle = cellStyle;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }
}
