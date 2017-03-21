/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cviac.nheart.xmppclient.bots;

import java.util.Random;

/**
 *
 * @author Cviac
 */
public class ChatMessage {

    public String msg, sender, converseid, receiver, senderName;
    public String ctime;
    public String msgid;
    public boolean isMine;// Did I send the message.
    public int ack = 0;

    public ChatMessage() {
    }
    
    

    public ChatMessage(String converseId, String Sender, String Receiver, String messageString,
                       String ID, boolean isMINE) {
        msg = messageString;
        isMine = isMINE;
        sender = Sender;
        msgid = ID;
        receiver = Receiver;
        senderName = sender;
        converseid= converseId;

        ctime = "";
    }

    public void setSenderName(String name) {
        senderName = name;
    }

    public void setMsgID() {

        msgid += "-" + String.format("%02d", new Random().nextInt(100));
        
    }
}
