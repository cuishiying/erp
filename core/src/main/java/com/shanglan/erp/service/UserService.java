package com.shanglan.erp.service;

import com.shanglan.erp.entity.User;
import com.shanglan.erp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by cuishiying on 2017/6/21.
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findUserByUsernameAndtruename(String username, String truename){
        User user = userRepository.findUserByUsernameAndtruename(username, truename);
        return user;
    }

    public User findByUid(Integer uid){
        User user = userRepository.findByUid(uid);
        return user;
    }

}
