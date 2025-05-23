package com.hpzzz.smsfilter.service;

import com.hpzzz.smsfilter.model.Sms;
import com.hpzzz.smsfilter.storage.PreferenceStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsService {

    private final PreferenceStorage preferenceStorage;
    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);
    private final UrlScannerService scanner;

    @Value("${app.operator.number}")
    private String operatorNumber;

    public SmsService(PreferenceStorage preferenceStorage, UrlScannerService scanner) {
        this.preferenceStorage = preferenceStorage;
        this.scanner = scanner;
    }

    public String handleIncomingSms(Sms sms) {
        String msg = sms.getMessage();

        if (sms.getRecipient().equals(operatorNumber)) {
            if (msg.equals("START")) {
                preferenceStorage.startProtection(sms.getSender());
                logger.info("User {} enabled protection", sms.getSender());
                return "Protection enabled";
            }

            if (msg.equals("STOP")) {
                preferenceStorage.stopProtection(sms.getSender());
                logger.info("User {} disabled protection", sms.getSender());
                return "Protection disabled";
            }
        }

        if (!preferenceStorage.isProtected(sms.getRecipient())) {
            return "Message delivered (protection inactive)";
        }

        List<String> urls = scanner.extractUrls(sms.getMessage());
        for (String url : urls) {
            if (scanner.isPhishing(url)) {
                logger.warn("Blocked message to {} due to phishing attempt", sms.getRecipient());
                return "Message blocked (phishing detected)";
            }
        }

        return "Message delivered";
    }
}
