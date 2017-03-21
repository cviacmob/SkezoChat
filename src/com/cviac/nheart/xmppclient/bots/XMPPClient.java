/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cviac.nheart.xmppclient.bots;

import com.google.gson.Gson;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.parsing.ExceptionLoggingCallback;
import org.jivesoftware.smack.sasl.provided.SASLPlainMechanism;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.TLSUtils;

public class XMPPClient implements StanzaListener {
//public static Nheart application;

    public boolean connected = false;
    public boolean loggedin = false;
    public boolean isconnecting = false;

    private String serverAddress;
    public static XMPPTCPConnection connection;

    public String loginUser;
    public String passwordUser;

    public static XMPPClient instance = null;
    public static boolean instanceCreated = false;
    String onlinestatus = "online";
    String offlinestatus = "offline";
    Date onlinestatusdate = new Date();
    Gson gson;
    public XMPPBot bot;
    //private ChatManagerListenerImpl mChatManagerListener;
    //private MMessageListener mMessageListener;

    public XMPPClient(String serverAdress, String logiUser,
            String passwordser) {
        this.serverAddress = serverAdress;
        this.loginUser = logiUser;
        this.passwordUser = passwordser;
    }

    public static XMPPClient getInstance(String server,
            String user, String pass) {

        if (instance == null) {
            instance = new XMPPClient(server, user, pass);
            instanceCreated = true;
        }
        return instance;

    }

    public boolean isConnected() {
        return connected;
    }

    public org.jivesoftware.smack.chat.Chat Mychat;

    ChatManagerListenerImpl mChatManagerListener;
    MMessageListener mMessageListener;

    String text = "";
    String mMessage = "", mReceiver = "";

    static {
        try {
            Class.forName("org.jivesoftware.smack.ReconnectionManager");

        } catch (ClassNotFoundException ex) {
            // problem loading reconnection manager
        }
    }

    public void init(XMPPBot bot) {
        this.bot = bot;
        gson = new Gson();
        mMessageListener = new MMessageListener();
        mChatManagerListener = new ChatManagerListenerImpl();
        initialiseConnection();

    }

    private void initialiseConnection() {

        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration
                .builder();
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        config.setServiceName(serverAddress);
        config.setHost(serverAddress);
        config.setPort(5222);
        config.setDebuggerEnabled(true);
        //config.setUsernameAndPassword("9894250016", "1234");
        onReady(config);

        XMPPTCPConnection.setUseStreamManagementResumptiodDefault(true);
        XMPPTCPConnection.setUseStreamManagementDefault(true);
        connection = new XMPPTCPConnection(config.build());
        connection.setPacketReplyTimeout(20000);
        XMPPConnectionListener connectionListener = new XMPPConnectionListener();
        connection.addConnectionListener(connectionListener);
    }

    public void processPacket(Stanza stanza) throws SmackException.NotConnectedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    static class AcceptAll implements StanzaFilter {

        @Override
        public boolean accept(Stanza packet) {
            return true;
        }
    }

