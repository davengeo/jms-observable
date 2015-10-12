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
import rx.schedulers.Schedulers;

@RestController
public class DeferredController {

    private final static Logger LOG = LoggerFactory.getLogger(DeferredController.class);

    @RequestMapping("/get-async")
    public DeferredResult<String> getAsync() {
        Observable<String> o = getAMessageObs();
        DeferredResult<String> deferred = new DeferredResult<>(90000);
        o.subscribe(deferred::setResult, deferred::setErrorResult);
        return deferred;
    }

    public Observable<String> getAMessageObs() {
        return Observable.<String>create(s -> {
            LOG.info("Start: Executing slow task in Service 1");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            s.onNext("data 1");
            LOG.info("End: Executing slow task in Service 1");
            s.onCompleted();
        }).subscribeOn(Schedulers.io());
    }

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    JmsListener listener;

    @RequestMapping("/send-receive")
    public DeferredResult<HttpEntity<String>> sendAndreceive() {

        DeferredResult<HttpEntity<String>> deferred = new DeferredResult<>(90000);
        final Observable<EventContainer> filtered = listener.jmsStream().
                take(1);
        filtered.subscribe(eventContainer -> {
            deferred.setResult(new HttpEntity<>("SENT " + eventContainer.getBody()));
        }, throwable -> {
            deferred.setErrorResult(new HttpEntity<>("ERROR"));
        }, () -> {
            LOG.info("completed");
        });
        jmsTemplate.send("mailbox-destination",
                session -> session.createTextMessage("jajaja!"));
        return deferred;
    }
}
