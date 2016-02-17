package org.daven.rx.controllers;

import org.daven.rx.domain.EventContainer;
import org.daven.rx.listeners.JmsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping("/send-receive/{message}")
    public DeferredResult<HttpEntity<String>> sendAndReceive(@PathVariable String message) {

        DeferredResult<HttpEntity<String>> deferred = new DeferredResult<>(90000);
        final Observable<EventContainer> filtered = listener.jmsStream().
                takeFirst(eventContainer1 -> eventContainer1.getBody().contains(message));

        filtered.subscribe(
                eventContainer -> deferred.setResult(new HttpEntity<>("RECEIVED " + eventContainer.getBody())),
                throwable -> deferred.setErrorResult(new HttpEntity<>("ERROR")),
                () -> LOG.debug("completed"));
        jmsTemplate.send("mailbox-destination",
                session -> session.createTextMessage(message));
        return deferred;
    }
}
