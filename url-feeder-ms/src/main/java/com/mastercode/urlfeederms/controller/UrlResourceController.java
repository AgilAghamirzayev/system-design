package com.mastercode.urlfeederms.controller;

import com.mastercode.urlfeederms.model.request.UrlResourceRequest;
import com.mastercode.urlfeederms.service.UrlResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UrlResourceController {

    private final UrlResourceService urlService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void submitUrl(@RequestBody UrlResourceRequest request) {
        urlService.save(request);
    }
}
