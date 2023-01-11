package controllers;

import entities.Book;
import entities.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import services.LibraryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/library")
public class GeneralController {

    @GetMapping("/welcome")
    public ResponseEntity<String> welcome(){
        try{
           return new ResponseEntity<>("Welcome to the library", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
