package com.example.websocket.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		
		// ChatController의 @SendTo("/user")로 메세지를 전송할 수 있게 해준다.
		registry.enableSimpleBroker("/user");
		
		// 클라이언트에서 보낸 메시지가 /app으로 시작하면, 메시지가 ChatController의 @MessageMapping의 메서드로 라우팅 된다.
		registry.setApplicationDestinationPrefixes("/app");
		
		// TODO
		registry.setUserDestinationPrefix("/user");
		
	}
	
	/**
	 *  클라이언트가 WebSocket 서버에 연결할 때 사용할 STOMP 엔드포인트(URL : /ws)를 등록
	 *  클라이언트는 이 엔드포인트(/ws)를 통해 WebSocket 연결을 시작하게 됩니다. 
	 *  SockJS 폴백 옵션도 여기서 설정할 수 있습니다.
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		
		registry.addEndpoint("/ws")
				.withSockJS(); // JavaScript SockJS의 Fallback Option 제공(WebSocket이 지원이 안된다면 다른 방식으로 연결)
	}

	@Override
	public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
		
		// 메세지의 MIME타입을 결정, JSON으로 설정함.
		DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
		resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
		
		// Jackson 라이브러리 => Java객체를 JSON으로 직렬화 및 역직렬화
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		// ObjectMapper => JSON데이터 처리
		converter.setObjectMapper(new ObjectMapper());
		converter.setContentTypeResolver(resolver);
		messageConverters.add(converter);
		
		// Spring 기본 메세지 변환기 설정 유지하면서 위의 수정사항만 반영.
		return false;
	}
	
	

	
}
