package com.shanglan.erp.service;

import com.shanglan.erp.base.AjaxResponse;
import com.shanglan.erp.base.ExcelUtils;
import com.shanglan.erp.dto.HiddenTroubleDTO;
import com.shanglan.erp.dto.HiddenTroubleResultDTO;
import com.shanglan.erp.dto.RiskQueryDTO;
import com.shanglan.erp.entity.*;
import com.shanglan.erp.repository.HiddenTroubleItemRepository;
import com.shanglan.erp.repository.HiddenTroubleRepository;
import com.shanglan.erp.repository.HiddenTroubleResultRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@Transactional
public class HiddenTroubleService {
    @Autowired
    private HiddenTroubleItemRepository hiddenTroubleItemRepository;
    @Autowired
    private HiddenTroubleRepository hiddenTroubleRepository;
    @Autowired
    private HiddenTroubleResultRepository hiddenTroubleResultRepository;

    public AjaxResponse save(List<HiddenTroubleItem> list){
        try{
            hiddenTroubleItemRepository.save(list);
            return AjaxResponse.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResponse.fail();
        }
    }

    public AjaxResponse save(HiddenTrouble hiddenTrouble){
        try{
            String[] split = hiddenTrouble.getCreateMonth().split("-");
            hiddenTrouble.setCreateMonthStr(split[0]+"年"+split[1]+"月");
            hiddenTrouble.setCreateTime(LocalDateTime.now());
            hiddenTroubleRepository.save(hiddenTrouble);
            return AjaxResponse.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResponse.fail();
        }
    }

    public AjaxResponse save(HiddenTroubleResult hiddenTroubleResult){
        try{
            hiddenTroubleResult.setPublicTime(LocalDateTime.now());
            hiddenTroubleResultRepository.save(hiddenTroubleResult);
            return AjaxResponse.success("发布成功");
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResponse.fail("发布失败");
        }
    }

    public Page<HiddenTroubleResult> findAll(HiddenTroubleResultDTO hiddenTroubleResultDTO,Pageable page){
        Specification<HiddenTroubleResult> spec = getResultWhereClause(hiddenTroubleResultDTO);
        Page<HiddenTroubleResult> list = hiddenTroubleResultRepository.findAll(spec, page);
        return list;
    }

    public Page<HiddenTrouble> findAll(HiddenTroubleDTO HiddenTrouble,Pageable page){
        Specification<HiddenTrouble> spec = getWhereClause(HiddenTrouble);
        Page<HiddenTrouble> list = hiddenTroubleRepository.findAll(spec,page);
        return list;
    }

    public HiddenTrouble findById(Integer id){
        HiddenTrouble troubleItem = hiddenTroubleRepository.findOne(id);
        return troubleItem;
    }
    public HiddenTroubleResult findResultById(Integer id){
        HiddenTroubleResult one = hiddenTroubleResultRepository.findOne(id);
        return one;
    }
    public AjaxResponse delete(Integer id){
        try {
            hiddenTroubleRepository.delete(id);
        }catch (Exception e){
            e.printStackTrace();
            return  AjaxResponse.fail("删除失败");
        }
        return AjaxResponse.success("删除成功");
    }
    public AjaxResponse deleteResult(Integer id){
        try {
            hiddenTroubleResultRepository.delete(id);
        }catch (Exception e){
            e.printStackTrace();
            return  AjaxResponse.fail("删除失败");
        }
        return AjaxResponse.success("删除成功");
    }

