/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topic.one;


import java.util.concurrent.CountDownLatch;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import org.apache.activemq.ActiveMQConnectionFactory;


public class MyTopicSubscriberOne extends Thread implements MessageListener{
    
    public MyTopicSubscriberOne(String name){
        this.setName(name);
    }
    
    private static final String TOPIC_NAME = "MyTopic";
    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final TopicConnectionFactory connectionFactory =  new ActiveMQConnectionFactory(BROKER_URL);
    private final CountDownLatch latch = new CountDownLatch(1);
    @Override
    public void run(){        
        TopicConnection con = null;
        TopicSession session = null;           
        MessageConsumer consumer = null; 
        TopicSubscriber subscriber = null;
        Topic topic = null;
        try{           
            con = connectionFactory.createTopicConnection();            
            session = con.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            topic = session.createTopic(TOPIC_NAME);
            subscriber = session.createSubscriber(topic);
            subscriber.setMessageListener(this);
            con.start();
            latch.await();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(consumer != null)    
                    consumer.close();
                
                if(session != null)
                    session.close();
                
                if(con != null)
                    con.close();
            }catch(JMSException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = null;
        String text = null;
        try {
            if (message instanceof TextMessage) {
                textMessage = (TextMessage) message;
                text = textMessage.getText();
                if (text != null && text.trim().equalsIgnoreCase("done")) {
                    latch.countDown();
                }
                System.out.println(Thread.currentThread().getName()+" - "+text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String [] args){
        MyTopicSubscriberOne [] threads = null;
        try{
            threads = new MyTopicSubscriberOne[]{
                new MyTopicSubscriberOne("NANCY"),
                new MyTopicSubscriberOne("MIRANDA"),
                new MyTopicSubscriberOne("JESSICA")
            };
            for(MyTopicSubscriberOne t : threads){
                t.start();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
