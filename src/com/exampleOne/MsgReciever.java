/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.exampleOne;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.Message;


public class MsgReciever {
    
    private static void recievMsg() throws Exception{
        
        final ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        
        final Connection connection = connectionFactory.createConnection();
        connection.start();
        
        final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        
        final Destination destination = session.createQueue("MYQUEUE");
        
        final MessageConsumer consumer = session.createConsumer(destination);
        
       // final Message message = (Message) consumer.receive(500000);
        final Message message = (Message) consumer.receive();
        
        if (message instanceof TextMessage) {
            final TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            System.out.println(text);
        }
        
        consumer.close();
        session.close();
        connection.close();
    }
    
    public static void main (String [] args){
        try{
            recievMsg();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
