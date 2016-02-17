/*
 * Copyright (c) 2015.
 * david.navarro.ext@proximus.com, me@davengeo.com
 */

package org.daven.rx.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class JmsObserver {

    private static final Logger LOG = LoggerFactory.getLogger(JmsObserver.class);

    @Autowired
    JmsListener listener;

    @PostConstruct
    private void init()  {
        listener.jmsStream().subscribe(
                eventContainer -> LOG.info("Observed:{}", eventContainer.getBody()),
                throwable -> LOG.error("Error:{}", throwable),
                () -> LOG.warn("JMS Stream completed"));
    }
}
