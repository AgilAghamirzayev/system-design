package com.mastercode.htmlfileworker.listener;

import com.mastercode.htmlfileworker.service.UrlProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Log4j2
@Component
@RequiredArgsConstructor
public class HtmlListener {

    private final UrlProcessor urlProcessor;

    @KafkaListener(topics = "html_topic")
    public void consume(String message) {
       log.info("Received message: {}", message);
       urlProcessor.process(message);
    }
}
