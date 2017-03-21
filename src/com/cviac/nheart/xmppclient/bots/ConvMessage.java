/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cviac.nheart.xmppclient.bots;

/**
 *
 * @author Cviac
 */



import java.util.Date;
import java.util.List;





public class ConvMessage  {

    
   
    private String msg;

   
    
    private boolean isMine;

    
    private Date ctime;

    
    private String converseid;

    
    
    private String senderName;


    private String sender;


    
    private String receiver;

    
    
    private String msgid;

    
    
    private int status;


    
  
    public ConvMessage() {
        super();
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public String getConverseid() {
        return converseid;
    }

    public void setConverseid(String converseid) {
        this.converseid = converseid;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
//
//    public static void updateStatus(String msgId, int status) {
//        new Update(ConvMessage.class)
//                .set("status = ?", status)
//                .where("msgid = ?", msgId)
//                .execute();
//        return;
//    }
//
//
//    public static List<ConvMessage> getAll(String converseid) {
//        return new Select()
//                .from(ConvMessage.class)
//                .where("converseid = ?", converseid)
//                //.orderBy("Name ASC")
//                .execute();
//    }

//    public static List<ConvMessage> getConversations() {
//        List<ConvMessage> conversations = SQLiteUtils.rawQuery(ConvMessage.class, "select * from (select * from ChatMessages ORDER BY ctime asc) AS x GROUP BY sender ORDER BY ctime DESC",
//                new String[]{});
//        return conversations;
//    }

//	public static List<ConvMessage> getMessagesFromConversation(int userId, int teamId, String conversationId, boolean isGroupConversation) {
//		List<ConvMessage> messages = new Select().from(ConvMessage.class).where("userId=? AND teamId=? AND conversation_id=? AND is_group_conversation=?", userId, teamId, conversationId, isGroupConversation).orderBy("created_time DESC").execute();
//		return messages;
//	}
//
//	public static void deleteMessages(int teamId) {
//		new Delete().from(ConvMessage.class).where("teamId=?", teamId).execute();
//	}


}