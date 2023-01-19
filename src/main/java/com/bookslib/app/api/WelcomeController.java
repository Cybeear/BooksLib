package com.bookslib.app.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/library/welcome")
@Slf4j
public class WelcomeController {

    @GetMapping
    public ResponseEntity<Map<String, String>> welcome() {
        var response = Map.of(
                "date", new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime()),
                "message", "Welcome to the library"
                );
        return ResponseEntity.ok(response);
    }
}
