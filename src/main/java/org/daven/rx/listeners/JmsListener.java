/*
 * Copyright (c) 2015.
 * david.navarro.ext@proximus.com, me@davengeo.com
 */

package org.daven.rx.listeners;

import org.daven.rx.domain.EventContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import rx.Observable;

import javax.jms.MessageListener;

@Configuration
@EnableJms
public class JmsListener implements JmsListenerConfigurer {

    private final static Logger LOG = LoggerFactory.getLogger(JmsListener.class);
    private final SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();

    public Observable<EventContainer> jmsStream() {
        return Observable.create(observer -> {
            MessageListener listener = message -> {
                LOG.info("received:{}", message);
                observer.onNext(new EventContainer().setMessage(message));
            };
            endpoint.setMessageListener(listener);
        });
    }



    @Override
    public void configureJmsListeners(JmsListenerEndpointRegistrar registar) {
        //need subscribe to active the messageListener
        //he is so lazy :)
        //jmsStream().subscribe(); in some part of the code

        endpoint.setId("myJmsEndPoint");
        endpoint.setDestination("mailbox-destination");
        endpoint.setConcurrency("3-5");
        registar.registerEndpoint(endpoint);
    }
}
