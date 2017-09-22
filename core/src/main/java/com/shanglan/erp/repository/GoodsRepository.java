package com.shanglan.erp.repository;

import com.shanglan.erp.base.AjaxResponse;
import com.shanglan.erp.config.Constance;
import com.shanglan.erp.entity.Collect;
import com.shanglan.erp.entity.Goods;
import com.shanglan.erp.entity.Storage;
import com.shanglan.erp.utils.JavaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by cuishiying on 2017/7/14.
 */
@Repository
public class GoodsRepository {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private Environment env;
    @Autowired
    private ErpStorageRepository erpStorageRepository;


    //*****************************************货品档案**************************************************************************

    /**
     * 根据货品id查找仓库名字（相同货品去重）
     * @param goodsId
     * @return
     */
    public String findStorage(Integer goodsId){
        String sql="select * from cnoa_jxc_storage s,cnoa_jxc_stock_goods_detail g where g.storageId = s.id and g.goodsId = ?";
        List<Storage> storage = jdbcTemplate.query(sql, new Object[]{goodsId}, new RowMapper<Storage>() {

            @Override
            public Storage mapRow(ResultSet resultSet, int i) throws SQLException {
                Storage storage = new Storage();
                storage.setId(resultSet.getInt("id"));
                storage.setStoragename(resultSet.getString("storagename"));
                return storage;
            }
        });
        return storage==null||storage.size()==0?"":storage.get(0).getStoragename();
    }





    //*****************************************货品档案**************************************************************************

    /**
     * 保存货品
     * @param goods
     * @return
     */
    public AjaxResponse save(Goods goods){

        String dm = env.getProperty("goods.dm");
        String usedate = env.getProperty("goods.usedate");
        String subcode = env.getProperty("goods.subcode");
        String abc = env.getProperty("goods.abc");
        String unitgroupcode = env.getProperty("goods.unitgroupcode");
        String unitcode = env.getProperty("goods.unitcode");

        String sql="INSERT ignore into cnoa_jxc_goods (goodsname,manager,unit,standard,sid,price,goodsCode,"+dm+","+usedate+","+subcode+","+abc+","+unitgroupcode+","+unitcode+") values (?,?,?,?,?,?,?,?,?,?,?,?,?) ON duplicate KEY UPDATE goodsCode = goodsCode ";
        int count= jdbcTemplate.update(sql, new Object[]{goods.getGoodsname(),goods.getManager(),goods.getUnit(),goods.getStandard(),goods.getSid(),goods.getPrice(),goods.getGoodsCode(),goods.getGoodsdm(),goods.getUsedate(),goods.getSubcode(),goods.getAbc(),goods.getUnitgroupcode(),goods.getUnitcode()});
        return AjaxResponse.success(count);
    }
    /**
     * 删除货品
     * @return
     */
    public AjaxResponse deleteGoods(Integer id){


        String sql="DELETE FROM cnoa_jxc_goods WHERE id = ?";
        int count= jdbcTemplate.update(sql, new Object[]{id});
        return AjaxResponse.success(count);
    }


    /**
     * 查找所有货品并去重
     * @param keyword
     * @param pageable
     * @return
     */
    public List<Goods> findAllGoods(String sql,String keyword,Pageable pageable) {

        List <Object> queryList=new ArrayList<Object>();
        if (keyword!=null&&!keyword.equals("")) {
            sql += " and c.goodsCode like ? or c.goodsname LIKE ?";
            queryList.add("%" + keyword + "%");
            queryList.add("%" + keyword + "%");
        }
        sql+=" GROUP BY c.goodsCode";

        List<Goods> list = jdbcTemplate.query(sql, queryList.toArray(),new RowMapper<Goods>() {

            @Override
            public Goods mapRow(ResultSet resultSet, int i) throws SQLException {
                Goods goods = new Goods();
                goods.setId(resultSet.getInt("id"));
                goods.setGoodsname(resultSet.getString("goodsname"));
                goods.setManager(resultSet.getInt("manager"));
                goods.setUnit(resultSet.getString("unit"));
                goods.setStandard(resultSet.getString("standard"));
                goods.setSid(resultSet.getInt("sid"));
                goods.setPrice(resultSet.getDouble("price"));
                goods.setGoodsCode(resultSet.getString("goodsCode"));
                goods.setOpeningInventoryQuantity(resultSet.getString("openingInventoryQuantity"));
                goods.setStorageId(resultSet.getInt("storageId"));

                goods.setGoodsdm(resultSet.getString("field6"));
                goods.setUsedate(JavaUtils.convert2String(resultSet.getString("field13")));
                goods.setSubcode(resultSet.getString("field1"));
                goods.setAbc(resultSet.getString("field7"));
                goods.setUnitgroupcode(resultSet.getString("field9"));
                goods.setUnitcode(resultSet.getString("field10"));
                return goods;
            }
        });
        return list;
    }


