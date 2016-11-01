package com.digiup.sandbox.model;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Msg {

    @Autowired(required = false)
    private Logger logger;

    public void process(String message) {
        logger.info("processing msg: " + message);
    }

}
