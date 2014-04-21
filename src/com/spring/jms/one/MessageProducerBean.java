/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.jms.one;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;


@Component("msgProducerBean")
@Scope("prototype")
public class MessageProducerBean {
    
    @Autowired
    @Qualifier("jmsTemplate")
    private JmsTemplate template;

    /**
     * @return the template
     */
    public JmsTemplate getTemplate() {
        return template;
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(JmsTemplate template) {
        this.template = template;
    }
    
    public void send(final String text) throws Exception{
        getTemplate().send(new MessageCreator() {

            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(text);
            }
        });
    }
}
