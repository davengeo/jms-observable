/*
 * Copyright (c) 2015.
 * david.navarro.ext@proximus.com, me@davengeo.com
 */

package org.daven.rx.listeners;

import org.springframework.stereotype.Component;
import rx.Observable;
import rx.Subscriber;

import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class DigesterListener {

    public Observable<String> digesterStream() {
        return Observable.create((Subscriber<? super String> subscriber) -> {
            String content = buffer.poll();
            if (content == null) {
                subscriber.onCompleted();
            } else {
                subscriber.onNext(content);
            }
        });
    }

    private ConcurrentLinkedQueue<String> buffer = new ConcurrentLinkedQueue<>();

    public void add(String content) {
        buffer.add(content);
    }

}
