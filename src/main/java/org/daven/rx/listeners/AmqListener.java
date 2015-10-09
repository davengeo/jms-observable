/*
 * Copyright (c) 2015.
 * david.navarro.ext@proximus.com, me@davengeo.com
 */

package org.daven.rx.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import rx.Subscriber;

import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Configuration
@EnableJms
public class AmqListener implements JmsListenerConfigurer {

    private final static Logger LOG = LoggerFactory.getLogger(AmqListener.class);

    private MessageAdapter messageAdapter = new MessageAdapter();

    @Override
    public void configureJmsListeners(JmsListenerEndpointRegistrar registar) {
        SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
        endpoint.setId("myJmsEndPoint");
        endpoint.setDestination("mailbox-destination");
        endpoint.setMessageListener(messageAdapter);
        endpoint.setConcurrency("3-5");
        registar.registerEndpoint(endpoint);
    }

    private class MessageAdapter implements MessageListener {

        private List<Subscriber<? super Object>> subscribers = newArrayList();

        public void addSubscriber(Subscriber<? super Object> subscriber) {
            this.subscribers.add(subscriber);
        }

        @Override
        public void onMessage(Message message) {
            LOG.info("Received:{}", message);
            //noinspection CodeBlock2Expr
            subscribers.forEach(subscriber -> {
//                subscriber.onNext(new EventContainer().setMessage(message));
            });
        }

        public void checkSubscribers() {
            subscribers.removeIf(Subscriber::isUnsubscribed);
            LOG.info("subscribers size:{}", subscribers.size());
        }

        public void purgeSubscribers() {
            subscribers.removeAll(subscribers);
        }
    }
}
