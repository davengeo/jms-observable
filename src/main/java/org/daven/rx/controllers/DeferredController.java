package org.daven.rx.controllers;

import org.daven.rx.domain.EventContainer;
import org.daven.rx.listeners.JmsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import rx.Observable;

@RestController
public class DeferredController {

    private final static Logger LOG = LoggerFactory.getLogger(DeferredController.class);

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    JmsListener listener;

    @RequestMapping("/send-receive")
    public DeferredResult<HttpEntity<String>> sendAndreceive() {

        DeferredResult<HttpEntity<String>> deferred = new DeferredResult<>(90000);
        final Observable<EventContainer> filtered = listener.jmsStream().
                take(1);
        filtered.subscribe(
                eventContainer -> deferred.setResult(new HttpEntity<>("SENT " + eventContainer.getBody())),
                throwable -> deferred.setErrorResult(new HttpEntity<>("ERROR")),
                () -> LOG.info("completed"));
        jmsTemplate.send("mailbox-destination",
                session -> session.createTextMessage("jajaja!"));
        return deferred;
    }
}
