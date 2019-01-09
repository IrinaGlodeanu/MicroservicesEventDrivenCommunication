package irina.rabbit.restGateway.controller;

import irina.rabbit.restGateway.dispatcher.CommandDispatcher;
import irina.rabbit.restGateway.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private CommandDispatcher messageDispatcher;

    @PostMapping
    public ResponseEntity createUser(@RequestBody @Valid User user) {

        messageDispatcher.createUser(user);

        return new ResponseEntity(HttpStatus.CREATED);
    }
}
