/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.exampleTwo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

        for(int i=1; i <= 20; i++){
            TextMessage msg = session.createTextMessage();
            msg.setText(i+" MESSAGE : "+formate.format(new Date()));
            producer.send(msg);
            Thread.sleep(2000);
        }
        
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
