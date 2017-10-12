package com.shanglan.erp.controller;

import com.shanglan.erp.base.AjaxResponse;
import com.shanglan.erp.dto.RiskQueryDTO;
import com.shanglan.erp.entity.Dept;
import com.shanglan.erp.entity.RiskCondition;
import com.shanglan.erp.entity.RiskItem;
import com.shanglan.erp.entity.RiskValue;
import com.shanglan.erp.service.DeptService;
import com.shanglan.erp.service.RiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping(path = "/risk")
public class RiskController {

    @Autowired
    private RiskService riskService;

    @Autowired
    private DeptService deptService;


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

    @RequestMapping(path = "/value/listview",method = RequestMethod.GET)
    public ModelAndView riskValueListView(RiskQueryDTO queryDTO, @PageableDefault(value = 10,sort = "id",direction = Sort.Direction.DESC) Pageable pageable){
        ModelAndView model = new ModelAndView("risk_valuelist");
        Page<RiskValue> page = riskService.findRiskValues(queryDTO,pageable);
        List<RiskItem> riskAddrs = riskService.findRiskItems("风险地点");
        List<RiskItem> riskLevels = riskService.findRiskItems("风险等级");
        List<RiskItem> riskValues = riskService.findRiskItems("风险分析");
        List<Dept> deptList = deptService.findAll();
        model.addObject("riskAddrs",riskAddrs);
        model.addObject("riskLevels",riskLevels);
        model.addObject("riskValues",riskValues);
        model.addObject("deptList",deptList);
        model.addObject("page",page);
        model.addObject("queryDTO",queryDTO);
        return model;
    }

    @RequestMapping(path = "/value/add",method = RequestMethod.GET)
    public ModelAndView addRiskValueView(){
        ModelAndView model = new ModelAndView("risk_addvalue");
        List<RiskItem> riskAddrs = riskService.findRiskItems("风险地点");
        List<RiskItem> riskLevels = riskService.findRiskItems("风险等级");
        List<RiskItem> riskValues = riskService.findRiskItems("风险分析");
        List<Dept> deptList = deptService.findAll();
        model.addObject("riskAddrs",riskAddrs);
        model.addObject("riskLevels",riskLevels);
        model.addObject("riskValues",riskValues);
        model.addObject("deptList",deptList);
        return model;
    }

    @RequestMapping(path = "/value/add",method = RequestMethod.POST)
    public AjaxResponse addRiskValue(@RequestParam Integer riskAddrId,@RequestParam String riskDesc,@RequestParam Integer riskLevelId,@RequestParam String precaution,@RequestParam Integer riskMkDeptId,@RequestParam Integer riskvalueId){
        AjaxResponse response = riskService.saveRiskValue(riskAddrId, riskDesc, riskLevelId, precaution, riskMkDeptId, riskvalueId);
        return response;
    }
}
