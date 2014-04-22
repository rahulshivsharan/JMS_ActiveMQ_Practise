/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.exampleFour;


public class MyMain {
    public static void main(String [] args){
        ProducerThread producer = null;
        ConsumerThread consumer = null;
        try{
            producer = new ProducerThread();
            consumer = new ConsumerThread();
            producer.start();
            consumer.start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
