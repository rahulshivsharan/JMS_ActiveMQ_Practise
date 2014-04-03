/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.exampleOne;

import java.util.Enumeration;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;


public class MsgBrowse {

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = null;
        Connection con = null;
        Session session = null;        
        Queue q = null;
        QueueBrowser browser = null;
        String url = "tcp://localhost:61616";
        Enumeration enumeration = null;
        Object obj = null;
        TextMessage txtMsg = null;
        try {
            connectionFactory = new ActiveMQConnectionFactory(url);
            con = connectionFactory.createConnection();
            con.start();
            session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
            q = session.createQueue("MYQUEUE");
            browser = session.createBrowser(q);
            enumeration = browser.getEnumeration();
            while (enumeration.hasMoreElements()) {
                obj = enumeration.nextElement();
                if (obj instanceof TextMessage) {
                    txtMsg = (TextMessage) obj;
                    System.out.println(txtMsg.getText());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
                
                if(con != null){
                    con.close();
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }

        }
    }
}
