package com.mastercode.htmlfileworker.service;

import com.mastercode.htmlfileworker.listener.HtmlListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Log4j2
@Service
public class UrlProcessor {

    @Async
    public void process(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            Elements links = document.select("a[href]");
            for (Element link : links) {
                log.info("Extracted: {}", link.attr("href"));
            }
        } catch (IOException e) {
            log.error("Exception: ", e);
        }
    }

}
