package com.example.websocket.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {
	
    private final UserService userService;
	
    @MessageMapping("/user.addUser")
    @SendTo("/user/public")
    public User addUser(
            @Payload User user
    ) {
        userService.saveUser(user);
        return user;
    }
	
    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public User disconnectUser(
            @Payload User user
    ) {
        userService.disconnect(user);
        return user;
    }
	
	/**
	 * ResponseEntity를 사용하면, 객체/커스텀클래스를 응답으로 줄 수 있다.
	 * @return
	 */
	@GetMapping("/users")
    public ResponseEntity<List<User>> findConnectedUsers() {
		
		// if문이 필요하지 않을까?
		return ResponseEntity.ok(userService.findConnectedUsers());
	}

}
