package com.sky.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;

/**
 * Author: 梁雨佳
 * Date: 2024/1/7 15:58:54
 * Description:
 */
@RestController
public class RedirectController {
    @Autowired
    private HttpServletResponse response;

    @GetMapping("/sky")
    public ResponseEntity redirect () {
        return ResponseEntity.status(302)
                .location(URI.create("http://192.168.171.129/sky/#/login"))
                .build();
    }
}
