/*
 * Copyright (c) 2015.
 * david.navarro.ext@proximus.com, me@davengeo.com
 */

package org.daven.rx.domain;

import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

public class EventContainer {

    private Message message;
    private String messageId;
    private String body;

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

    public EventContainer setBody(String body) {
        this.body = body;
        return this;
    }

    public String getBody() {
        return body;
    }

    public static EventContainer from(Message message) throws JMSException {
        TextMessage textMessage = (ActiveMQTextMessage) message;
        return new EventContainer().
                setMessageId(message.getJMSMessageID()).setBody(textMessage.getText());
    }

}
