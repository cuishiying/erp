package com.shanglan.erp.controller;

import com.shanglan.erp.base.AjaxResponse;
import com.shanglan.erp.base.ExcelUtils;
import com.shanglan.erp.dto.CategoryTreeDTO;
import com.shanglan.erp.dto.ExcelBean;
import com.shanglan.erp.dto.GoodsDTO;
import com.shanglan.erp.entity.Category;
import com.shanglan.erp.entity.Collect;
import com.shanglan.erp.entity.Config;
import com.shanglan.erp.entity.Goods;
import com.shanglan.erp.service.ErpService;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

import static sun.net.www.protocol.http.HttpURLConnection.userAgent;

@RestController
@RequestMapping(path = "/erp")
public class ErpController {
    @Autowired
    private ErpService erpService;

    //***********************************货品分类********************************************************************************

    /**
     * 分类结构树
     * @return
     */
    @RequestMapping(path = "/category/tree")
    public ModelAndView index(){
        ModelAndView model = new ModelAndView("erp_category");
        List<CategoryTreeDTO> category = erpService.findCategory();
        List<Category> allCategory = erpService.findAllCategory();
        model.addObject("category",category);
        model.addObject("all",allCategory);
        return model;
    }

    /**
     * 添加分类
     * @param category
     * @return
     */
    @RequestMapping(path = "/category/add",method = RequestMethod.POST)
    public AjaxResponse addCategory(@RequestBody Category category){
        AjaxResponse response = erpService.save(category);
        return response;
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @RequestMapping(path = "/category/delete/{id}",method = RequestMethod.GET)
    public AjaxResponse deleteCategory(@PathVariable Integer id){
        AjaxResponse ajaxResponse = erpService.deleteCategory(id);
        return ajaxResponse;
    }

    /**
     * 获取二级分类列表
     * @param id
     * @return
     */
    @RequestMapping(path = "/goods/subCategory",method = RequestMethod.POST)
    public AjaxResponse findSubCategory(@RequestParam Integer id){
        List<Category> subCategory = erpService.findSubCategory(id);
        return AjaxResponse.success(subCategory);
    }

    //*****************************************货品档案**************************************************************************

    /**
     * 货品列表
     * @param keyword
     * @param pageable
     * @return
     */
    @RequestMapping(path = "/goods/list",method = RequestMethod.GET)
    public ModelAndView list(String keyword,@PageableDefault(value = 10,sort = "id",direction = Sort.Direction.DESC) Pageable pageable){
        ModelAndView model = new ModelAndView("erp_goods");
        Page<GoodsDTO> page = erpService.findGoodsByPage(keyword,pageable);
        List<CategoryTreeDTO> category = erpService.findCategory();
        model.addObject("category",category);
        model.addObject("page",page);
        model.addObject("keyword",keyword);
        return model;
    }


    /**
     * 添加货品
     * @param goods
     * @return
     */
    @RequestMapping(path = "/goods/add",method = RequestMethod.POST)
    public AjaxResponse addGoods(@RequestBody Goods goods){
        AjaxResponse response = erpService.save(goods);
        return response;
    }

    /**
     * 删除货品
     * @param id
     * @return
     */
    @RequestMapping(path = "/goods/delete/{id}",method = RequestMethod.GET)
    public AjaxResponse deleteGoods(@PathVariable Integer id){
        AjaxResponse ajaxResponse = erpService.deleteGoods(id);
        return ajaxResponse;
    }



    /**
     * 批量导入货品
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(path = "/goods/importGoods", method = RequestMethod.POST)
    public AjaxResponse<?> importGoods(HttpServletRequest request) throws Exception{
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("upfile");
        if(file.isEmpty()){
            throw new Exception("文件不存在！");
        }
        InputStream in = file.getInputStream();
        AjaxResponse ajaxResponse = erpService.importGoods(in, file);
        return ajaxResponse;
    }

    /**
     * 将数据库中的数据导出为excel
     *
     * @return
     */
    @RequestMapping(path = "/goods/export",method = RequestMethod.GET)
    public AjaxResponse exportGoods(HttpServletRequest request, HttpServletResponse response) {
        response.reset(); //清除buffer缓存
        erpService.exportGoods(response);
        return AjaxResponse.success();
    }



    //******************************************收发存*************************************************************************

    /**
     * 收发存汇总
     * @param keyword
     * @param pageable
     * @return
     */
    @RequestMapping(path = "/collect",method = RequestMethod.GET)
    public ModelAndView collect(String keyword,@PageableDefault(value = 10,sort = "id",direction = Sort.Direction.DESC) Pageable pageable){
        ModelAndView model = new ModelAndView("erp_collect");
        Page<Collect> collect = erpService.findCollectByPage(keyword,pageable);
        List<CategoryTreeDTO> category = erpService.findCategory();
        Config config = erpService.findConfig();
        model.addObject("category",category);
        model.addObject("page",collect);
        model.addObject("keyword",keyword);
        model.addObject("config",config);
        return model;
    }
    /**
     * 初始导入收发存汇总
     * @return
     * @throws Exception
     */
    @RequestMapping(path = "/collect/initImport", method = RequestMethod.POST)
    public AjaxResponse<?> importCollect(HttpServletRequest request) throws Exception{
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("upfile");
        if(file.isEmpty()){
            throw new Exception("文件不存在！");
        }
        InputStream in = file.getInputStream();
        AjaxResponse ajaxResponse = erpService.importCollect(in, file);
        return ajaxResponse;
    }

    /**
     * 将数据库中的数据导出为excel
     *
     * @return
     */
    @RequestMapping(path = "/collect/export",method = RequestMethod.GET)
    public AjaxResponse exportCollect(HttpServletRequest request, HttpServletResponse response) {
        response.reset(); //清除buffer缓存
        erpService.exportCollect(response);
        return AjaxResponse.success();
    }


    @RequestMapping(path = "/collect/chongku",method = RequestMethod.POST)
    public AjaxResponse redbase(@RequestParam Integer storageId,@RequestParam Integer goodsId,@RequestParam Integer count){
        AjaxResponse response = erpService.redbase(storageId, goodsId, count);
        return response;
    }

    //******************************************退库*************************************************************************

    /**
     * 出库列表
     * @return
     */
    @RequestMapping(path = "/chuku/list",method = RequestMethod.GET)
    public ModelAndView chukuListView(String keyword,@PageableDefault(value = 10,sort = "id",direction = Sort.Direction.DESC) Pageable pageable){
        ModelAndView modelAndView = new ModelAndView("erp_chukulist");
        modelAndView.addObject("chukulist",erpService.findChukuList(keyword));
        modelAndView.addObject("keyword",keyword);
        return modelAndView;
    }

    /**
     * 出库表单详情
     * @param uFlowId
     * @return
     */
    @RequestMapping(path = "/chuku/form/{uFlowId}")
    public ModelAndView chukuFormView(@PathVariable Integer uFlowId){
        ModelAndView modelAndView = new ModelAndView("erp_chukuform");
        modelAndView.addObject("chukuHead",erpService.findWfChukuHead(uFlowId));
        modelAndView.addObject("chukuItemList",erpService.findWfChukuItem(uFlowId));
        return modelAndView;
    }


    /**
     * 删除货品记录
     * @param uFlowId
     * @param bindid
     * @return
     */
    @RequestMapping(path = "/chuku/delete",method = RequestMethod.POST)
    public AjaxResponse deleteChukuItem(@RequestParam Integer uFlowId,@RequestParam Integer bindid){
        AjaxResponse response = erpService.deleteChukuItem(uFlowId, bindid);
        return response;
    }
}
