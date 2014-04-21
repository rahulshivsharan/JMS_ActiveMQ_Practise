/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.jms.one;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class MyMainTwo {
    public static void main(String [] args){
        MessageProducerBean producerBean = null;
        MyMessageListener listenerBean = null;
        ApplicationContext ctx = null;
        DateFormat dateFormat = null;
        Thread t = null;
        StringBuffer buffer = null;
        try{
            dateFormat = new SimpleDateFormat("HH:mm:ss.SS a");
            ctx = new ClassPathXmlApplicationContext("resource/bean.xml");
            producerBean = (MessageProducerBean) ctx.getBean("msgProducerBean");
            listenerBean = (MyMessageListener) ctx.getBean("listenerBean");
            t = new Thread(listenerBean);
            t.start();
            for(int x=1; x <= 20; x++){
                buffer = new StringBuffer();
                buffer.append(x).append("> ").append(dateFormat.format(new Date()));
                
                producerBean.send(buffer.toString());
            }
            producerBean.send("done");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
