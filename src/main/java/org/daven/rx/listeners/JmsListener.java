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
import rx.Subscriber;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;

import javax.jms.JMSException;
import javax.jms.MessageListener;

import static rx.Observable.create;

@Configuration
@EnableJms
public class JmsListener implements JmsListenerConfigurer {

    private final static Logger LOG = LoggerFactory.getLogger(JmsListener.class);

    private final static SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();

    private final static ConnectableObservable<EventContainer> jmsStream =
            create((Subscriber<? super EventContainer> observer) -> {
                MessageListener listener = message -> {
                    LOG.debug("received:{}", message);
                    try {
                        observer.onNext(EventContainer.from(message));
                    } catch (JMSException e) {
                        LOG.error("Caught:{}", e);
                        observer.onError(e);
                    }
                };
                endpoint.setMessageListener(listener);
            }).
                    subscribeOn(Schedulers.io()).
                    publish();


    public ConnectableObservable<EventContainer> jmsStream() {
        return jmsStream;
    }

    @Override
    public void configureJmsListeners(JmsListenerEndpointRegistrar registar) {
        jmsStream().connect();
        endpoint.setId("myJmsEndPoint");
        endpoint.setDestination("mailbox-destination");
        endpoint.setConcurrency("3-5");
        registar.registerEndpoint(endpoint);
    }
}
