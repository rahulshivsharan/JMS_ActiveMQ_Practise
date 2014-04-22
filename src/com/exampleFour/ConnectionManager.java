/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.exampleFour;

import java.util.Properties;
import javax.jms.ConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;


public class ConnectionManager {
    private static  ConnectionFactory connectionFactory = null;
    static {
        Properties prop = null;
        Context jndiCtx = null;        
        try {
            prop = new Properties();
            prop.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            prop.put(Context.PROVIDER_URL, "tcp://localhost:61616");            
            jndiCtx = new InitialContext(prop);            
            connectionFactory = (ConnectionFactory) jndiCtx.lookup("ConnectionFactory");  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ConnectionManager() {            
    }    
    
    public static ConnectionFactory getConnectionFactory(){
        return connectionFactory;
    }
}
