package com.shanglan.erp.service;

import com.shanglan.erp.base.AjaxResponse;
import com.shanglan.erp.dto.AttForm;
import com.shanglan.erp.entity.*;
import com.shanglan.erp.enums.AttRecordStatus;
import com.shanglan.erp.repository.*;
import com.shanglan.erp.utils.JavaUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AttService {

    @Autowired
    private AttMgrJoinattRepository attMgrJoinattRepository;
    @Autowired
    private AttClassesRepository attClassesRepository;
    @Autowired
    private AttRecordRepository attRecordRepository;
    @Autowired
    private DeptService deptService;
    @Autowired
    private AttRepository attRepository;
    @Autowired
    private AttMachineRepository attMachineRepository;

    /**
     * 查找所有需要考勤的人员
     * @return
     */
    public List<AttMgrJoinatt> findAllJoinattUser(){
        List<AttMgrJoinatt> allJoinatt = attMgrJoinattRepository.findAllJoinatt();

        for (AttMgrJoinatt a:allJoinatt) {
            a.setDeptname(deptService.findById(a.getDeptId()).getName());
        }
        return allJoinatt;
    }

    /**
     * 新增排班
     */
    public AjaxResponse addClasses(AttClasses attClasses){
        attClassesRepository.save(attClasses);
        return AjaxResponse.success();
    }

    public AjaxResponse delClasses(Integer id){
        attClassesRepository.delete(id);
        return AjaxResponse.success();
    }

    /**
     * 获取排班列表
     */
    public List<AttClasses> findClasses(){
        List<AttClasses> all = attClassesRepository.findAll();
        return all;
    }

    /**
     * 给所有员工绑定排班
     * @param list
     * @return
     */
    public AjaxResponse updateJoinattUsers(List<AttMgrJoinatt> list){
        for (int i=0;i<list.size();i++){
            AttMgrJoinatt attMgrJoinatt = attMgrJoinattRepository.findByUid(list.get(i).getUid());
            attMgrJoinatt.setCid(list.get(i).getCid());
        }
        return AjaxResponse.success();
    }

    /**
     * 定时保存所有员工当天的考勤记录,2017-09-01
     */
    public void saveTodayAttendances(String date){
        List<AttMgrJoinatt> list = findAllJoinattUser();
        if(StringUtils.isEmpty(date)){
            date = LocalDate.now().toString();
        }
        for (AttMgrJoinatt a:list) {
            saveAttendance(a.getUid(),date);
        }
    }

    /**
     * 保存一天的记录
     */
    public void saveAttendance(Integer uid,String date){
        String week = JavaUtils.getDayOfWeekByDate(date);
        AttMgrJoinatt attMgrJoinatt = attMgrJoinattRepository.findByUid(uid);
        AttClasses attClasses = attClassesRepository.findOne(attMgrJoinatt.getCid());
        String machineRecord = findMachineRecord(attMgrJoinatt.getImportKey(), date);
        //需要打卡次数
        int attClassCount = attClasses.getAttClassTimes().size();
        //实际打卡次数
        int count = 0;
        List<Integer> list = new ArrayList<>();
        for(int i=0;i<attClassCount;i++){
            String s = attClasses.getAttClassTimes().get(i);
            if(StringUtils.equals(s.trim(),"-")){
                continue;
            }
            String[] split = s.split("-");

            String startTime = date+" "+split[0]+":00";
            String endTime = date+" "+split[1]+":00";

            Integer attendance = attMachineRepository.isAttendance(attMgrJoinatt.getImportKey(), JavaUtils.convert2Timelong(startTime), JavaUtils.convert2Timelong(endTime));
            list.add(attendance);
            if(attendance>0){
                count++;
            }
        }

        AttRecord attRecord = new AttRecord();
        attRecord.setUid(uid);
        attRecord.setTruename(attMgrJoinattRepository.findByUid(uid).getTruename());
        attRecord.setDate(date);
        attRecord.setDept(attMgrJoinatt.getDeptname());
        attRecord.setMachineData(machineRecord);
        attRecord.setWeekday(week);
        if(attClassCount==4){
            //每天需要4次考勤

            if(count==4){
                //考勤正常
                attRecord.setAttRecordStatus(AttRecordStatus.normal);
                attRecord.setAttendanceDay(Double.parseDouble("1"));
            }else if(count==3){
                if(list.get(0)==0||list.get(1)==0){
                    //上午缺勤
                    attRecord.setAttRecordStatus(AttRecordStatus.mAbsence);
                    attRecord.setAttendanceDay(0.5);
                }else{
                    //下午缺勤
                    attRecord.setAttRecordStatus(AttRecordStatus.aAbsence);
                    attRecord.setAttendanceDay(0.5);
                }

            }else if(count==2){
                if(list.get(0)==0&&list.get(1)==0){
                    //上午缺勤
                    attRecord.setAttRecordStatus(AttRecordStatus.mAbsence);
                    attRecord.setAttendanceDay(0.5);

                }else if(list.get(2)==0&&list.get(3)==0){
                    //下午缺勤
                    attRecord.setAttRecordStatus(AttRecordStatus.aAbsence);
                    attRecord.setAttendanceDay(0.5);

                }else{
                    //缺勤一天
                    attRecord.setAttRecordStatus(AttRecordStatus.absence);
                    attRecord.setAttendanceDay(Double.parseDouble("0"));
                }

            }else{
                //缺勤一天
                attRecord.setAttRecordStatus(AttRecordStatus.absence);
                attRecord.setAttendanceDay(Double.parseDouble("0"));
            }
        }else if(attClassCount==3){
            //每天需要3次考勤
            if(count==3){
                //考勤正常
                attRecord.setAttRecordStatus(AttRecordStatus.normal);
                attRecord.setAttendanceDay(Double.parseDouble("1"));
            }else{
                //缺勤一天
                attRecord.setAttRecordStatus(AttRecordStatus.absence);
                attRecord.setAttendanceDay(Double.parseDouble("0"));
            }

        }else if(attClassCount==2){
            //每天需要2次考勤
            if(count==2){
                //考勤正常
                attRecord.setAttRecordStatus(AttRecordStatus.normal);
                attRecord.setAttendanceDay(Double.parseDouble("1"));
            }else{
                //缺勤一天
                attRecord.setAttRecordStatus(AttRecordStatus.absence);
                attRecord.setAttendanceDay(Double.parseDouble("0"));
            }

        }else if(attClassCount==1){
            //每天需要1次考勤
            if(count==1){
                //考勤正常
                attRecord.setAttRecordStatus(AttRecordStatus.normal);
                attRecord.setAttendanceDay(Double.parseDouble("1"));
            }else{
                //缺勤一天
                attRecord.setAttRecordStatus(AttRecordStatus.absence);
                attRecord.setAttendanceDay(Double.parseDouble("0"));
            }
        }
        attRecordRepository.save(attRecord);
    }

    /**
     * 考勤登记
     * @param attRecord
     * @return
     */
    public AjaxResponse updateAttendance(AttRecord attRecord){
        AttRecord newAttRecord = attRecordRepository.findOne(attRecord.getId());
        if(null!=newAttRecord){
            newAttRecord.setAttRecordStatus(attRecord.getAttRecordStatus());
            newAttRecord.setAttendanceDay(attRecord.getAttendanceDay());
            newAttRecord.setUpdateTime(LocalDateTime.now());
        }
        return AjaxResponse.success();
    }

    /**
     * 获取员工月度考勤
     */
    public List<AttRecord> findMonthAttendance(Integer uid,Integer year,Integer month){
        String firstDayOfMonth = JavaUtils.getFirstDayOfMonth(year, month);
        String lastDayOfMonth = JavaUtils.getLastDayOfMonth(year, month);
        List<AttRecord> monthAttendance = attRecordRepository.findMonthAttendance(uid, firstDayOfMonth, lastDayOfMonth);
        return monthAttendance;
    }

    /**
     * 月度报表,统计某员工月度考勤，出勤天数、旷工数等
     * @param year
     * @param month
     * @return
     */
    public List<AttForm> monthForm(Integer year,Integer month){
        List<AttForm> attForms = attRepository.statisticalForms(year, month);
        return attForms;
    }





    /**
     * 查询某人某天的打卡记录
     * @param date
     * @return
     */
    public List<AttMachine> findAtt(Integer enrollNum, String date){

        String startTime = date+" 00:00:00";
        String endTime = date+" 23:59:59";

        List<AttMachine> allJoinatt = attMachineRepository.findAllMachineRecord(enrollNum, JavaUtils.convert2Timelong(startTime),JavaUtils.convert2Timelong(endTime));
        return allJoinatt;
    }

    /**
     * 考勤原始数据
     * @param enrollNum
     * @param date
     * @return
     */
    public String findMachineRecord(Integer enrollNum, String date){
        List<String> newList = new ArrayList<>();
        String startTime = date+" 00:00:00";
        String endTime = date+" 23:59:59";
        List<Long> list = attMachineRepository.findMachineRecord(enrollNum, JavaUtils.convert2Timelong(startTime),JavaUtils.convert2Timelong(endTime));
        for (Long s:list) {
            newList.add(JavaUtils.convertLong2Time(s+""));
        }
        String join = StringUtils.join(newList, "-");
        return join;
    }

    /**
     * 查询某人某月打卡记录
     * @param uid
     * @param year
     * @param month
     */
    public void checkMonthAtt(Integer uid,Integer year,Integer month){
        Integer importKey = attMgrJoinattRepository.findByUid(uid).getImportKey();
        List<String> currentDayListOfMonth = JavaUtils.getDayListOfMonth(year,month);
        for(int i=0;i<currentDayListOfMonth.size();i++){
            findAtt(importKey,currentDayListOfMonth.get(i));
        }
    }


}