    /**
     * 查询条件
     * @param queryVo
     * @return
     */
    private Specification<HiddenTrouble> getWhereClause(HiddenTroubleDTO queryVo) {
        return (root, query, cb) -> {
            List<Predicate> predicate = new ArrayList<>();

            //关键词
            if(queryVo!=null&& StringUtils.isNotBlank(queryVo.getKeyword())){
                predicate.add(cb.or(cb.like(root.<String>get("name"), "%" + queryVo.getKeyword().trim() + "%"),
                        cb.like(root.<String>get("entryPerson"), "%" + queryVo.getKeyword().trim() + "%")));

            }

            //检查时间
            if (queryVo!=null&&queryVo.getBeginDate() != null) {
//                LocalDateTime begin = LocalDateTime.of(queryVo.getBeginDate(), LocalTime.MIN);
                Predicate dateQuery = cb.greaterThanOrEqualTo(root.<LocalDate>get("createTime"), queryVo.getBeginDate());
                predicate.add(dateQuery);
            }
            if (queryVo!=null&&queryVo.getEndDate() != null) {
//                LocalDateTime end = LocalDateTime.of(queryVo.getEndDate(), LocalTime.MAX);
                Predicate dateQuery = cb.lessThanOrEqualTo(root.<LocalDate>get("createTime"), queryVo.getEndDate());
                predicate.add(dateQuery);
            }
//            Predicate deletedQuery = cb.equal(root.<Integer>get("deleted"),false);
//            predicate.add(deletedQuery);

            return query.where(predicate.toArray(new Predicate[predicate.size()])).getRestriction();
        };
    }

    private Specification<HiddenTroubleResult> getResultWhereClause(HiddenTroubleResultDTO  queryVo) {
        return (root, query, cb) -> {
            List<Predicate> predicate = new ArrayList<>();

            //关键词
            if(queryVo!=null&& StringUtils.isNotBlank(queryVo.getKeyword())){
                predicate.add(cb.or(cb.like(root.<String>get("name"), "%" + queryVo.getKeyword().trim() + "%"),
                        cb.like(root.<String>get("content"), "%" + queryVo.getKeyword().trim() + "%")));

            }

            //检查时间
            if (queryVo!=null&&queryVo.getBeginDate() != null) {
                LocalDateTime begin = LocalDateTime.of(queryVo.getBeginDate(), LocalTime.MIN);
                Predicate dateQuery = cb.greaterThanOrEqualTo(root.<LocalDateTime>get("publicTime"), begin);
                predicate.add(dateQuery);
            }
            if (queryVo!=null&&queryVo.getEndDate() != null) {
                LocalDateTime end = LocalDateTime.of(queryVo.getEndDate(), LocalTime.MAX);
                Predicate dateQuery = cb.lessThanOrEqualTo(root.<LocalDateTime>get("publicTime"), end);
                predicate.add(dateQuery);
            }

            return query.where(predicate.toArray(new Predicate[predicate.size()])).getRestriction();
        };
    }
    public void export(Integer id,HttpServletResponse response){
        String fileName = "事故隐患排查治理登记台账";
        String sheetName = "事故隐患排查治理登记台账";
        HiddenTrouble hiddenTrouble = findById(id);
        List<HiddenTroubleItem> list = hiddenTrouble.getHiddentroubles();
        if(list.size()==0){
            HiddenTroubleItem item = new HiddenTroubleItem();
            item.setOrderNumber(0);
            item.setCheckAddress("测试地点");
            item.setCheckTime(LocalDate.now());
            item.setCheckPerson("测试人员");
            item.setContent("测试内容");
            item.setType("测试类型");
            item.setLevel("A");
            item.setActions("测试整改措施");
            item.setResponsiblePerson("测试人");
            item.setControllingMoney("20");
            item.setTimeLimit("测试年限");
            item.setManagementPlan("测试预案");
            item.setHandlePerson("督办部门");
            item.setAcceptanceTime(LocalDate.now());
            item.setAcceptancePeople("测试验收人");
            item.setResult("测试结果");
            list.add(item);
        }
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("序号","orderNumber");
        map.put("排查地点","checkAddress");
        map.put("排查时间","checkTime");
        map.put("排查人员","checkPerson");
        map.put("存在的隐患及问题","content");
        map.put("类别","type");
        map.put("等级","level");
        map.put("整改措施","actions");
        map.put("责任单位/责任人","responsiblePerson");
        map.put("资金","controllingMoney");
        map.put("时限","timeLimit");
        map.put("预案","managementPlan");
        map.put("督办部门/人员","handlePerson");
        map.put("验收时间","acceptanceTime");
        map.put("验收部门/人员","acceptancePeople");
        map.put("整改结果","result");
        ExcelUtils.exportHiddenTrouble(fileName,sheetName,HiddenTroubleItem.class,list,map,response,hiddenTrouble.getCreateMonthStr());
    }

}