    /**
     * 批量导入货品
     * @param list
     * @return
     *
     */
    public AjaxResponse<?> batchSave(final List<Goods> list){

        String dm = env.getProperty("goods.dm");
        String usedate = env.getProperty("goods.usedate");
        String subcode = env.getProperty("goods.subcode");
        String abc = env.getProperty("goods.abc");
        String unitgroupcode = env.getProperty("goods.unitgroupcode");
        String unitcode = env.getProperty("goods.unitcode");
        String unitgroupname = env.getProperty("goods.unitgroupname");

        final List<Goods> tempBpplist = list;
        String sql="insert ignore into cnoa_jxc_goods(goodsCode,goodsname,standard,unit,sid,manager,"+dm+","+usedate+","+subcode+","+abc+","+unitgroupcode+","+unitcode+","+unitgroupname+",openingInventoryQuantity,storageId)" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.batchUpdate(sql,new BatchPreparedStatementSetter() {

            @Override
            public int getBatchSize() {
                return tempBpplist.size();
            }
            @Override
            public void setValues(PreparedStatement ps, int i)
                    throws SQLException {
                ps.setString(1, tempBpplist.get(i).getGoodsCode());
                ps.setString(2, tempBpplist.get(i).getGoodsname());
                ps.setString(3, tempBpplist.get(i).getStandard());
                ps.setString(4, tempBpplist.get(i).getUnit());
                ps.setInt(5, tempBpplist.get(i).getSid()==null?-1:tempBpplist.get(i).getSid());
                ps.setDouble(6, tempBpplist.get(i).getManager()==null?1:tempBpplist.get(i).getManager());

                ps.setString(7, tempBpplist.get(i).getGoodsdm());
                ps.setString(8, tempBpplist.get(i).getUsedate());
                ps.setString(9, tempBpplist.get(i).getSubcode());
                ps.setString(10, tempBpplist.get(i).getAbc());
                ps.setString(11, tempBpplist.get(i).getUnitgroupcode());
                ps.setString(12, tempBpplist.get(i).getUnitcode());
                ps.setString(13, tempBpplist.get(i).getUnitgroupname());
                ps.setString(14, tempBpplist.get(i).getOpeningInventoryQuantity()==null?null:tempBpplist.get(i).getOpeningInventoryQuantity());
                ps.setInt(15,tempBpplist.get(i).getStorageId()==null?-1:tempBpplist.get(i).getStorageId());
            }
        });
        return AjaxResponse.success();
    }

    //*****************************************收发存**************************************************************************

