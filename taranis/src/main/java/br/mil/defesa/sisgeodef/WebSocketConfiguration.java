package br.mil.defesa.sisgeodef;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

	 
	 @Override
	 public void registerStompEndpoints(StompEndpointRegistry registry) {
		 registry.addEndpoint("/radar-data").setAllowedOrigins("*").withSockJS();
	 }	

	 
	 @Override
	 public void configureWebSocketTransport( WebSocketTransportRegistration registration ) {
		 registration
		 	.setSendTimeLimit( 15 * 1000 )
		 	.setMessageSizeLimit( 5000 * 1024 ) 		// Max incoming message size => 5Mo
		 	.setSendBufferSizeLimit( 5000 * 1024 ); 	// Max outgoing buffer size => 5Mo
		 
         //registration.setMessageSizeLimit(200000); // default : 64 * 1024
         //registration.setSendTimeLimit(20 * 10000); // default : 10 * 10000
         //registration.setSendBufferSizeLimit(3* 512 * 1024); // default : 512 * 1024		 
		 
	 }	 
	 
}
