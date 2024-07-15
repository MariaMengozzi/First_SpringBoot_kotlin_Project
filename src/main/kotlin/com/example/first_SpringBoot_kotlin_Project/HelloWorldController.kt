package com.example.first_SpringBoot_kotlin_Project

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController //we are telling to spring that this is a Rest Controller
@RequestMapping("api/hello") //this controller will be responsible for all "api/hello" requests

class HelloWorldController {

    @GetMapping //this will be the get endpoint for the route -> api/hello if we set @GetMapping("springboot") that will be for the api/hello/springboot root
    fun helloWorld(): String = "Hello, this is a REST endpoint! "
}