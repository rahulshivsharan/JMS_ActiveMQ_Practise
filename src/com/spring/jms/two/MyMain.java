/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.jms.two;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class MyMain {
    public static void main(String [] args){
        ApplicationContext ctx = null;
        
        MyMessageConsumer consumerBean = null;
        MyMessageProducer producerBean = null;
        try{
            ctx = new ClassPathXmlApplicationContext("resource/bean-msg.xml");
            consumerBean = (MyMessageConsumer) ctx.getBean("myConsumerBean");
            producerBean = (MyMessageProducer) ctx.getBean("myProducerBean");
            producerBean.sendMessages();
            
        }catch(Exception e){
            e.printStackTrace();
        }    
    }
}
