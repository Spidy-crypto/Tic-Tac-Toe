package com.example.demo.controller;


import com.example.demo.model.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/getUser/{id}")
    public User getUser(@PathVariable Long id){
        return  userService.getUser(id);
    }

    @RequestMapping(method = RequestMethod.POST,value = "/addUser")
    public boolean addUser(@RequestBody User user){
        return userService.addUser(user);
    }
}
