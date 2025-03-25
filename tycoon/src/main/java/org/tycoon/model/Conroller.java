package org.tycoon.model;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Conroller {

    @GetMapping("/")
    public ResponseEntity<String> index(){
        return ResponseEntity.ok("Connect Tycoon");
    }
}
