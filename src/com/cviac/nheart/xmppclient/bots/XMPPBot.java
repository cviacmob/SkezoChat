/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cviac.nheart.xmppclient.bots;

//import static chatbot.ChatBot.bot;
//import static chatbot.ChatBot.chat;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cviac.nheart.xmppclient.bots.XMPPClient;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.MagicStrings;


/**
 *
 * @author Cviac
 */
public class XMPPBot {
    
    private String botname;
    private String botpath;
    private String xmppserver;
    private String userid;
    private String password;

//    private static final String DOMAIN = "ec2-35-162-147-104.us-west-2.compute.amazonaws.com";
//    private static final String USERNAME = "9894250016";
//    private static final String PASSWORD = "1234";

    private Bot bot;

    private Chat chat;

    public XMPPClient client;
    

    public XMPPBot (String botname, String botpath, String xmppserver, String userid,String password){
        this.botname=botname;
        this.botpath=botpath;
        this.xmppserver=xmppserver;
        this.userid=userid;
        this.password=password;
        bot = new Bot(botname,botpath,"chat");
         chat = new Chat(bot);
        
    }

    public void Init() {
        
        client = XMPPClient.getInstance(xmppserver, userid, password);
        client.init(this);
        client.connect();
    }
    
    
    
    public static void main(String[] args) {

        //XMPPBot bot = new XMPPBot("test", MagicStrings.root_path);
        XMPPBot bot = new XMPPBot(args[0], args[1],args[2],args[3],args[4]);
        
        System.out.println("File Readed Sucessfully");     
        bot.Init();
           
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(XMPPBot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }       
    }
    
    public String response(String inmsg) {
        return chat.multisentenceRespond(inmsg);
    }

    public String getBotname() {
        return botname;
    }

    public void setBotname(String botname) {
        this.botname = botname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


    

}
