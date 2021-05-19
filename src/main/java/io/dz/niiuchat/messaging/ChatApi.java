package io.dz.niiuchat.messaging;

import io.dz.niiuchat.authentication.NiiuUser;
import io.dz.niiuchat.domain.tables.pojos.Messages;
import io.dz.niiuchat.messaging.dto.CreateGroupInput;
import io.dz.niiuchat.messaging.dto.CreateGroupOutput;
import io.dz.niiuchat.messaging.dto.CreateMessageInput;
import io.dz.niiuchat.messaging.dto.GetMessagesInput;
import io.dz.niiuchat.messaging.dto.GroupOutput;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

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
  public Messages insertMessage(
          Principal principal,
          @RequestBody CreateMessageInput createMessageInput
  ) {
    if (createMessageInput.getHasAttachment()) {

    } else {
      return messagingService.insertMessageText(NiiuUser.from(principal).getUser().getId(), createMessageInput);
    }
    return messagingService.insertMessageText(NiiuUser.from(principal).getUser().getId(), createMessageInput);
  }

  @GetMapping(path = "/messages/{groupId}/", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Messages> getMessagesByGroupId(
          Principal principal,
          @PathVariable String groupId,
          @Valid GetMessagesInput input
  ) {
    var niiuUser = NiiuUser.from(principal).getUser();

    return messagingService.getMessagesByGroupId(niiuUser.getId(), groupId, input.toPageInfo());
  }

}
