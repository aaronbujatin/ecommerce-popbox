package org.xyz.usersvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tests")
public class TestController {


    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<String> greet(Authentication authentication){

        return ResponseEntity.ok("hello from user endpoint");
    }


}
