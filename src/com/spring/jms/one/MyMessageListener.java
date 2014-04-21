/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.jms.one;


import javax.jms.TextMessage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;



@Component("listenerBean")
@Scope("prototype")
public class MyMessageListener implements  Runnable{

    @Autowired
    @Qualifier("jmsTemplate")
    private JmsTemplate template;
    
    

    @Override
    public void run() {
        boolean flag = true;
        TextMessage msg = null;
        String txt = null;
        try{
            while(flag){
                //System.out.println("Running...");
                msg = (TextMessage) getTemplate().receive();
                txt = msg.getText();
                System.out.println(txt);
                if(txt != null && txt.equalsIgnoreCase("done")){
                    flag = false;
                }
            }
           
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * @return the template
     */
    public JmsTemplate getTemplate() {
        return template;
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(JmsTemplate template) {
        this.template = template;
    }

   
    
}
