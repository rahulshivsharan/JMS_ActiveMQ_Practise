/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.jms.two;

import java.util.concurrent.CountDownLatch;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;


public class MyMessageConsumer implements  MessageListener{

    private Destination destination;
    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private MessageConsumer consumer;
    
    
    
    public void init() {
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);     
            consumer = session.createConsumer(destination);
            consumer.setMessageListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the destination
     */
    public Destination getDestination() {
        return destination;
    }

    /**
     * @param destination the destination to set
     */
    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    /**
     * @return the connectionFactory
     */
    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    /**
     * @param connectionFactory the connectionFactory to set
     */
    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void onMessage(Message message) {
        TextMessage msg = null;
        String str = null;
        try{
            msg = (TextMessage) message;
            str = msg.getText();
            System.out.println(str);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void distroy() {
        try {
            if (consumer != null) {
                consumer.close();
            }

            if (session != null) {
                session.close();
            }

            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
