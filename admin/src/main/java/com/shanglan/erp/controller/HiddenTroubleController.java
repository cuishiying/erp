package com.shanglan.erp.controller;

import com.shanglan.erp.base.AjaxResponse;
import com.shanglan.erp.dto.HiddenTroubleDTO;
import com.shanglan.erp.dto.HiddenTroubleResultDTO;
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
    public ModelAndView hiddenTroubleListView(String username, String truename, HiddenTroubleDTO hiddenTroubleDTO, @PageableDefault(value = 10,sort = "finishTime",direction = Sort.Direction.DESC) Pageable pageable, HttpServletRequest request){
        ModelAndView model = new ModelAndView("hiddentrouble_list");
        Page<HiddenTroubleItem> page = hiddenTroubleService.findAll(hiddenTroubleDTO,pageable);
        model.addObject("page",page);
        model.addObject("query",hiddenTroubleDTO);
        return model;
    }

    @RequestMapping(path = "/detail/{id}",method = RequestMethod.GET)
    public ModelAndView hiddenTroubleDetail(@PathVariable Integer id, String username, String truename, @PageableDefault(value = 10,sort = "finishTime",direction = Sort.Direction.DESC) Pageable pageable, HttpServletRequest request){
        ModelAndView model = new ModelAndView("hiddentrouble_detail");
        HiddenTroubleItem hiddenTrouble = hiddenTroubleService.findById(id);
        model.addObject("hiddenTrouble",hiddenTrouble);
        return model;
    }

    @RequestMapping(path = "/add",method = RequestMethod.GET)
    public ModelAndView addView(){
        ModelAndView model = new ModelAndView("hiddentrouble_add");
        model.addObject("grades", HiddenTroubleLevels.values());
        return model;
    }

    @RequestMapping(path = "/add",method = RequestMethod.POST)
    public AjaxResponse add(@RequestBody List<HiddenTroubleItem> list){
        AjaxResponse response = hiddenTroubleService.save(list);
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

    @RequestMapping(path = "/result/add",method = RequestMethod.POST)
    public AjaxResponse addResult(@RequestBody HiddenTroubleResult hiddenTroubleResult){
        AjaxResponse response = hiddenTroubleService.save(hiddenTroubleResult);
        return response;
    }
}
