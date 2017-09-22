package com.shanglan.erp.controller;

import com.shanglan.erp.base.AjaxResponse;
import com.shanglan.erp.dto.AttForm;
import com.shanglan.erp.dto.QueryDTO;
import com.shanglan.erp.entity.AttClasses;
import com.shanglan.erp.entity.AttMgrJoinatt;
import com.shanglan.erp.entity.AttRecord;
import com.shanglan.erp.enums.AttRecordStatus;
import com.shanglan.erp.service.AttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 考勤相关
 */
@RestController
@RequestMapping("/att")
public class AttController {

    @Autowired
    private AttService attService;



    @RequestMapping(path = "/note",method = RequestMethod.GET)
    public ModelAndView note(){
        ModelAndView model = new ModelAndView("att_note");
        return model;
    }
    /**
     * 获取参加考勤的人会
     * @return
     */
    @RequestMapping(path = "/users",method = RequestMethod.GET)
    public ModelAndView findJoinAttUser(){
        ModelAndView model = new ModelAndView("att_users");
        List<AttMgrJoinatt> allJoinattUser = attService.findAllJoinattUser();
        List<AttClasses> classes = attService.findClasses();
        model.addObject("users",allJoinattUser);
        model.addObject("classes",classes);
        return model;
    }

    /**
     * 排班绑定
     * @param list
     * @return
     */
    @RequestMapping(path = "/saveJoinAttUsers",method = RequestMethod.POST)
    public AjaxResponse saveJoinAttUsers(@RequestBody List<AttMgrJoinatt> list){
        attService.updateJoinattUsers(list);
        return AjaxResponse.success();
    }

    /**
     * 获取月度考勤
     * @param uid
     * @param queryDTO
     * @return
     */
    @RequestMapping(path = "/users/{uid}",method = RequestMethod.GET)
    public ModelAndView getJoinAttUser(@PathVariable Integer uid,QueryDTO queryDTO){
        ModelAndView model = new ModelAndView("att_user_detail");

        Date queryDate = queryDTO.getQueryDate();
        Calendar c = Calendar.getInstance();
        c.setTime(queryDate);

        List<AttRecord> monthAttendance = attService.findMonthAttendance(uid, c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1);
        model.addObject("list",monthAttendance);
        model.addObject("uid",uid);
        model.addObject("queryDTO",queryDTO);
        model.addObject("attStatus", AttRecordStatus.values());
        return model;
    }

    /**
     * 考勤登记
     * @param attRecord
     * @return
     */
    @RequestMapping(path = "/updateAttendance",method = RequestMethod.POST)
    public AjaxResponse updateAttendance(@RequestBody AttRecord attRecord){
        AjaxResponse ajaxResponse = attService.updateAttendance(attRecord);
        return ajaxResponse;
    }

    /**
     * 获取所有人考勤报表
     * @return
     */
    @RequestMapping(path = "/monthform",method = RequestMethod.GET)
    public ModelAndView monthForm(QueryDTO queryDTO){
        ModelAndView model = new ModelAndView("att_record");

        Date queryDate = queryDTO.getQueryDate();
        Calendar c = Calendar.getInstance();
        c.setTime(queryDate);

        List<AttForm> attForms = attService.monthForm(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1);
        model.addObject("list",attForms);
        return model;
    }

    /**
     * 设置排班
     * @return
     */
    @RequestMapping(path = "/classes",method = RequestMethod.GET)
    public ModelAndView setClasses(){
        ModelAndView model = new ModelAndView("att_classes");
        List<AttClasses> classes = attService.findClasses();
        model.addObject("classes",classes);
        return model;
    }

    /**
     * 添加排班
     * @param attClasses
     */
    @RequestMapping(path = "/addClasses",method = RequestMethod.POST)
    public AjaxResponse addClasses(@RequestBody AttClasses attClasses){
        AjaxResponse ajaxResponse = attService.addClasses(attClasses);
        return ajaxResponse;
    }
    /**
     * 删除排班
     */
    @RequestMapping(path = "/deleteClass/{id}",method = RequestMethod.POST)
    public AjaxResponse delClasses(@PathVariable Integer id){
        AjaxResponse ajaxResponse = attService.delClasses(id);
        return ajaxResponse;
    }


}
