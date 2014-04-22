/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.exampleFour;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;



public class ConsumerThread extends Thread implements MessageListener{

    private ConnectionFactory cf = null;
    private Connection con = null;
    Destination destination = null;        
    Session session = null;
    
    @Override
    public void run(){        
        MessageConsumer consumer = null;
        try{
            cf = ConnectionManager.getConnectionFactory();
            con = cf.createConnection();
            con.start();
            session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);            
            destination = session.createQueue("MYQUEUE");
            consumer = session.createConsumer(destination);
            consumer.setMessageListener(this);
        }catch(Exception e){
            e.printStackTrace();
        }    
       
    }
    
    @Override
    public void onMessage(Message message) {
        TextMessage msg = null;
        String txt = null;
        try{
            msg = (TextMessage) message;
            txt =  msg.getText();
            System.out.println(txt);
            if(txt != null && txt.equalsIgnoreCase("done")){
                closeResource(con, session);
            }
        }catch(Exception w){
            w.printStackTrace();
        }
    }
    
    private void closeResource(Connection con, Session session) throws Exception{
        
        if(session != null) session.close();
        if(con != null) con.close();
    }
    
}
