package io.dz.niiuchat.messaging;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Empty;
import io.dz.niiuchat.authentication.NiiuUser;
import io.dz.niiuchat.messaging.dto.CreateGroupInput;
import io.dz.niiuchat.messaging.dto.CreateGroupOutput;
import io.dz.niiuchat.messaging.dto.GroupOutput;
import io.dz.niiuchat.messaging.dto.MessageInput;
import io.dz.niiuchat.messaging.dto.MessageOutput;
import java.security.Principal;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/messaging")
public class ChatApi {

  private final MessagingService messagingService;

  public ChatApi(MessagingService messagingService) { this.messagingService = messagingService; }

  @GetMapping(path = "/chats", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<GroupOutput> getGroups(Principal principal) {

    return messagingService.getGroups(NiiuUser.from(principal).getUser().getId());

  }

  @PostMapping(path = "/chats", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CreateGroupOutput> createGroup(
      Principal principal,
      @RequestBody @Valid CreateGroupInput createGroupInput
  ) {
    createGroupInput.getUserIds().add(NiiuUser.from(principal).getUser().getId());
    var createGroupOutput = messagingService.createGroup(createGroupInput.getUserIds());

    return ResponseEntity.ok(createGroupOutput);
  }

  @PostMapping(path = "/message", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Empty> insertMessage(
      Principal principal,
      @RequestBody MessageInput messageInput
  ) {
    messagingService.insertMessageText(NiiuUser.from(principal).getUser().getId(), messageInput);

    return ResponseEntity.ok().build();
  }

  @GetMapping(path = "/messages", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<MessageOutput> getMessagesByGroupId() {
    return messagingService.getMessagesByGroupId("dagadsgasdf");
  }

}
