/*
 * Copyright (c) 2015.
 * david.navarro.ext@proximus.com, me@davengeo.com
 */

package org.daven.rx.runners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Component
public class SendMessageRunner implements CommandLineRunner{

    private static final Logger LOG = LoggerFactory.getLogger(SendMessageRunner.class);

    @Autowired
    JmsTemplate jmsTemplate;

    @Override
    public void run(String... strings) throws Exception {
        IntStream.range(0, 10).forEach(i -> {
            jmsTemplate.send("mailbox-destination",
                    session -> session.createTextMessage("ping!"));
            LOG.info("Sending a new message.");
        });

        jmsTemplate.send("mailbox-destination",
                session -> session.createTextMessage("last ping"));
        LOG.info("The end");
    }
}
