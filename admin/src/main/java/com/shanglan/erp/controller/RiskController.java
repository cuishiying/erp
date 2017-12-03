package com.shanglan.erp.controller;

import com.shanglan.erp.base.AjaxResponse;
import com.shanglan.erp.dto.RiskQueryDTO;
import com.shanglan.erp.entity.*;
import com.shanglan.erp.service.DeptService;
import com.shanglan.erp.service.RiskService;
import com.shanglan.erp.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * 风险管控
 */
@RestController
@RequestMapping(path = "/risk")
public class RiskController {

    @Autowired
    private RiskService riskService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private UserService userService;

    //******************************配置项****************************
    @RequestMapping(path = "/item/addview",method = RequestMethod.GET)
    public ModelAndView addRiskItemView(){
        ModelAndView model = new ModelAndView("risk_additem");
        List<RiskCondition> conditionList = riskService.findRiskConditions();
        List<RiskItem> itemList = riskService.findRiskItems();
        model.addObject("conditionList",conditionList);
        model.addObject("itemList",itemList);
        return model;
    }

    @RequestMapping(path = "/item/save",method = RequestMethod.POST)
    public AjaxResponse saveRiskItem(@RequestParam Integer conditionId,@RequestParam String conditionValue){
        AjaxResponse response = riskService.saveRiskItem(conditionId, conditionValue);
        return response;
    }
    @RequestMapping(path = "/item/delete/{id}",method = RequestMethod.GET)
    public AjaxResponse deleteRiskItem(@PathVariable Integer id){
        AjaxResponse response = riskService.deleteRiskItem(id);
        return response;
    }

    //******************************风险管控****************************

    @RequestMapping(path = "/value/list", method = RequestMethod.GET)
    public ModelAndView riskValueList(String username, String truename, RiskQueryDTO queryDTO, @PageableDefault(value = 10, sort = "updateTime", direction = Sort.Direction.DESC) Pageable pageable, HttpServletRequest request) {
        if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(truename)) {
            User user = userService.findUserByUsernameAndtruename(username, truename);
            request.getSession().invalidate();
            request.getSession().setAttribute("uid", user.getUid());
        }
        ModelAndView model = new ModelAndView("risk_valuelist");
        Page<RiskValue> page = riskService.findRiskValues(queryDTO, pageable);
        List<RiskItem> riskLevels = riskService.findRiskItems("风险分级");
        model.addObject("riskLevels", riskLevels);
        model.addObject("page", page);
        model.addObject("queryDTO", queryDTO);
        return model;
    }

    @RequestMapping(path = "/value/add",method = RequestMethod.GET)
    public ModelAndView addRiskValueView(){
        ModelAndView model = new ModelAndView("risk_addvalue");
        List<RiskItem> riskLevels = riskService.findRiskItems("风险分级");
        model.addObject("riskLevels",riskLevels);
        return model;
    }
    @RequestMapping(path = "/value/add",method = RequestMethod.POST)
    public AjaxResponse addRiskValue(@RequestBody RiskValue riskValue, HttpServletRequest request){
        Integer uid = (Integer) request.getSession().getAttribute("uid");
        AjaxResponse ajaxResponse = riskService.addRiskValue(uid,riskValue);
        return ajaxResponse;
    }

    @RequestMapping(path = "/value/detail/{id}",method = RequestMethod.GET)
    public ModelAndView detailRiskValueView(@PathVariable Integer id){
        ModelAndView model = new ModelAndView("risk_valuedetail");
        RiskValue riskValue = riskService.findRiskValueById(id);
        model.addObject("riskValue",riskValue);
        model.addObject("readonly",true);
        return model;
    }
    @RequestMapping(path = "/value/edit/{id}",method = RequestMethod.GET)
    public ModelAndView editRiskValueView(@PathVariable Integer id){
        ModelAndView model = new ModelAndView("risk_editvalue");
        RiskValue riskValue = riskService.findRiskValueById(id);
        model.addObject("riskValue",riskValue);
        model.addObject("readonly",false);
        return model;
    }
    @RequestMapping(path = "/value/edit/{id}",method = RequestMethod.POST)
    public AjaxResponse editRiskValue(@PathVariable Integer id,@RequestBody RiskValue riskValue, HttpServletRequest request){
        Integer uid = (Integer) request.getSession().getAttribute("uid");
        AjaxResponse response = riskService.updateRiskValue(uid, riskValue);
        return response;
    }
    @RequestMapping(path = "/value/delete/{id}",method = RequestMethod.GET)
    public AjaxResponse deleteRiskValue(@PathVariable Integer id,HttpServletRequest request){
        Integer uid = (Integer) request.getSession().getAttribute("uid");
        AjaxResponse response = riskService.physicsDeleteRiskValue(uid,id);
        return response;
    }
}
