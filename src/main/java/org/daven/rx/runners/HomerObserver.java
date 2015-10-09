/*
 * Copyright (c) 2015.
 * david.navarro.ext@proximus.com, me@davengeo.com
 */

package org.daven.rx.runners;

import org.daven.rx.listeners.JmsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class HomerObserver {

    private static final Logger LOG = LoggerFactory.getLogger(HomerObserver.class);

    @Autowired
    JmsListener listener;

    @PostConstruct
    private void init()  {
        listener.jmsStream().subscribe(eventContainer -> {
            LOG.info("GOTCHA:{}", eventContainer);
        });
    }

}
