package com.hpzzz.smsfilter.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UrlScannerService {

    private final RestTemplate restTemplate;

    public UrlScannerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${app.webrisk.url}")
    private String webRiskApiUrl;

    @Value("${app.webrisk.auth}")
    private String authHeader;

    private static final Logger logger = LoggerFactory.getLogger(UrlScannerService.class);

    public List<String> extractUrls(String text) {
        Pattern pattern = Pattern.compile("https?://\\S+");
        Matcher matcher = pattern.matcher(text);
        return matcher.results().map(MatchResult::group).toList();
    }

    @Cacheable(value = "urlScanResults", key = "#url")
    public boolean isPhishing(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = Map.of("uri", url);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(webRiskApiUrl, request, String.class);
            return response.getBody().contains("phishing");
        } catch (RestClientException e) {
            logger.warn("Phishing check failed: {}", e.getMessage());
            return false;
        }
    }
}
