package com.demo.util.websocket.model;

import lombok.Data;

@Data
public class GroupMessage {
    String groupId;
    String message;
    int type;

    public GroupMessage(String groupId, String message, int type) {
        this.groupId = groupId;
        this.message = message;
        this.type = type;
    }
}
