package com.example.websocket.chatroom;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
	
	private final ChatRoomRepository chatRoomRepository;

	/**
	 * 
	 * 
	 * @param senderId
	 * @param recipientId
	 * @param createNewRoomIfNotExists
	 * @return
	 */
	public Optional<String> getChatRoomId(String senderId, String recipientId, boolean createNewRoomIfNotExists){
		
		return chatRoomRepository
				.findBySenderIdAndRecipientId(senderId, recipientId)
				.map(ChatRoom::getChatId)
				.or(() ->{
					if(createNewRoomIfNotExists) {
						var chatId = createChatId(senderId, recipientId);
						return Optional.of(chatId);
					}
					return Optional.empty();
				});
	}
	
	private String createChatId(String senderId, String recipientId) {
		
		String chatId = String.format("%s_%s", senderId, recipientId); // 홍길동_김말똥
		
		/* 빌더 패턴(Builder Pattern)
		 * >> 보통은 new ChatRoom 해서 생성자로 하지만, builder()를 이용한다.
		 * >> 생성자 파라미터가 많을 경우 가독성 하락
		 * >> 어떤 값을 먼저 설정하던 상관 없음
		 */
		ChatRoom senderRecipient = ChatRoom.builder()
				.chatId(chatId)
				.senderId(senderId)
				.recipientId(recipientId)
				.build();
		
		ChatRoom recipientSender = ChatRoom.builder()
				.chatId(chatId)
				.senderId(senderId)
				.recipientId(recipientId)
				.build();
		
		chatRoomRepository.save(senderRecipient);
		chatRoomRepository.save(recipientSender);
		
		return chatId;
	}
	
}
