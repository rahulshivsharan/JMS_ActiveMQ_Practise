/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.jms.two;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;


public class MyMessageProducer  {

    private Destination destination;
    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private MessageProducer producer;

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

    public void init() {
        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            producer = session.createProducer(destination);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void distroy() {
        try {
            if (producer != null) {
                producer.close();
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

    
    public void sendMessages() {
        DateFormat formate = null;
        TextMessage msg = null;
        StringBuffer buffer = null;
        try {
            formate = new SimpleDateFormat("HH:mm:ss.SS a");
            
            for(int x=1; x <= 5; x++){
                buffer = new StringBuffer();
                buffer.append(x).append("> ").append(formate.format(new Date()));
                msg = session.createTextMessage();
                msg.setText(buffer.toString());
                producer.send(msg);
                Thread.sleep(1000);
            }
            msg = session.createTextMessage();
            msg.setText("done");
            producer.send(msg);
        } catch (Exception w) {
            w.printStackTrace();
        }
    }
}
