package com.demo.util.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Login {
    String login;

    public Login(String login) {
        this.login = login;
    }
}
