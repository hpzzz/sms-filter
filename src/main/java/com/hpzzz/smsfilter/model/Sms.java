package com.hpzzz.smsfilter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sms {
    private String sender;
    private String recipient;
    private String message;
}

