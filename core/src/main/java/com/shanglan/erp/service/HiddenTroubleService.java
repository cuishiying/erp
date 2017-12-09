package com.shanglan.erp.service;

import com.shanglan.erp.base.AjaxResponse;
import com.shanglan.erp.base.ExcelUtils;
import com.shanglan.erp.config.Constance;
import com.shanglan.erp.dto.HiddenTroubleDTO;
import com.shanglan.erp.dto.HiddenTroubleItemDTO;
import com.shanglan.erp.dto.HiddenTroubleResultDTO;
import com.shanglan.erp.dto.RiskQueryDTO;
import com.shanglan.erp.entity.*;
import com.shanglan.erp.repository.HiddenTroubleConfigRepository;
import com.shanglan.erp.repository.HiddenTroubleItemRepository;
import com.shanglan.erp.repository.HiddenTroubleRepository;
import com.shanglan.erp.repository.HiddenTroubleResultRepository;
import com.shanglan.erp.utils.JavaUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    private UserService userService;
    @Autowired
    private HiddenTroubleConfigRepository hiddenTroubleConfigRepository;


    public AjaxResponse saveConfig(String name,String classifyName){
        HiddenTroubleConfig config = new HiddenTroubleConfig();
        config.setName(name);
        config.setClassifyName(classifyName);
        hiddenTroubleConfigRepository.save(config);
        return AjaxResponse.success();
    }

    public List<HiddenTroubleConfig> findAllConfigs(){
        List<HiddenTroubleConfig> list = hiddenTroubleConfigRepository.findAll();
        return list;
    }

    public AjaxResponse deleteConfig(Integer id){
        hiddenTroubleConfigRepository.delete(id);
        return AjaxResponse.success();
    }

    public AjaxResponse save(List<HiddenTroubleItem> list){
        try{
            hiddenTroubleItemRepository.save(list);
            return AjaxResponse.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResponse.fail();
        }
    }

    public AjaxResponse save(Integer uid,HiddenTrouble hiddenTrouble){
        try{
            if(null!=uid){
                User user = userService.findByUid(uid);
                hiddenTrouble.setEntryPerson(user.getUsername());
            }else {
                hiddenTrouble.setEntryPerson(null);
            }
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

    public AjaxResponse updateHiddenTroubleItem(Integer id,HiddenTroubleItem hiddenTroubleItem){
        HiddenTroubleItem troubleItem = hiddenTroubleItemRepository.findOne(id);
        troubleItem.setOrderNumber(hiddenTroubleItem.getOrderNumber());
        troubleItem.setCheckAddress(hiddenTroubleItem.getCheckAddress());
        troubleItem.setCheckTime(hiddenTroubleItem.getCheckTime());
        troubleItem.setCheckPerson(hiddenTroubleItem.getCheckPerson());
        troubleItem.setContent(hiddenTroubleItem.getContent());
        troubleItem.setType(hiddenTroubleItem.getType());
        troubleItem.setLevel(hiddenTroubleItem.getLevel());
        troubleItem.setActions(hiddenTroubleItem.getActions());
        troubleItem.setResponsiblePerson(hiddenTroubleItem.getResponsiblePerson());
        troubleItem.setControllingMoney(hiddenTroubleItem.getControllingMoney());
        troubleItem.setTimeLimit(hiddenTroubleItem.getTimeLimit());
        troubleItem.setManagementPlan(hiddenTroubleItem.getManagementPlan());
        troubleItem.setHandlePerson(hiddenTroubleItem.getHandlePerson());
        troubleItem.setAcceptanceTime(hiddenTroubleItem.getAcceptanceTime());
        troubleItem.setAcceptancePeople(hiddenTroubleItem.getAcceptancePeople());
        troubleItem.setResult(hiddenTroubleItem.getResult());
        return AjaxResponse.success();
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
    public Page<HiddenTroubleItem> findAll(HiddenTroubleItemDTO hiddenTroubleItemDTO, Pageable page){
        Specification<HiddenTroubleItem> spec = getItemWhereClause(hiddenTroubleItemDTO);
        Page<HiddenTroubleItem> list = hiddenTroubleItemRepository.findAll(spec,page);
        return list;
    }

    public List<HiddenTroubleItem> findAll(HiddenTroubleItemDTO hiddenTroubleItemDTO){
        Specification<HiddenTroubleItem> spec = getItemWhereClause(hiddenTroubleItemDTO);
        List<HiddenTroubleItem> list = hiddenTroubleItemRepository.findAll(spec);
        return list;
    }

    public HiddenTrouble findById(Integer id){
        HiddenTrouble troubleItem = hiddenTroubleRepository.findOne(id);
        return troubleItem;
    }
    public HiddenTroubleItem findItemById(Integer id){
        HiddenTroubleItem troubleItem = hiddenTroubleItemRepository.findOne(id);
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
    public AjaxResponse deleteItem(Integer id){
        try {
            hiddenTroubleItemRepository.delete(id);
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
                Predicate dateQuery = cb.greaterThanOrEqualTo(root.<LocalDate>get("createTime"), queryVo.getBeginDate());
                predicate.add(dateQuery);
            }
            if (queryVo!=null&&queryVo.getEndDate() != null) {
                Predicate dateQuery = cb.lessThanOrEqualTo(root.<LocalDate>get("createTime"), queryVo.getEndDate());
                predicate.add(dateQuery);
            }
//            Predicate deletedQuery = cb.equal(root.<Integer>get("deleted"),false);
//            predicate.add(deletedQuery);

            return query.where(predicate.toArray(new Predicate[predicate.size()])).getRestriction();
        };
    }

    private Specification<HiddenTroubleItem> getItemWhereClause(HiddenTroubleItemDTO queryVo) {
        return (Root<HiddenTroubleItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicate = new ArrayList<>();

            //关键词
            if(queryVo!=null&& StringUtils.isNotBlank(queryVo.getKeyword())){
                predicate.add(cb.or(cb.like(root.<String>get("name"), "%" + queryVo.getKeyword().trim() + "%"),
                        cb.like(root.<String>get("entryPerson"), "%" + queryVo.getKeyword().trim() + "%")));

            }

            if (queryVo!=null&&queryVo.getQueryDate() != null) {
                LocalDate firstDayOfMonth = JavaUtils.getFirstDayOfMonth(queryVo.getQueryDate());
                Predicate startQuery = cb.greaterThanOrEqualTo(root.<LocalDate>get("checkTime"), firstDayOfMonth);
                predicate.add(startQuery);
                LocalDate lastDayOfMonth = JavaUtils.getLastDayOfMonth(queryVo.getQueryDate());
                Predicate lastQuery = cb.lessThanOrEqualTo(root.<LocalDate>get("checkTime"), lastDayOfMonth);
                predicate.add(lastQuery);
            }

            if (queryVo!=null&&StringUtils.isNotBlank(queryVo.getQueryType())) {
                predicate.add(cb.like(root.<String>get("type"), "%" + queryVo.getQueryType().trim() + "%"));
            }

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
            item.setResult("未整改");
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

    public void export(HiddenTroubleItemDTO hiddenTroubleItemDTO,HttpServletResponse response){
        String fileName = "事故隐患排查治理登记台账";
        String sheetName = "事故隐患排查治理登记台账";
        List<HiddenTroubleItem> list = findAll(hiddenTroubleItemDTO);

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
            item.setResult("未整改");
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
        ExcelUtils.exportHiddenTrouble(fileName,sheetName,HiddenTroubleItem.class,list,map,response,JavaUtils.getMonth(hiddenTroubleItemDTO.getQueryDate()));
    }

    public AjaxResponse importData(InputStream in, MultipartFile file) throws Exception {

        String nowLong = JavaUtils.getNowLong();

        List<List<Object>> listob = ExcelUtils.getBankListByExcel(in,file.getOriginalFilename());
        List<HiddenTroubleItem> list=new ArrayList<>();

        for (int i = 0; i < listob.size(); i++) {

            if(i==0){
                //时间
            }else if(i==1||i==2){
                //表头
            }else{
                //正文
                List<Object> lo = listob.get(i);
                HiddenTroubleItem item = new HiddenTroubleItem();
                item.setOrderNumber(Integer.parseInt(String.valueOf(lo.get(0))));
                item.setCheckAddress(String.valueOf(lo.get(1)));
                item.setCheckTime(JavaUtils.convert2LocalDate(String.valueOf(lo.get(2))));
                item.setCheckPerson(String.valueOf(lo.get(3)));
                item.setContent(String.valueOf(lo.get(4)));
                item.setType(String.valueOf(lo.get(5)));
                item.setLevel(String.valueOf(lo.get(6)));
                item.setActions(String.valueOf(lo.get(7)));
                item.setResponsiblePerson(String.valueOf(lo.get(8)));
                item.setControllingMoney(String.valueOf(lo.get(9)));
                item.setTimeLimit(String.valueOf(lo.get(10)));
                item.setManagementPlan(String.valueOf(lo.get(11)));
                item.setHandlePerson(String.valueOf(lo.get(12)));
                item.setAcceptanceTime(JavaUtils.convert2LocalDate(String.valueOf(lo.get(13))));
                item.setAcceptancePeople(String.valueOf(lo.get(14)));
                item.setResult(String.valueOf(lo.get(15)));
                list.add(item);
            }
        }
        try {
            hiddenTroubleItemRepository.save(list);
            return AjaxResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResponse.fail();
        }

    }

}
