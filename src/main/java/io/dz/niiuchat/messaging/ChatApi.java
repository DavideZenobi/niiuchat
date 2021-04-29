package io.dz.niiuchat.messaging;


import io.dz.niiuchat.authentication.NiiuUser;
import io.dz.niiuchat.domain.tables.pojos.Chats;
import io.dz.niiuchat.domain.tables.pojos.Users;
import io.dz.niiuchat.messaging.dto.GroupOutput;
import java.security.Principal;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/chats")
public class ChatApi {

  private final MessagingService messagingService;

  public ChatApi(MessagingService messagingService) { this.messagingService = messagingService; }

  @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<GroupOutput> getGroups(Principal principal) {

    return messagingService.getGroups(NiiuUser.from(principal).getUser().getId());

  }

}
