package io.dz.niiuchat.messaging;

import io.dz.niiuchat.configuration.CacheConfiguration;
import io.dz.niiuchat.messaging.repository.ChatRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class MessagingCachedService {

    private final ChatRepository chatRepository;

    public MessagingCachedService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Cacheable(cacheNames = CacheConfiguration.USERS_BY_GROUP, unless = "#result.size() == 0")
    public Set<Long> getUserIdsForGroup(String groupId) {
        return new HashSet<>(chatRepository.getUserIdsByGroupId(groupId));
    }

}
