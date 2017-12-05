package com.shanglan.erp.controller;

import com.shanglan.erp.base.AjaxResponse;
import com.shanglan.erp.dto.HiddenTroubleDTO;
import com.shanglan.erp.dto.HiddenTroubleResultDTO;
import com.shanglan.erp.entity.HiddenTrouble;
import com.shanglan.erp.entity.HiddenTroubleItem;
import com.shanglan.erp.entity.HiddenTroubleResult;
import com.shanglan.erp.enums.HiddenTroubleLevels;
import com.shanglan.erp.service.HiddenTroubleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 隐患排查
 */
@RestController
@RequestMapping("/hiddenTrouble")
public class HiddenTroubleController {

    @Autowired
    private HiddenTroubleService hiddenTroubleService;

    @RequestMapping(path = "/list",method = RequestMethod.GET)
    public ModelAndView hiddenTroubleListView(String username, String truename, HiddenTroubleDTO hiddenTroubleDTO, @PageableDefault(value = 10,sort = "createTime",direction = Sort.Direction.DESC) Pageable pageable, HttpServletRequest request){
        ModelAndView model = new ModelAndView("hiddentrouble_list");
        Page<HiddenTrouble> page = hiddenTroubleService.findAll(hiddenTroubleDTO,pageable);
        model.addObject("page",page);
        model.addObject("query",hiddenTroubleDTO);
        return model;
    }

    /**
     * 上拉加载数据
     * @param pageable
     * @return
     */
    @RequestMapping(path = "list/data",method = RequestMethod.GET)
    public AjaxResponse hiddenTrouble(HiddenTroubleDTO hiddenTroubleDTO,@PageableDefault(value = 10,sort = "createTime",direction = Sort.Direction.DESC) Pageable pageable){
        Page<HiddenTrouble> page = hiddenTroubleService.findAll(hiddenTroubleDTO,pageable);
        return AjaxResponse.success(page);
    }

    @RequestMapping(path = "/detail/{id}",method = RequestMethod.GET)
    public ModelAndView hiddenTroubleDetail(@PathVariable Integer id, String username, String truename, HttpServletRequest request){
        ModelAndView model = new ModelAndView("hiddentrouble_detail");
        HiddenTrouble hiddenTrouble = hiddenTroubleService.findById(id);
        int size = hiddenTrouble.getHiddentroubles().size();
        if(size<16){
            for(int i=0;i<16-size;i++){
                HiddenTroubleItem hiddenTroubleItem = new HiddenTroubleItem();
                hiddenTrouble.getHiddentroubles().add(hiddenTroubleItem);
            }
        }
        model.addObject("hiddenTroubleId",id);
        model.addObject("hiddenTrouble",hiddenTrouble);
        return model;
    }
    @RequestMapping(path = "/delete/{id}",method = RequestMethod.GET)
    public AjaxResponse delete(@PathVariable Integer id){
        AjaxResponse response = hiddenTroubleService.delete(id);
        return response;
    }

    @RequestMapping(path = "/add",method = RequestMethod.GET)
    public ModelAndView addView(){
        ModelAndView model = new ModelAndView("hiddentrouble_add");
        model.addObject("level", HiddenTroubleLevels.values());
        model.addObject("curMonth", new Date());
        return model;
    }

    @RequestMapping(path = "/add",method = RequestMethod.POST)
    public AjaxResponse add(@RequestBody HiddenTrouble hiddenTrouble,HttpServletRequest request){
        Integer uid = (Integer) request.getSession().getAttribute("uid");
        AjaxResponse response = hiddenTroubleService.save(uid,hiddenTrouble);
        return response;
    }

    @RequestMapping(path = "/result/list",method = RequestMethod.GET)
    public ModelAndView resultListView(HiddenTroubleResultDTO hiddenTroubleResultDTO,@PageableDefault(value = 10,sort = "publicTime",direction = Sort.Direction.DESC) Pageable pageable){
        ModelAndView model = new ModelAndView("hiddentrouble_result_list");
        Page<HiddenTroubleResult> page = hiddenTroubleService.findAll(hiddenTroubleResultDTO,pageable);
        model.addObject("page",page);
        model.addObject("query",hiddenTroubleResultDTO);
        return model;
    }
    @RequestMapping(path = "/result/add",method = RequestMethod.GET)
    public ModelAndView addResultView(){
        ModelAndView model = new ModelAndView("hiddentrouble_result_add");
        return model;
    }

    @RequestMapping(path = "/result/detail/{id}",method = RequestMethod.GET)
    public ModelAndView resultDetailView(@PathVariable Integer id){
        ModelAndView model = new ModelAndView("hiddentrouble_result_detail");
        HiddenTroubleResult hiddenTroubleResult = hiddenTroubleService.findResultById(id);
        model.addObject("hiddenTroubleResult",hiddenTroubleResult);
        return model;
    }

    @RequestMapping(path = "/result/delete/{id}",method = RequestMethod.GET)
    public AjaxResponse deleteResult(@PathVariable Integer id){
        AjaxResponse response = hiddenTroubleService.deleteResult(id);
        return response;
    }

    @RequestMapping(path = "/result/add",method = RequestMethod.POST)
    public AjaxResponse addResult(@RequestBody HiddenTroubleResult hiddenTroubleResult){
        AjaxResponse response = hiddenTroubleService.save(hiddenTroubleResult);
        return response;
    }
    @RequestMapping(path = "/export/{id}",method = RequestMethod.GET)
    public AjaxResponse exportGoods(@PathVariable Integer id,HttpServletRequest request, HttpServletResponse response) {
        response.reset(); //清除buffer缓存
        hiddenTroubleService.export(id,response);
        return AjaxResponse.success();
    }
}
