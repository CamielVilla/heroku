package nl.thelastages.website.controller;

import jakarta.validation.Valid;
import nl.thelastages.website.service.UserService;
import nl.thelastages.website.model.dto.CreateUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("thelastages")
public class UserController {
    private final UserService userService;
    @Autowired
    ApplicationEventPublisher eventPublisher;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/addemail")
    public ResponseEntity<Boolean> addEmail (@Valid @RequestBody CreateUserDto dto) {
        return ResponseEntity.ok(userService.addEmail(dto));
    }

}