    public List<Collect> findCollect(String keyword, Pageable pageable){

        String sql = "SELECT c.storageId,c.goodsId,c.storageName,c.goodsCode,c.storageCode,c.goodsname,c.standard,c.unit,c.goodsSubCode,c.inventoryClassificationName,c.openingInventoryQuantity,c.openingBalance,SUM(c.stockInCount) AS sic,SUM(c.stockInBalance) AS sib,SUM(c.stockOutCount) AS soc,SUM(c.stockOutBalance) AS sob,SUM(c.endingInventoryQuantity) AS eiq,SUM(c.endingBalance) AS eb,SUM(c.chongkuCount) AS ck FROM cnoa_jxc_collect c WHERE 1=1";
        List <Object> queryList=new ArrayList<Object>();
        if (keyword!=null&&!keyword.equals("")) {
            sql += " AND c.goodsCode like ? or c.goodsname LIKE ?";
            queryList.add("%" + keyword + "%");
            queryList.add("%" + keyword + "%");
        }
        sql += " GROUP BY c.storageName,c.goodsCode ORDER BY c.goodsCode";
        List<Collect> query = jdbcTemplate.query(sql,queryList.toArray(), new RowMapper<Collect>() {

            @Override
            public Collect mapRow(ResultSet resultSet, int i) throws SQLException {
                Collect collect = new Collect();
                collect.setStorageId(resultSet.getInt("storageId"));
                collect.setGoodsId(resultSet.getInt("goodsId"));
                collect.setStorageName(resultSet.getString("storageName"));
                collect.setGoodsCode(resultSet.getString("goodsCode"));
                collect.setStorageCode(resultSet.getString("storageCode"));
                collect.setGoodsname(resultSet.getString("goodsname"));
                collect.setStandard(resultSet.getString("standard"));
                collect.setUnit(resultSet.getString("unit"));
                collect.setGoodsSubCode(resultSet.getString("goodsSubCode"));
                collect.setInventoryClassificationName(resultSet.getString("inventoryClassificationName"));

                collect.setOpeningInventoryQuantity(resultSet.getString("openingInventoryQuantity"));
                collect.setOpeningBalance(resultSet.getString("openingBalance"));

                int initCount = 0;
                int chongkuCount = 0;
                if(resultSet.getString("openingInventoryQuantity")!=null){
                    initCount = Integer.parseInt(resultSet.getString("openingInventoryQuantity"));
                }
                if(resultSet.getString("ck")!=null){
                    chongkuCount = Integer.parseInt(resultSet.getString("ck"));
                }
                collect.setStockInCount(String.valueOf(Integer.parseInt(resultSet.getString("sic"))-initCount-chongkuCount));
                collect.setStockInBalance(resultSet.getString("sib"));
                collect.setStockOutCount(resultSet.getString("soc")==null?"0":resultSet.getString("soc"));
                collect.setStockOutBalance(resultSet.getString("sob"));
                collect.setEndingInventoryQuantity(resultSet.getString("eiq"));
                collect.setEndingBalance(resultSet.getString("eb"));
                collect.setChongkuCount(chongkuCount);
                return collect;
            }
        });
        return query;
    }

    /**
     * 收发存汇总导入数量
     */
    public void saveInitCount(){
        String qsql="select * from cnoa_jxc_goods as c where c.openingInventoryQuantity is not null";
        List<Goods> all = findAllGoods(qsql, null, null);

        String sql="insert ignore into cnoa_jxc_stock_goods_detail(posttime,storageId,goodsId,quantity,price,sum,openingInventoryQuantity,initCount) values(?,?,?,?,?,?,?,?)";
        jdbcTemplate.batchUpdate(sql,new BatchPreparedStatementSetter() {

            @Override
            public int getBatchSize() {
                return all.size();
            }
            @Override
            public void setValues(PreparedStatement ps, int i)
                    throws SQLException {
                ps.setString(1, JavaUtils.getNowLong());
                ps.setInt(2, all.get(i).getStorageId());
                ps.setInt(3, all.get(i).getId());
                ps.setString(4, all.get(i).getOpeningInventoryQuantity());
                ps.setString(5, "");
                ps.setString(6, "");
                ps.setString(7, all.get(i).getOpeningInventoryQuantity());
                ps.setInt(8, 0);
            }
        });

    }

    /**
     * 冲库插入
     * @param posttime
     * @param storageId
     * @param goodsId
     * @param quantity
     */
    public void redbase(String posttime,Integer storageId,Integer goodsId,Integer quantity){
        String sql="insert ignore into cnoa_jxc_stock_goods_detail(posttime,storageId,goodsId,quantity,chongkuCount) values(?,?,?,?,?)";
        jdbcTemplate.update(sql,new Object[]{posttime,storageId,goodsId,quantity,quantity});
    }

    public Integer truncateTable(){
        String sql = "delete from cnoa_jxc_goods";
        Integer integer = jdbcTemplate.queryForObject(sql, Integer.class);
        return integer;
    }
}
