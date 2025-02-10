package bham.team.web.rest;

import bham.team.domain.Conversation;
import bham.team.repository.ConversationRepository;
import bham.team.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link bham.team.domain.Conversation}.
 */
@RestController
@RequestMapping("/api/conversations")
@Transactional
public class ConversationResource {

    private static final Logger LOG = LoggerFactory.getLogger(ConversationResource.class);

    private static final String ENTITY_NAME = "conversation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConversationRepository conversationRepository;

    public ConversationResource(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    /**
     * {@code POST  /conversations} : Create a new conversation.
     *
     * @param conversation the conversation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new conversation, or with status {@code 400 (Bad Request)} if the conversation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Conversation> createConversation(@Valid @RequestBody Conversation conversation) throws URISyntaxException {
        LOG.debug("REST request to save Conversation : {}", conversation);
        if (conversation.getId() != null) {
            throw new BadRequestAlertException("A new conversation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        conversation = conversationRepository.save(conversation);
        return ResponseEntity.created(new URI("/api/conversations/" + conversation.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, conversation.getId().toString()))
            .body(conversation);
    }

    /**
     * {@code PUT  /conversations/:id} : Updates an existing conversation.
     *
     * @param id the id of the conversation to save.
     * @param conversation the conversation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conversation,
     * or with status {@code 400 (Bad Request)} if the conversation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the conversation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Conversation> updateConversation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Conversation conversation
    ) throws URISyntaxException {
        LOG.debug("REST request to update Conversation : {}, {}", id, conversation);
        if (conversation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, conversation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conversationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        conversation = conversationRepository.save(conversation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, conversation.getId().toString()))
            .body(conversation);
    }

    /**
     * {@code PATCH  /conversations/:id} : Partial updates given fields of an existing conversation, field will ignore if it is null
     *
     * @param id the id of the conversation to save.
     * @param conversation the conversation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conversation,
     * or with status {@code 400 (Bad Request)} if the conversation is not valid,
     * or with status {@code 404 (Not Found)} if the conversation is not found,
     * or with status {@code 500 (Internal Server Error)} if the conversation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Conversation> partialUpdateConversation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Conversation conversation
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Conversation partially : {}, {}", id, conversation);
        if (conversation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, conversation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conversationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Conversation> result = conversationRepository
            .findById(conversation.getId())
            .map(existingConversation -> {
                if (conversation.getDateCreated() != null) {
                    existingConversation.setDateCreated(conversation.getDateCreated());
                }

                return existingConversation;
            })
            .map(conversationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, conversation.getId().toString())
        );
    }

    /**
     * {@code GET  /conversations} : get all the conversations.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of conversations in body.
     */
    @GetMapping("")
    public List<Conversation> getAllConversations(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all Conversations");
        if (eagerload) {
            return conversationRepository.findAllWithEagerRelationships();
        } else {
            return conversationRepository.findAll();
        }
    }

    /**
     * {@code GET  /conversations/:id} : get the "id" conversation.
     *
     * @param id the id of the conversation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the conversation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Conversation> getConversation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Conversation : {}", id);
        Optional<Conversation> conversation = conversationRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(conversation);
    }

    /**
     * {@code DELETE  /conversations/:id} : delete the "id" conversation.
     *
     * @param id the id of the conversation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConversation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Conversation : {}", id);
        conversationRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
