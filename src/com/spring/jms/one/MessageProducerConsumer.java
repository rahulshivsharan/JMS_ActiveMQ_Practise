/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.jms.one;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;


@Component("producerBean")
@Scope("prototype")
public class MessageProducerConsumer {

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

    public void send(final String message) throws Exception {
        getTemplate().send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(message);
            }
        });
    }

    public void recieve() throws Exception {
        TextMessage msg = (TextMessage) getTemplate().receive();
        String str = msg.getText();
        System.out.println(str);
    }

    public static void main(String[] args) {
        ApplicationContext ctx = null;
        MessageProducerConsumer producerBean = null;
        MessageProducerConsumer consumerBean = null;
        DateFormat format = null;
        try {
            format = new SimpleDateFormat("HH:mm:ss.SS a");
            ctx = new ClassPathXmlApplicationContext("resource/bean.xml");
            producerBean = (MessageProducerConsumer) ctx.getBean("producerBean");
            consumerBean = (MessageProducerConsumer) ctx.getBean("producerBean");
            producerBean.send(format.format(new Date()));
            consumerBean.recieve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
