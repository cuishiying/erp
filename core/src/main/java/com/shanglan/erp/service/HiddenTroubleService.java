package com.shanglan.erp.service;

import com.shanglan.erp.base.AjaxResponse;
import com.shanglan.erp.entity.HiddenTroubleItem;
import com.shanglan.erp.repository.HiddenTroubleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HiddenTroubleService {
    @Autowired
    private HiddenTroubleRepository hiddenTroubleRepository;

    public AjaxResponse save(List<HiddenTroubleItem> list){
        try{
            hiddenTroubleRepository.save(list);
            return AjaxResponse.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResponse.fail();
        }
    }

    public Page<HiddenTroubleItem> findAll(Pageable page){
        Page<HiddenTroubleItem> list = hiddenTroubleRepository.findAll(page);
        return list;
    }

    public HiddenTroubleItem findById(Integer id){
        HiddenTroubleItem troubleItem = hiddenTroubleRepository.findOne(id);
        return troubleItem;
    }

}
