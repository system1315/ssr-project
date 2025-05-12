package com.kh.demo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
  @Bean
  public ChatClient openAIChatClient(OpenAiChatModel chatModel) {
    return ChatClient.create(chatModel);
  }
}
