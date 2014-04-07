/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.exampleTwo;

import java.util.concurrent.CountDownLatch;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;


public class VirtualQueueConsumer extends Thread implements MessageListener {

    private static final String BROKER_URL = "tcp://localhost:61616";
    private final String QUEUE_NAME;
    private final ConnectionFactory connectionFactory;    
    private final CountDownLatch latch = new CountDownLatch(1);
    public VirtualQueueConsumer(String name) {
        this.connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
        this.QUEUE_NAME = name;
    }

    @Override
    public void run() {
        Connection con = null;
        Session session = null;
        Destination dest = null;
        MessageConsumer consumer = null;
        try {
            con = this.connectionFactory.createConnection();
            con.start();
            session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
            dest = session.createQueue(this.QUEUE_NAME);
            consumer = session.createConsumer(dest);
            consumer.setMessageListener(this);
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(con,session,consumer);
        }
    }

    private void closeConnection(Connection con,Session session, MessageConsumer consumer) {
        try {
            
            if(consumer != null)
                consumer.close();
            
            if (con != null) {
                con.stop();
                con.close();
            }
            
            if(session != null)
                session.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(Message message) {
        handle(message);
    }

    private void handle(Message msg) {
        TextMessage txtMsg = null;
        String txt = null;
        try {
            if (msg instanceof TextMessage) {
                txtMsg = (TextMessage) msg;
                txt = txtMsg.getText();
                if (txt != null && txt.trim().equalsIgnoreCase("done")) {
                    latch.countDown();
                }
                System.out.println(txt);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            VirtualQueueConsumer consumer = new VirtualQueueConsumer("MYQUEUE");
            consumer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
