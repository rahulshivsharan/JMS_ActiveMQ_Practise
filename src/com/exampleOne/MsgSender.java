/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.exampleOne;

import java.beans.DesignMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;


public class MsgSender {
    
    
    private static void sendMsg() throws Exception{
        final String url = "tcp://localhost:61616";
        final ConnectionFactory cf = new ActiveMQConnectionFactory(url);
        Connection con = cf.createConnection();
        con.start();
        
        final Session session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
        
        final Destination destination = session.createQueue("MYQUEUE");
        
        final MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        DateFormat formate = new SimpleDateFormat("d MMM yyyy h:mm:ss a");
//        String dateTime = formate.format(new Date());        
//        final TextMessage message = session.createTextMessage();
//        message.setText("THE MESSAGE SEND ON "+dateTime);
//        producer.send(message);
        
        TextMessage [] messages = new TextMessage[] {
            session.createTextMessage(),
            session.createTextMessage(),
            session.createTextMessage()
        };
        
        
        messages[0].setText("MY FIRST MSG : "+formate.format(new Date()));
        producer.send(messages[0]);
        messages[1].setText("MY SECOND MSG : "+formate.format(new Date()));
        producer.send(messages[1]);
        messages[2].setText("MY THIRD MSG : "+formate.format(new Date()));
        producer.send(messages[2]);
        
        
        session.close();
        con.close();
    }
    
    public static void main(String [] args){        
        try{
            sendMsg();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
