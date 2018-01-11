package com.shanglan.erp.repository;

import com.shanglan.erp.base.AjaxResponse;
import com.shanglan.erp.config.Constance;
import com.shanglan.erp.dto.ErpChuku;
import com.shanglan.erp.dto.User;
import com.shanglan.erp.dto.WfChukuHead;
import com.shanglan.erp.dto.WfChukuItem;
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
import org.springframework.web.bind.annotation.RequestMapping;

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


    //*****************************************仓库**************************************************************************

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
     * @return
     */
    public Integer findGoodsTotle(String sql,String keyword){
        String dm = env.getProperty("goods.dm");
        String usedate = env.getProperty("goods.usedate");
        String subcode = env.getProperty("goods.subcode");
        String abc = env.getProperty("goods.abc");
        String unitgroupcode = env.getProperty("goods.unitgroupcode");
        String unitcode = env.getProperty("goods.unitcode");

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
                return null;
            }
        });
        return list.size();
    }
    public List<Goods> findGoods(String sql,String keyword,Pageable pageable) {

        if(null==keyword){
            keyword = "";
        }

        String dm = env.getProperty("goods.dm");
        String usedate = env.getProperty("goods.usedate");
        String subcode = env.getProperty("goods.subcode");
        String abc = env.getProperty("goods.abc");
        String unitgroupcode = env.getProperty("goods.unitgroupcode");
        String unitcode = env.getProperty("goods.unitcode");

        List <Object> queryList=new ArrayList<Object>();
        if (keyword!=null&&!keyword.equals("")) {
            sql += " and c.goodsCode like ? or c.goodsname LIKE ?";
            queryList.add("%" + keyword + "%");
            queryList.add("%" + keyword + "%");
        }
        sql+=" GROUP BY c.goodsCode";

        if(null!=pageable){
            sql += " limit "+pageable.getPageSize()+" offset "+pageable.getPageSize()*pageable.getPageNumber();
        }

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

                goods.setGoodsdm(resultSet.getString(dm)==null?"":resultSet.getString(dm));
                goods.setUsedate(JavaUtils.convert2String(resultSet.getString(usedate)));
                goods.setSubcode(resultSet.getString(subcode));
                goods.setAbc(resultSet.getString(abc));
                goods.setUnitgroupcode(resultSet.getString(unitgroupcode));
                goods.setUnitcode(resultSet.getString(unitcode));
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

    public Integer findCollectTotle(String keyword){
        String sql = "SELECT count(0) FROM cnoa_jxc_collect c WHERE 1=1";
        List <Object> queryList=new ArrayList<Object>();
        if (keyword!=null&&!keyword.equals("")) {
            sql += " AND c.goodsCode like ? or c.goodsname LIKE ?";
            queryList.add("%" + keyword + "%");
            queryList.add("%" + keyword + "%");
        }
        sql += " GROUP BY c.storageName,c.goodsCode";
        List<Collect> query = jdbcTemplate.query(sql, queryList.toArray(), new RowMapper<Collect>() {

            @Override
            public Collect mapRow(ResultSet resultSet, int i) throws SQLException {
                return null;
            }
        });
        return query.size();
    }

    public List<Collect> findCollect(String keyword, Pageable pageable){
        if(null==keyword){
            keyword = "";
        }

        String sql = "SELECT c.storageId,c.goodsId,c.storageName,c.goodsCode,c.storageCode,c.goodsname,c.standard,c.unit,c.goodsSubCode,c.inventoryClassificationName,c.openingInventoryQuantity,c.openingBalance,SUM(c.stockInCount) AS sic,SUM(c.stockInBalance) AS sib,SUM(c.stockOutCount) AS soc,SUM(c.stockOutBalance) AS sob,SUM(c.endingInventoryQuantity) AS eiq,SUM(c.endingBalance) AS eb,SUM(c.chongkuCount) AS ck FROM cnoa_jxc_collect c WHERE 1=1";
        List <Object> queryList=new ArrayList<Object>();
        if (keyword!=null&&!keyword.equals("")) {
            sql += " AND c.goodsCode like ? or c.goodsname LIKE ?";
            queryList.add("%" + keyword + "%");
            queryList.add("%" + keyword + "%");
        }
        sql += " GROUP BY c.storageName,c.goodsCode ORDER BY c.goodsCode";
        if(null!=pageable){
            sql += " limit "+pageable.getPageSize()+" offset "+pageable.getPageSize()*pageable.getPageNumber();
        }
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
        List<Goods> all = findGoods(qsql, null, null);

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
     * 收发存初始化表格
     */
    public void truncateTable(){

        String cnoa_z_wf_d = env.getProperty("wf.d.cnoa_z_wf_d");
        String cnoa_z_wf_t = env.getProperty("wf.t.cnoa_z_wf_t");


        String sql1 = "TRUNCATE TABLE cnoa_jxc_goods;";
        String sql2 = "TRUNCATE TABLE cnoa_jxc_stock_goods_detail;";
        String sql3 = "TRUNCATE TABLE cnoa_jxc_collect;";
        String sql4 = "TRUNCATE TABLE "+cnoa_z_wf_t+";";
        String sql5 = "TRUNCATE TABLE "+cnoa_z_wf_d+";";
        String sql6 = "TRUNCATE TABLE cnoa_jxc_stock_chuku;";
        String sql7 = "TRUNCATE TABLE cnoa_jxc_stock_ruku;";
        try{
            jdbcTemplate.execute(sql1);
            jdbcTemplate.execute(sql2);
            jdbcTemplate.execute(sql3);
            jdbcTemplate.execute(sql4);
            jdbcTemplate.execute(sql5);
            jdbcTemplate.execute(sql6);
            jdbcTemplate.execute(sql7);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //*****************************************退库**************************************************************************

    /**
     * 出库列表
     * @return
     */
    public List<ErpChuku> findChukuList(String keyword){

        String sql = "select c.id,c.indentnumber,c.posttime,c.type,c.uid,c.storageid,c.status,c.uFlowId,s.storagename from cnoa_jxc_stock_chuku AS c LEFT JOIN cnoa_jxc_storage AS s ON c.storageid = s.id WHERE 1=1 AND c.status=1";

        List <Object> queryList=new ArrayList<Object>();
        if (keyword!=null&&!keyword.equals("")) {
            sql += " AND c.indentnumber like ?";
            queryList.add("%" + keyword + "%");
        }

        sql += " ORDER BY c.posttime DESC ";
        List<ErpChuku> query = jdbcTemplate.query(sql,queryList.toArray(), new RowMapper<ErpChuku>() {
            @Override
            public ErpChuku mapRow(ResultSet resultSet, int i) throws SQLException {
                ErpChuku erpChuku = new ErpChuku();
                erpChuku.setId(resultSet.getInt("id"));
                erpChuku.setIndentnumber(resultSet.getString("indentnumber"));
                erpChuku.setPosttime(JavaUtils.convert2DateTime(resultSet.getString("posttime")));
                erpChuku.setType(resultSet.getInt("type"));
                erpChuku.setUid(resultSet.getInt("uid"));
                erpChuku.setStorageid(resultSet.getInt("storageid"));
                erpChuku.setStatusId(resultSet.getInt("status"));
                erpChuku.setStatus(resultSet.getInt("status")==1?"审批结束":"审批中");
                erpChuku.setuFlowId(resultSet.getInt("uFlowId"));
                erpChuku.setStorageName(resultSet.getString("storagename"));
                return erpChuku;
            }
        });
        return query;
    }


    /**
     * 出库明细表
     * @param uFlowId
     * @return
     */
    public List<WfChukuItem> findWfItem(Integer uFlowId){

        String codeNum = env.getProperty("wf.d.code");
        String totleprice = env.getProperty("wf.d.totleprice");
        String price = env.getProperty("wf.d.price");
        String name = env.getProperty("wf.d.name");
        String classify = env.getProperty("wf.d.classify");
        String standard = env.getProperty("wf.d.standard");
        String unit = env.getProperty("wf.d.unit");
        String count = env.getProperty("wf.d.count");
        String cnoa_z_wf_d = env.getProperty("wf.d.cnoa_z_wf_d");

        List<WfChukuItem> query = null;

        String sql = "select * from "+cnoa_z_wf_d+" as c where c.uFlowId = ?";

        try{
            query = jdbcTemplate.query(sql, new Object[]{uFlowId}, new RowMapper<WfChukuItem>() {

                @Override
                public WfChukuItem mapRow(ResultSet resultSet, int i) throws SQLException {
                    WfChukuItem wfChukuItem = new WfChukuItem();
                    wfChukuItem.setFid(resultSet.getInt("fid"));
                    wfChukuItem.setuFlowId(resultSet.getInt("uFlowId"));
                    wfChukuItem.setRowid(resultSet.getInt("rowid"));
                    wfChukuItem.setBindid(resultSet.getInt("bindid"));
                    wfChukuItem.setCodeNum(resultSet.getString(codeNum));
                    wfChukuItem.setTotleprice(resultSet.getFloat(totleprice));
                    wfChukuItem.setPrice(resultSet.getFloat(price));
                    wfChukuItem.setName(resultSet.getString(name));
                    wfChukuItem.setClassify(resultSet.getString(classify));
                    wfChukuItem.setStandard(resultSet.getString(standard));
                    wfChukuItem.setUnit(resultSet.getString(unit));
                    wfChukuItem.setCount(resultSet.getInt(count));
                    return wfChukuItem;
                }
            });
        }catch (Exception e){
            query = new ArrayList<>();
            return query;
        }

        return query;
    }

    public WfChukuHead findWfHead(Integer uFlowId){

        String storagename = env.getProperty("wf.t.storagename");
        String flowdate = env.getProperty("wf.t.flowdate");
        String cnoa_z_wf_t = env.getProperty("wf.t.cnoa_z_wf_t");

        WfChukuHead head = null;

        String sql = "select * from "+cnoa_z_wf_t+" as c where c.uFlowId = ?";
        try{
            head = jdbcTemplate.queryForObject(sql,new Object[]{uFlowId}, new RowMapper<WfChukuHead>() {
                @Override
                public WfChukuHead mapRow(ResultSet resultSet, int i) throws SQLException {
                    WfChukuHead wfChukuHead = new WfChukuHead();
                    wfChukuHead.setFid(resultSet.getInt("fid"));
                    wfChukuHead.setuFlowId(resultSet.getInt("uFlowId"));
                    wfChukuHead.setFlowNumber(resultSet.getString("flowNumber"));
                    wfChukuHead.setFlowName(resultSet.getString("flowName"));
                    wfChukuHead.setUid(resultSet.getInt("uid"));
                    wfChukuHead.setStatus(resultSet.getInt("status"));
                    wfChukuHead.setLevel(resultSet.getInt("level"));
                    wfChukuHead.setReason(resultSet.getString("reason"));
                    wfChukuHead.setPosttime(JavaUtils.convert2DateTime(resultSet.getString("posttime")));
                    wfChukuHead.setEndtime(JavaUtils.convert2DateTime(resultSet.getString("endtime")));
                    wfChukuHead.setStorageName(resultSet.getString(storagename));
                    wfChukuHead.setFlowDate(resultSet.getString(flowdate));
                    return wfChukuHead;
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return head;
    }

    public User findUser(Integer uid){
        String sql = "select * from cnoa_main_user as c where c.uid = ?";
        User user = jdbcTemplate.queryForObject(sql, new Object[]{uid}, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                User user = new User();
                user.setUid(resultSet.getInt("uid"));
                user.setUsername(resultSet.getString("username"));
                user.setTruename(resultSet.getString("truename"));
                return user;
            }
        });

        return user;
    }

    /**
     * 退库商品回退
     * 注意，出库时一个出库单只能对应一个仓库（目前无法区分仓库）
     * @param uFlowId
     * @param bindid
     * @return
     */
    public AjaxResponse deleteChukuItem(Integer uFlowId,Integer bindid){

        String cnoa_z_wf_d = env.getProperty("wf.d.cnoa_z_wf_d");

        String sqlform ="DELETE FROM "+cnoa_z_wf_d+" WHERE uFlowId = ? AND bindid = ?";
        String sqldetail ="DELETE FROM cnoa_jxc_stock_goods_detail WHERE uFlowId = ? AND goodsId = ?";
        int form = jdbcTemplate.update(sqlform, new Object[]{uFlowId, bindid});
        int detail = jdbcTemplate.update(sqldetail, new Object[]{uFlowId, bindid});
        return AjaxResponse.success("退库成功");
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


}
