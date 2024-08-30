package com.example.websocket.chat;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {
	
	private final SimpMessagingTemplate messagingTemplate;
	private final ChatMessageService chatMessageService;
	
	/**
	 * convertAndSendToUser(String user, String destination, Object payload)
	 * >> 채널에 구독하고 있는 사용자들 중 모두에게가 아닌 특정한 사용자에게 메세지를 보낼 수 있도록 해주는 메소드
	 * 
	 *  
	 * @param chatMessage
	 */
	@MessageMapping("/chat")
	public void processMessage(@Payload ChatMessage chatMessage) {
		ChatMessage savedMsg = chatMessageService.save(chatMessage);
		// ex : john/queue/messages
		messagingTemplate.convertAndSendToUser(
				chatMessage.getRecipientId()
				, "/queue/messages"
				, ChatNotification.builder()
					.id(savedMsg.getId())
					.senderId(savedMsg.getSenderId())
					.recipientId(savedMsg.getRecipientId())
					.content(savedMsg.getContent())
					.build()					
				);
	}
	
	@GetMapping("/messages/{senderId}/{recipientId}")
	public ResponseEntity<List<ChatMessage>> findChatMessages(
				@PathVariable String senderId,
				@PathVariable String recipientId
			){
		return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
	}
}
