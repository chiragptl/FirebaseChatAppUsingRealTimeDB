package com.example.chatappfirebase.dashboard;

public class MessagesList {
    private String name, mobile, lastMessage, profile;

    private int unseenMessages;

    public MessagesList(String name, String mobile, String profile, String lastMessage, int unseenMessages) {
        this.name = name;
        this.mobile = mobile;
        this.lastMessage = lastMessage;
        this.profile = profile;
        this.unseenMessages = unseenMessages;
    }

    public MessagesList(String getName, String getMobile, String lastMessage, int i) {
        this.name = getName;
        this.mobile = getMobile;
        this.lastMessage = lastMessage;
        this.unseenMessages = i;
    }

    public String getName() {
        return name;
    }

    public String getProfile() {
        return profile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getUnseenMessages() {
        return unseenMessages;
    }

    public void setUnseenMessages(int unseenMessages) {
        this.unseenMessages = unseenMessages;
    }
}
