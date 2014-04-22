/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.exampleFour;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;


public class ProducerThread extends Thread {

    @Override
    public void run() {
        ConnectionFactory cf = null;
        Connection con = null;
        Destination destination = null;

        Session session = null;
        MessageProducer producer = null;
        DateFormat dateFormat = null;
        TextMessage msg = null;
        try {
            cf = ConnectionManager.getConnectionFactory();
            con = cf.createConnection();
            con.start();
            session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);

            destination = session.createQueue("MYQUEUE");
            producer = session.createProducer(destination);
            dateFormat = new SimpleDateFormat("HH:mm:ss.SS a");

            for (int x = 1; x <= 20; x++) {
                msg = session.createTextMessage(x + "> RAW " + dateFormat.format(new Date()));
                producer.send(msg);
                Thread.sleep(1000);
            }
            msg = session.createTextMessage("done");
            producer.send(msg);
            producer.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (producer != null) {
                    producer.close();
                }

                if (session != null) {
                    session.close();
                }

                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
