package com.shanglan.erp.service;


import com.shanglan.erp.base.AjaxResponse;
import com.shanglan.erp.base.ExcelUtils;
import com.shanglan.erp.config.Constance;
import com.shanglan.erp.dto.*;
import com.shanglan.erp.dto.User;
import com.shanglan.erp.entity.*;
import com.shanglan.erp.repository.*;
import com.shanglan.erp.utils.JavaUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

import static sun.net.www.protocol.http.HttpURLConnection.userAgent;

@Service
@Transactional
public class ErpService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SortRepository sortRepository;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private ErpStorageRepository erpStorageRepository;
    @Autowired
    private CollectRepository collectRepository;
    @Autowired
    private ConfigRepository configRepository;


    //*****************************************货品分类**************************************************************************

    /**
     * 保存分类
     * @param category
     * @return
     */
    public AjaxResponse save(Category category){
        if(StringUtils.isEmpty(category.getParentName())){
            category.setParentId(null);
            category.setParentName(null);
        }else{
            Category c = categoryRepository.findByName(category.getParentName());
            category.setParentId(c.getId());
        }
        categoryRepository.save(category);
        return AjaxResponse.success("创建成功");
    }
    public AjaxResponse deleteCategory(Integer id){
        categoryRepository.deleteCategories(id);
        return AjaxResponse.success();
    }

    /**
     * 查找1级分类
     * @return
     */
    public List<Category> findTopCategory(){
        List<Category> topCategory = categoryRepository.findTopCategory();
        return topCategory;
    }

    /**
     * 查找2级分类
     * @param parentId
     * @return
     */
    public List<Category> findSubCategory(Integer parentId){
        List<Category> subCategory = categoryRepository.findSubCategory(parentId);
        return subCategory;
    }

    /**
     * 查找所有分类
     * @return
     */
    public List<Category> findAllCategory(){
        List<Category> all = categoryRepository.findAllCategory();
        return all;
    }

    /**
     * 找到所有分类
     * @return
     */
    public List<CategoryTreeDTO> findCategory(){
        List<Category> topCategory = this.findTopCategory();
        List<CategoryTreeDTO> list = new ArrayList<>();
        for (Category c:topCategory) {
            CategoryTreeDTO categoryTreeDTO = new CategoryTreeDTO();
            List<Category> subCategory = this.findSubCategory(c.getId());
            categoryTreeDTO.setTopCategory(c);
            categoryTreeDTO.setSubCategory(subCategory);
            list.add(categoryTreeDTO);
        }
        return list;
    }


    //*****************************************货品档案**************************************************************************

    /**
     * 获取所有货品
     * @param keyword
     * @param pageable
     * @return
     */
    public Page<GoodsDTO> findGoodsByPage(String keyword, Pageable pageable){

        String sql="select * from cnoa_jxc_goods as c WHERE 1=1";
        List<Goods> list = goodsRepository.findGoods(sql,keyword,pageable);
        Integer totle = goodsRepository.findGoodsTotle(sql, keyword);
        Page<Goods> page = new PageImpl<Goods>(list, pageable, totle);
        Page<GoodsDTO> page1 = page.map(new Converter<Goods, GoodsDTO>() {
            @Override
            public GoodsDTO convert(Goods goods) {
                GoodsDTO dto = new GoodsDTO();
                dto.setGoods(goods);
                return dto;
            }
        });
        return page1;
    }

    public List<Goods> findGoods(String keyword){

        String sql="select * from cnoa_jxc_goods as c WHERE 1=1";
        List<Goods> list = goodsRepository.findGoods(sql,keyword,null);
        return list;
    }

    /**
     * 新增货品
     * @param goods
     * @return
     */
    public AjaxResponse save(Goods goods){
        Category sub = categoryRepository.findOne(Integer.parseInt(goods.getSubcode()));
        Sort sort = sortRepository.findAll().get(0);

        goods.setSubcode(sub.getCode());//二级编码
        goods.setManager(1);
        goods.setUsedate(JavaUtils.convert2long(goods.getUsedate())+"");//启用日期
        goods.setSid(sort.getId());//货品分类id
        AjaxResponse response = goodsRepository.save(goods);
        return response;
    }

    public AjaxResponse deleteGoods(Integer id){
        AjaxResponse ajaxResponse = goodsRepository.deleteGoods(id);
        return ajaxResponse;
    }


    /**
     * 导入货品档案
     * @param in
     * @param file
     * @return
     * @throws Exception
     */
    public AjaxResponse importGoods(InputStream in, MultipartFile file) throws Exception {

        List<List<Object>> listob = ExcelUtils.getBankListByExcel(in,file.getOriginalFilename());
        List<Goods> goodsList=new ArrayList<>();

        for (int i = 0; i < listob.size(); i++) {
            List<Object> lo = listob.get(i);
            Goods goods = new Goods();
            goods.setGoodsCode(String.valueOf(lo.get(2)));//存货编码（货品编码）
            goods.setGoodsname(String.valueOf(lo.get(3)));//货品名称
            goods.setStandard(String.valueOf(lo.get(4)));//规格
            goods.setGoodsdm(String.valueOf(lo.get(5)));//存货代码
            goods.setUnit(String.valueOf(lo.get(6)));//单位
            goods.setSubcode(String.valueOf(lo.get(7)));//二级编码
            goods.setUnitgroupname(String.valueOf(lo.get(8)));//计量单位组名称
            goods.setAbc(String.valueOf(lo.get(9)));//abc分类
            goods.setUsedate(JavaUtils.convert2long(String.valueOf(lo.get(10)))+"");//启用日期
            goods.setUnitgroupcode(String.valueOf(lo.get(11)));//计量单位组编码
            goods.setUnitcode(String.valueOf(lo.get(12)));//主计量单位编码
            goods.setManager(1);
            goods.setSid(sortRepository.findAll().get(0).getId());//货品分类id
            goodsList.add(goods);
        }
        goodsRepository.batchSave(goodsList);
        return AjaxResponse.success();
    }

    public void exportGoods(HttpServletResponse response){
        String fileName = "货品档案";
        String sheetName = "货品档案";
        List<Goods> list = this.findGoods(null);
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("序号","goodOrder");
        map.put("选择","checked");
        map.put("存货编码","goodsCode");
        map.put("存货名称","goodsname");
        map.put("规格型号","standard");
        map.put("存货代码","goodsdm");
        map.put("主计量单位名称","unit");
        map.put("存货大类编码","subcode");
        map.put("计量单位组名称","unitgroupname");
        map.put("ABC分类","abc");
        map.put("启用日期","usedate");
        map.put("计量单位组编码","unitgroupcode");
        map.put("主计量单位编码","unitcode");
        ExcelUtils.export(fileName,sheetName,Goods.class,list,map,response);
    }


    //*****************************************收发存*************************************************************************

    /**
     * 冲库
     * @return
     */
    public AjaxResponse redbase(Integer storageId,Integer goodsId,Integer count){
        String nowLong = JavaUtils.getNowLong();
        goodsRepository.redbase(nowLong,storageId,goodsId,count);
        return AjaxResponse.success();
    }

    public Page<Collect> findCollectByPage(String keyword, Pageable pageable){

        List<Collect> list = goodsRepository.findCollect(keyword, pageable);
        Integer totle = goodsRepository.findCollectTotle(keyword);
        Page<Collect> page = new PageImpl<Collect>(list, pageable, totle);
        return page;
    }
    /**
     * 收发存列表
     * @return
     */
    public List<Collect> findCollect(String keyword){

        List<Collect> collect = goodsRepository.findCollect(keyword,null);
        return collect;
    }

    /**
     * 初始导入收发存汇总表
     * @param in
     * @param file
     * @return
     * @throws Exception
     */
    public AjaxResponse importCollect(InputStream in, MultipartFile file) throws Exception {

        goodsRepository.truncateTable();

        String nowLong = JavaUtils.getNowLong();

        List<Storage> sList = erpStorageRepository.findAll();
        Constance.setStorageConfig(sList);

        List<List<Object>> listob = ExcelUtils.getBankListByExcel(in,file.getOriginalFilename());
        List<Goods> goodsList=new ArrayList<>();

        for (int i = 0; i < listob.size(); i++) {
            List<Object> lo = listob.get(i);
            Goods goods = new Goods();
            goods.setGoodsCode(String.valueOf(lo.get(1)));//存货编码（货品编码）
            goods.setGoodsdm(String.valueOf(lo.get(2)));//存货代码
            goods.setGoodsname(String.valueOf(lo.get(3)));//货品名称
            goods.setStandard(String.valueOf(lo.get(4)));//规格
            goods.setUnit(String.valueOf(lo.get(5)));//单位
            goods.setSubcode(String.valueOf(lo.get(6)));//二级编码
            goods.setOpeningInventoryQuantity(String.valueOf(lo.get(14)));//期初结存数量
            goods.setStorageId(Constance.getStoragenameConfig(String.valueOf(lo.get(0))));
            goods.setSid(sortRepository.findAll().get(0).getId());//货品分类id
            goods.setUsedate(nowLong);
            goodsList.add(goods);
        }
        goodsRepository.batchSave(goodsList);
        goodsRepository.saveInitCount();
        return AjaxResponse.success();
    }

    public void exportCollect(HttpServletResponse response){
        String fileName = "收发存汇总表";
        String sheetName = "收发存汇总表";
        List<Collect> collect = this.findCollect(null);
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("仓库名称","storageName");
        map.put("存货编码","goodsCode");
        map.put("存货代码","storageCode");
        map.put("存货名称","goodsname");
        map.put("规格型号","standard");
        map.put("主计量单位名称","unit");
        map.put("存货大类编码","goodsSubCode");
        map.put("存货分类名称","inventoryClassificationName");
        map.put("期初结存数量","openingInventoryQuantity");
        map.put("期初结存金额","openingBalance");
        map.put("总计入库数量","stockInCount");
        map.put("总计入库金额","stockInBalance");
        map.put("总计出库数量","stockOutCount");
        map.put("总计出库金额","stockOutBalance");
        map.put("期末结存数量","endingInventoryQuantity");
        map.put("期末结存金额","endingBalance");

        ExcelUtils.export(fileName,sheetName,Collect.class,collect,map,response);
    }


    //*****************************************退库*************************************************************************

    /**
     * 出库列表
     */
    public List<ErpChuku> findChukuList(String keyword){
        if(null==keyword){
            keyword = "";
        }
        List<ErpChuku> chukuList = goodsRepository.findChukuList(keyword);
        return chukuList;
    }

    /**
     * 出库表头
     * @param uFlowId
     * @return
     */
    public WfChukuHead findWfChukuHead(Integer uFlowId){
        WfChukuHead wfHead = goodsRepository.findWfHead(uFlowId);
        User user = goodsRepository.findUser(wfHead.getUid());
        wfHead.setTruename(user.getTruename());
        return wfHead;
    }

    /**
     * 出库商品明细
     * @param uFlowId
     * @return
     */
    public List<WfChukuItem> findWfChukuItem(Integer uFlowId){
        List<WfChukuItem> wfItem = goodsRepository.findWfItem(uFlowId);
        return wfItem;
    }

    public AjaxResponse deleteChukuItem(Integer uFlowId,Integer bindid){
        AjaxResponse response = goodsRepository.deleteChukuItem(uFlowId, bindid);
        return response;
    }



    //*****************************************初始化数据库*************************************************************************

    /**
     * truncate是删除表中所有数据（还会重置自增长字段）
     */
    public void truncateTable(){
        goodsRepository.truncateTable();
    }

    public Config findConfig(){
        Config one = configRepository.findOne(1);
        if(null==one){
            one = new Config();
            one.setShowImpBtn(true);
        }
        return one;
    }
}
