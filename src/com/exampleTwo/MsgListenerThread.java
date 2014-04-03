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
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;


public class MsgListenerThread extends Thread implements MessageListener {

    
    @Override
    public void run() {
        ConnectionFactory cf = null;
        Connection con = null;
        Session session = null;
        Destination dest = null;        
        String url = "tcp://localhost:61616";
        MessageConsumer consumer = null;       
        try {
            cf = new ActiveMQConnectionFactory(url);
            con = cf.createConnection();
            con.start();
            session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
            dest = session.createQueue("MYQUEUE");
            consumer = session.createConsumer(dest);
            consumer.setMessageListener(this);
            while(true){} // to keep the current thread running
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
    
    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = null;
        String text = null;
        try {
            if (message instanceof TextMessage) {
                textMessage = (TextMessage) message;
                text = textMessage.getText();
                System.out.println(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String [] args){
        MsgListenerThread t = null;
        try{
            t = new MsgListenerThread();
            t.start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
