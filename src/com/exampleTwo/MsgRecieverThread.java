/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.exampleTwo;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;


public class MsgRecieverThread extends Thread {

    @Override
    public void run() {
        ConnectionFactory cf = null;
        Connection con = null;
        Session session = null;
        Destination dest = null;        
        String url = "tcp://localhost:61616";
        MessageConsumer consumer = null;
        Message msg = null;
        TextMessage textMessage = null;
        String text = null;
        try {
            cf = new ActiveMQConnectionFactory(url);
            con = cf.createConnection();
            con.start();
            session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
            dest = session.createQueue("MYQUEUE");
            consumer = session.createConsumer(dest);

            while (true) {
                msg = consumer.receive();
                if (msg instanceof TextMessage) {
                    textMessage = (TextMessage) msg;
                    text = textMessage.getText();
                    System.out.println(text);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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

    public static void main(String[] args) {
        MsgRecieverThread t = null;
        try {
            t = new MsgRecieverThread();
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
