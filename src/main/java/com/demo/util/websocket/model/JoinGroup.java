package com.demo.util.websocket.model;

import lombok.Data;

@Data
public class JoinGroup {
    String userId;
    String groupId;
    int type;

    public JoinGroup(String userId, String groupId, int type) {
        this.userId = userId;
        this.groupId = groupId;
        this.type = type;
    }
}
