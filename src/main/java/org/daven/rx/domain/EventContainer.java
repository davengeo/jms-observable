/*
 * Copyright (c) 2015.
 * david.navarro.ext@proximus.com, me@davengeo.com
 */

package org.daven.rx.domain;

import javax.jms.Message;

public class EventContainer {

    private Message message;
    private String messageId;

    public String getMessageId() {
        return messageId;
    }

    public Message getMessage() {
        return message;
    }

    public EventContainer setMessage(Message message) {
        this.message = message;
        return this;
    }

    public EventContainer setMessageId(String messageId) {
        this.messageId = messageId;
        return this;
    }
}
