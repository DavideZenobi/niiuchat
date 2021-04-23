package io.dz.niiuchat.messaging;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/chats")
public class ChatApi {

  private final ChatService chatService;

  public ChatApi(ChatService chatService) { this.chatService = chatService; }

  @GetMapping(path = "/chat", produces = MediaType.APPLICATION_JSON_VALUE)
  public void getByGroupId() {

  }

}
