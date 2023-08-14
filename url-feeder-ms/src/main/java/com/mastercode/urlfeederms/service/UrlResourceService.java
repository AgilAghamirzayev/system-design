package com.mastercode.urlfeederms.service;

import com.mastercode.urlfeederms.model.Properties;
import com.mastercode.urlfeederms.entity.UrlResource;
import com.mastercode.urlfeederms.repository.UrlResourceRepository;
import com.mastercode.urlfeederms.model.request.UrlResourceRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Log4j2
@Service
@RequiredArgsConstructor
public class UrlResourceService {

    @Value("${url-feeder-ms.cool-down}")
    private Integer coolDown;
    private final Properties properties;
    private final UrlResourceRepository urlRepository;
    private final KafkaService kafkaService;

    @Async
    @Transactional
    public void save(UrlResourceRequest request) {
        log.info("-----------------> {}", Thread.currentThread().getName());
        Optional<UrlResource> existingUrlOpt = urlRepository.findByUrl(request.getUrl());
        Optional<String> optionalContentType = Optional.empty();

        UrlResource urlResource = UrlResource.builder()
                .url(request.getUrl())
                .timesProcessed(0)
                .build();

        if (existingUrlOpt.isPresent()) {
            UrlResource existingUrl = existingUrlOpt.get();
            if (existingUrl.getLastProcessed().getTime() + TimeUnit.DAYS.toMillis(coolDown) >
                    System.currentTimeMillis()) {
                log.info("URL {} already processed on {}", existingUrl.getUrl(), existingUrl.getLastProcessed());
                return;
            }

            urlResource = existingUrl;
            optionalContentType = Optional.of(existingUrl.getContentType());
        }

        urlResource.setLastProcessed(new Timestamp(System.currentTimeMillis()));
        urlResource.setTimesProcessed(urlResource.getTimesProcessed() + 1);

        if (optionalContentType.isEmpty()) {
            optionalContentType = getContentType(request.getUrl());
        }

        if (optionalContentType.isEmpty()) {
            log.warn("Content-Type not found for URL {}", request.getUrl());
            return;
        }

        Optional<String> topicByContentType = getTopicByContentType(optionalContentType.get());
        if (topicByContentType.isEmpty()) {
            log.warn("Content-Type {} not mapped", optionalContentType.get());
            return;
        }

        String topic = topicByContentType.get();

        if (urlResource.getContentType() == null || urlResource.getContentType().isEmpty()) {
            urlResource.setContentType(optionalContentType.get());
        }

        kafkaService.send(topic, urlResource.getUrl());
        urlRepository.save(urlResource);
    }

    private Optional<String> getTopicByContentType(String rawContentType) {
        String contentType = rawContentType.split(";")[0].replace("/", "-");
        log.info("Content-Type: {}", contentType);
        Map<String, String> kafkaTopics = properties.getKafkaTopics();
        if (kafkaTopics.containsKey(contentType)) {
            return Optional.of(kafkaTopics.get(contentType));
        }
        return Optional.empty();
    }

    private Optional<String> getContentType(String path) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URI(path).toURL().openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();
            return Optional.of(connection.getContentType());
        } catch (IOException | URISyntaxException e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }
}
