package com.example.websocket.chat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.websocket.chatroom.ChatRoomService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
	
	private final ChatMessageRepository repository;
	private final ChatRoomService chatRoomService;
	
	public ChatMessage save(ChatMessage chatMessage) {
		String chatId = chatRoomService.getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true)
										.orElseThrow(); // Exception 만들기
		
		chatMessage.setChatId(chatId);
		repository.save(chatMessage);
		return chatMessage;
	}
	
	public List<ChatMessage> findChatMessages(String senderId, String recipientId){
		var chatId = chatRoomService.getChatRoomId(senderId, recipientId, false);
		return chatId.map(repository::findByChatId).orElse(new ArrayList<>());
	}
}
