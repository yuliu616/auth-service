package com.yu.controller;

import com.yu.exception.RecordInsertionFailException;
import com.yu.exception.UnhandledException;
import com.yu.model.IntegerId;
import com.yu.model.config.IdGenerationStrategy;
import com.yu.model.auth.User;
import com.yu.modelMapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class IdGenerationController {

    @Autowired
    protected UserMapper userMapper;

    @Value("${auth-service.options.model.user.id-generation-strategy}")
    protected IdGenerationStrategy userIdGenerationStrategy;

    private static final Logger logger = LoggerFactory.getLogger(IdGenerationController.class);

    @Transactional
    public User fillWithGeneratedId(User model){
        if (userIdGenerationStrategy == IdGenerationStrategy.ID_TABLE) {
            return this.fillWithGeneratedIdWithIdTable(model);
        } else {
            throw new UnhandledException("unknown IdGenerationStrategy: "+this.userIdGenerationStrategy);
        }
    }

    private User fillWithGeneratedIdWithIdTable(User model){
        IntegerId integerId = new IntegerId();
        long affected = userMapper.generateUserId(integerId);
        if (affected <= 0) {
            throw new RecordInsertionFailException();
        }
        String idInString = String.valueOf(integerId.getId());
        model.setId(idInString);
        return model;
    }

}
