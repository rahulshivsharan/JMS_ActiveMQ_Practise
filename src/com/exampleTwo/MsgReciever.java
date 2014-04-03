/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.exampleTwo;

import java.util.concurrent.CountDownLatch;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;


public class MsgReciever implements MessageListener {

    private final CountDownLatch letch = new CountDownLatch(1);

    public void run() throws Exception {
        ConnectionFactory connectionFactory = null;
        Connection connection = null;
        Session session = null;
        Destination destination = null;
        MessageConsumer consumer = null;
        try {
            connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("MYQUEUE");
            consumer = session.createConsumer(destination);
            consumer.setMessageListener(this);
            letch.await();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                final TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                System.out.println(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        try {
            MsgReciever rec = new MsgReciever();
            rec.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