    private void onReady(XMPPTCPConnectionConfiguration.Builder builder) {
        builder.setSecurityMode(ConnectionConfiguration.SecurityMode.ifpossible);
        builder.setCompressionEnabled(false);
        builder.setSendPresence(true);
        {
            try {
                TLSUtils.acceptAllCertificates(builder);
                TLSUtils.disableHostnameVerificationForTlsCertificicates(builder);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(XMPPClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (KeyManagementException ex) {
                Logger.getLogger(XMPPClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        setUpSASL();
        connection = new XMPPTCPConnection(builder.build());
        // connection.addAsyncStanzaListener(this, new AcceptAll());
        XMPPConnectionListener connectionListener = new XMPPConnectionListener();
        connection.addConnectionListener(connectionListener);
        // by default Smack disconnects in case of parsing errors
        connection.setParsingExceptionCallback(new ExceptionLoggingCallback());
    }

    private void setUpSASL() {

        final Map<String, String> registeredSASLMechanisms = SASLAuthentication.getRegisterdSASLMechanisms();
        for (String mechanism : registeredSASLMechanisms.values()) {
            SASLAuthentication.blacklistSASLMechanism(mechanism);
        }
        SASLAuthentication.unBlacklistSASLMechanism(SASLPlainMechanism.NAME);

    }

    public void connect() {
        if (connection.isConnected()) {
            return;
        }
        connected = false;
        try {
            connection.connect();
            connected = true;
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void disconnect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                connection.disconnect();
            }
        }).start();
    }

    public void login() {
        try {
            connection.login(loginUser, passwordUser);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class ChatManagerListenerImpl implements ChatManagerListener {

        @Override
        public void chatCreated(final org.jivesoftware.smack.chat.Chat chat,
                final boolean createdLocally) {
            if (!createdLocally) {
                chat.addMessageListener(mMessageListener);
            }

        }

    }
    
    public void sendMessage(String receiver,String body) {
        Chat Mychat;
        Mychat = ChatManager.getInstanceFor(connection).createChat(
                (receiver + "@" + serverAddress),
                mMessageListener);
        final Message message = new Message();
        
        
        
        message.setBody(body);
        message.setStanzaId(System.currentTimeMillis()+"");
        message.setType(Message.Type.chat);
        try {
            if (connection.isAuthenticated()) {
                Mychat.sendMessage(message);
            } else {
                login();
            }
        } catch (Exception e) {
            e.printStackTrace();
       }
    }
    


    private void sendResponseMessage(String receiver, ChatMessage chatMessage) {
        
        String rsp = bot.response(chatMessage.msg);
        chatMessage.msg = rsp;
        chatMessage.senderName = bot.getBotname();
        chatMessage.sender = bot.getUserid();
        chatMessage.receiver = receiver;
        ChatMessage rspMsg = new ChatMessage();
        rspMsg.msg = rsp;
        rspMsg.converseid = chatMessage.converseid;
        rspMsg.receiver = chatMessage.sender;
        rspMsg.isMine = false;
        rspMsg.sender = bot.getUserid();
        rspMsg.senderName = bot.getBotname(); 
        rspMsg.msgid = "BotMsgID" + System.currentTimeMillis() + "";      
        
        String body = gson.toJson(rspMsg);
        Chat Mychat;
        Mychat = ChatManager.getInstanceFor(connection).createChat(
                (receiver + "@" + serverAddress),
                mMessageListener);
        final Message message = new Message();
        message.setBody(body);
        message.setStanzaId(rspMsg.msgid);
        message.setType(Message.Type.chat);
        try {
            if (connection.isAuthenticated()) {
                Mychat.sendMessage(message);
                System.out.println("Response: "+ rsp);
            } else {
                login();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // throw Exception("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    public class XMPPConnectionListener implements ConnectionListener {

        @Override
        public void connected(final XMPPConnection xmppc) {
            connected = true;
            if (!connection.isAuthenticated()) {
                login();
            }
        }

        @Override
        public void authenticated(XMPPConnection xmppc, boolean bln) {
            loggedin = true;
            System.out.println("authenticated");
            ChatManager.getInstanceFor(connection).addChatListener(
                    mChatManagerListener);

        }

        @Override
        public void connectionClosed() {
            connected = false;
            loggedin = false;
        }

        @Override
        public void connectionClosedOnError(Exception excptn) {
            connected = false;
            loggedin = false;
        }

        @Override
        public void reconnectionSuccessful() {
            connected = true;
            loggedin = false;
        }

        @Override
        public void reconnectingIn(int i) {
            loggedin = false;
        }

        @Override
        public void reconnectionFailed(Exception excptn) {
            connected = false;
            loggedin = false;
        }

    }

    private class MMessageListener implements ChatMessageListener {

        public MMessageListener() {
        }
        @Override
        public void processMessage(final org.jivesoftware.smack.chat.Chat chat, final Message msg) {
            System.out.println("Request: "+ msg);

            if (msg.getType() == Message.Type.chat
                    && msg.getBody() != null) {
                try {
                    final ChatMessage chatMessage = gson.fromJson(
                            msg.getBody(), ChatMessage.class);
                    sendResponseMessage(msg.getFrom(),chatMessage);
                } catch (Exception e) {
                }
            }
        }

    
    }

}
