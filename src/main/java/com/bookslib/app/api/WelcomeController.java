package com.bookslib.app.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

@RestController
@RequestMapping("/welcome")
public class WelcomeController {

    private static final Logger log = LoggerFactory.getLogger(WelcomeController.class);

    @GetMapping
    public ResponseEntity<Map<String, String>> welcome() {
        var response = Map.of(
                "message", "Welcome to the library",
                "date", new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime())
        );
        return ResponseEntity.ok(response);

    }

}
