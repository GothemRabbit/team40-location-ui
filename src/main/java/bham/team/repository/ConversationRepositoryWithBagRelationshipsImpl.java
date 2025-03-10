package bham.team.repository;

import bham.team.domain.Conversation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ConversationRepositoryWithBagRelationshipsImpl implements ConversationRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String CONVERSATIONS_PARAMETER = "conversations";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Conversation> fetchBagRelationships(Optional<Conversation> conversation) {
        return conversation.map(this::fetchProfileDetails).map(this::fetchParticipants);
    }

    @Override
    public Page<Conversation> fetchBagRelationships(Page<Conversation> conversations) {
        return new PageImpl<>(
            fetchBagRelationships(conversations.getContent()),
            conversations.getPageable(),
            conversations.getTotalElements()
        );
    }

    @Override
    public List<Conversation> fetchBagRelationships(List<Conversation> conversations) {
        return Optional.of(conversations).map(this::fetchProfileDetails).map(this::fetchParticipants).orElse(Collections.emptyList());
    }

    Conversation fetchProfileDetails(Conversation result) {
        return entityManager
            .createQuery(
                "select conversation from Conversation conversation left join fetch conversation.profileDetails where conversation.id = :id",
                Conversation.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Conversation> fetchProfileDetails(List<Conversation> conversations) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, conversations.size()).forEach(index -> order.put(conversations.get(index).getId(), index));
        List<Conversation> result = entityManager
            .createQuery(
                "select conversation from Conversation conversation left join fetch conversation.profileDetails where conversation in :conversations",
                Conversation.class
            )
            .setParameter(CONVERSATIONS_PARAMETER, conversations)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Conversation fetchParticipants(Conversation result) {
        return entityManager
            .createQuery(
                "select conversation from Conversation conversation left join fetch conversation.participants where conversation.id = :id",
                Conversation.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Conversation> fetchParticipants(List<Conversation> conversations) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, conversations.size()).forEach(index -> order.put(conversations.get(index).getId(), index));
        List<Conversation> result = entityManager
            .createQuery(
                "select conversation from Conversation conversation left join fetch conversation.participants where conversation in :conversations",
                Conversation.class
            )
            .setParameter(CONVERSATIONS_PARAMETER, conversations)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
