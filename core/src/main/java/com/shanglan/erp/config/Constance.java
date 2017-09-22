package com.shanglan.erp.config;

import com.shanglan.erp.entity.Storage;

import java.util.HashMap;
import java.util.List;

public class Constance {
    private static HashMap storageidConfig = new HashMap();
    private static HashMap storagenameConfig = new HashMap();

    public static String getStorageConfig(String id) {
        return (String) storageidConfig.get(id);
    }

    public static Integer getStoragenameConfig(String name) {
        return (Integer) storagenameConfig.get(name);
    }

    public static void setStorageConfig(List<Storage> list) {
        for(int i=0;i<list.size();i++){
            storagenameConfig.put(list.get(i).getStoragename(),list.get(i).getId());
            storageidConfig.put(list.get(i).getId(),list.get(i).getStoragename());
        }
    }
}
