package com.stella.rememberall.common;

import com.stella.rememberall.common.response.OnlyResponseString;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/health")
    public OnlyResponseString healthCheck() {
        return new OnlyResponseString("Health Check");
    }
}
