/*
 * Copyright (c) 2015.
 * david.navarro.ext@proximus.com, me@davengeo.com
 */

package org.daven.rx.listeners;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Order(2)
@Component
public class AnotherObserver implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(AnotherObserver.class);

    @Autowired
    JmsListener listener;

    @Autowired
    ApplicationContext context;

    @PostConstruct
    private void init()  {
        LOG.info("second observer");
        listener.jmsStream().subscribe(eventContainer -> {
            LOG.info("GOTCHA:{}", eventContainer.getBody());
        }, throwable -> {
            LOG.error("Error:{}");
        }, () -> {
            LOG.warn("JMS Stream completed");
        });
    }


    @Override
    public void run(String... strings) throws Exception {
        for (String name : context.getBeanDefinitionNames()) {
//            LOG.info(name);
        }
    }
}
