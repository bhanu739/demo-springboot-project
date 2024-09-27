package com.springsec.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@Tag(name = "Test API", description = "API for testing basic endpoints")
public class TestController {

    @GetMapping("/helloworld")
    @Operation(summary = "Test API", description = "This is a test API")
    public String helloWorld() {
        return "Hello World";
    }
}
