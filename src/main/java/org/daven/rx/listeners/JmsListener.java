/*
 * Copyright (c) 2015.
 * david.navarro.ext@proximus.com, me@davengeo.com
 */

package org.daven.rx.listeners;

import org.daven.rx.domain.EventContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import static rx.Observable.create;

@Configuration
@EnableJms
public class JmsListener implements JmsListenerConfigurer {

    private final static Logger LOG = LoggerFactory.getLogger(JmsListener.class);
    private final SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    JmsListenerContainerFactory jmsListenerContainerFactory;

    public Observable<EventContainer> jmsStream() {
        return create((Subscriber<? super EventContainer> observer) -> {
            MessageListener listener = message -> {
                LOG.info("received:{}", message);
                try {
                    EventContainer event = new EventContainer().
                            setMessageId(message.getJMSMessageID()).
                            setBody(((TextMessage) message).getText());
                    observer.onNext(event);
                } catch (JMSException e) {
                    observer.onError(e);
                }
            };
            endpoint.setMessageListener(listener);
        }).observeOn(Schedulers.io());
    }

    @Override
    public void configureJmsListeners(JmsListenerEndpointRegistrar registar) {
        jmsStream().publish();
        endpoint.setId("myJmsEndPoint");
        endpoint.setDestination("mailbox-destination");
        endpoint.setConcurrency("3-5");
        registar.registerEndpoint(endpoint);
    }
}
