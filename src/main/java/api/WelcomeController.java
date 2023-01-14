package api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/library/welcome")
public class WelcomeController {

    @GetMapping("")
    public ResponseEntity<Map<String, String>> welcome() {
        Map<String, String> response = new LinkedHashMap<>();
        response.put("message", "Welcome to the library");
        response.put("date", new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime()));
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
