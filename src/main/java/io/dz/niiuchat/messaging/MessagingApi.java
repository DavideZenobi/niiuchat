package io.dz.niiuchat.messaging;

import io.dz.niiuchat.authentication.NiiuUser;
import io.dz.niiuchat.domain.tables.pojos.Messages;
import io.dz.niiuchat.messaging.dto.CreateGroupInput;
import io.dz.niiuchat.messaging.dto.CreateGroupOutput;
import io.dz.niiuchat.messaging.dto.CreateMessageInput;
import io.dz.niiuchat.messaging.dto.GetMessagesInput;
import io.dz.niiuchat.messaging.dto.GroupOutput;
import io.dz.niiuchat.messaging.dto.MessageDto;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/messaging")
public class MessagingApi {

  private final MessagingService messagingService;

  public MessagingApi(MessagingService messagingService) { this.messagingService = messagingService; }

  @GetMapping(path = "/chats", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<GroupOutput> getGroups(Principal principal) {
    return messagingService.getGroups(NiiuUser.from(principal).getUser().getId());
  }

  @PostMapping(path = "/chats", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CreateGroupOutput> createGroup(
      Principal principal,
      @RequestBody @Valid CreateGroupInput createGroupInput
  ) {
    var niiuUserId = NiiuUser.from(principal).getUser().getId();

    createGroupInput.getUserIds().add(niiuUserId);
    var createGroupOutput = messagingService.createGroup(new ArrayList<>(createGroupInput.getUserIds()), niiuUserId);

    return ResponseEntity.ok(createGroupOutput);
  }

  @PostMapping(path = "/messages", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Messages insertMessage(
      Principal principal,
      @ModelAttribute CreateMessageInput createMessageInput
  ) {
    return messagingService.insertMessage(NiiuUser.from(principal).getUser().getId(), createMessageInput);
  }

  @GetMapping(path = "/messages/{groupId}/", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<MessageDto> getMessagesByGroupId(
      Principal principal,
      @PathVariable String groupId,
      @Valid GetMessagesInput input
  ) {
    var niiuUser = NiiuUser.from(principal).getUser();

    return messagingService.getMessagesByGroupId(niiuUser.getId(), groupId, input.toPageInfo());
  }

  @DeleteMapping(path = "/chats/{groupId}/")
  public void deleteChat(
      Principal principal,
      @PathVariable String groupId
  ) {
    messagingService.deleteChat(NiiuUser.from(principal).getUser().getId(), groupId);
  }

}
