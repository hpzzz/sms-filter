package com.hpzzz.smsfilter.service;

import com.hpzzz.smsfilter.model.Sms;
import com.hpzzz.smsfilter.storage.PreferenceStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SmsServiceTest {

    private PreferenceStorage preferenceStorage;
    private UrlScannerService scanner;
    private SmsService smsService;

    @BeforeEach
    void setup() {
        preferenceStorage = mock(PreferenceStorage.class);
        scanner = mock(UrlScannerService.class);
        smsService = new SmsService(preferenceStorage, scanner);
    }

    @Test
    void shouldBlockPhishingSms() {
        Sms sms = new Sms("48888888888", "48999999999", "Hello, your delivery status is here: http://phishing.com");

        when(preferenceStorage.isProtected("48999999999")).thenReturn(true);
        when(scanner.extractUrls(sms.getMessage())).thenReturn(List.of("http://phishing.com"));
        when(scanner.isPhishing("http://phishing.com")).thenReturn(true);

        String result = smsService.handleIncomingSms(sms);

        assertEquals("Message blocked (phishing detected)", result);
    }

    @Test
    void shouldAllowSafeSms() {
        Sms sms = new Sms("48888888888", "48999999999", "This is safe text.");

        when(preferenceStorage.isProtected("48999999999")).thenReturn(true);
        when(scanner.extractUrls(sms.getMessage())).thenReturn(Collections.emptyList());

        String result = smsService.handleIncomingSms(sms);

        assertEquals("Message delivered", result);
    }

    @Test
    void shouldIgnoreSmsIfProtectionDisabled() {
        Sms sms = new Sms("48888888888", "48888888888", "Click me: http://phishy.com");

        when(preferenceStorage.isProtected("48999999999")).thenReturn(false);

        String result = smsService.handleIncomingSms(sms);

        assertEquals("Message delivered (protection inactive)", result);
    }
}
