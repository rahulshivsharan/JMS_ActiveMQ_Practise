/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topic.one;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;


public class MyTopicPublisherOne extends Thread {

    private static final String TOPIC_NAME = "MyTopic";
    private static final String BROKER_URL = "tcp://localhost:61616";
    private final ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);

    @Override
    public void run() {
        Connection con = null;
        Session session = null;
        Destination dest = null;
        MessageProducer producer = null;
        DateFormat dateFormat = null;        
        try {
            con = connectionFactory.createConnection();
            con.start();
            session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
            dest = session.createTopic(TOPIC_NAME);
            producer = session.createProducer(dest);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            dateFormat = new SimpleDateFormat("h:mm:ss.SS a");
            
            for (int i = 1; i <= 20; i++) {
                TextMessage msg = session.createTextMessage();
                if (i < 20) {
                    msg.setText(i + " MESSAGE : " + dateFormat.format(new Date()));
                } else {
                    msg.setText("done");
                }

                producer.send(msg);
                Thread.sleep(2000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                this.closeResource(session, con);
            }catch(JMSException e){
                e.printStackTrace();
            }
        }
    }
    
    private void closeResource(Session session,Connection con) throws JMSException{
        if(session != null)
            session.close();
        if(con != null)
            con.close();
    }
    

    public static void main(String[] args) {
        Thread t = null;
        try {
            t = new MyTopicPublisherOne();
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
