package com.hpzzz.smsfilter.controller;

import com.hpzzz.smsfilter.model.Sms;
import com.hpzzz.smsfilter.service.SmsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
public class SmsController {

    private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @PostMapping
    public ResponseEntity<String> receiveSms(@RequestBody Sms sms) {
        String response = smsService.handleIncomingSms(sms);
        return ResponseEntity.ok(response);
    }
}
