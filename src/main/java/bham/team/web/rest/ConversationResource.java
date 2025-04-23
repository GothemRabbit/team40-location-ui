package bham.team.web.rest;

import bham.team.repository.ConversationRepository;
import bham.team.service.ConversationService;
import bham.team.service.dto.ConversationDTO;
import bham.team.service.dto.MessageDTO;
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
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link bham.team.domain.Conversation}.
 */
@RestController
@RequestMapping("/api/conversations")
public class ConversationResource {

    private static final Logger LOG = LoggerFactory.getLogger(ConversationResource.class);

    private static final String ENTITY_NAME = "conversation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConversationService conversationService;

    private final ConversationRepository conversationRepository;

    public ConversationResource(ConversationService conversationService, ConversationRepository conversationRepository) {
        this.conversationService = conversationService;
        this.conversationRepository = conversationRepository;
    }

    /**
     * {@code POST  /conversations} : Create a new conversation.
     *
     * @param conversationDTO the conversationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new conversationDTO, or with status {@code 400 (Bad Request)} if the conversation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ConversationDTO> createConversation(@Valid @RequestBody ConversationDTO conversationDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save Conversation : {}", conversationDTO);
        if (conversationDTO.getId() != null) {
            throw new BadRequestAlertException("A new conversation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        conversationDTO = conversationService.save(conversationDTO);
        return ResponseEntity.created(new URI("/api/conversations/" + conversationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, conversationDTO.getId().toString()))
            .body(conversationDTO);
    }

    /**
     * {@code PUT  /conversations/:id} : Updates an existing conversation.
     *
     * @param id the id of the conversationDTO to save.
     * @param conversationDTO the conversationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conversationDTO,
     * or with status {@code 400 (Bad Request)} if the conversationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the conversationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ConversationDTO> updateConversation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ConversationDTO conversationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Conversation : {}, {}", id, conversationDTO);
        if (conversationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, conversationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conversationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        conversationDTO = conversationService.update(conversationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, conversationDTO.getId().toString()))
            .body(conversationDTO);
    }

    /**
     * {@code PATCH  /conversations/:id} : Partial updates given fields of an existing conversation, field will ignore if it is null
     *
     * @param id the id of the conversationDTO to save.
     * @param conversationDTO the conversationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conversationDTO,
     * or with status {@code 400 (Bad Request)} if the conversationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the conversationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the conversationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ConversationDTO> partialUpdateConversation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ConversationDTO conversationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Conversation partially : {}, {}", id, conversationDTO);
        if (conversationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, conversationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conversationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConversationDTO> result = conversationService.partialUpdate(conversationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, conversationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /conversations} : get all the conversations.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of conversations in body.
     */
    @GetMapping("")
    public List<ConversationDTO> getAllConversations(
        @RequestParam(name = "filter", required = false) String filter,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("productstatus-is-null".equals(filter)) {
            LOG.debug("REST request to get all Conversations where productStatus is null");
            return conversationService.findAllWhereProductStatusIsNull();
        }
        LOG.debug("REST request to get all Conversations");
        return conversationService.findAll();
    }

    /**
     * {@code GET  /conversations/:id} : get the "id" conversation.
     *
     * @param id the id of the conversationDTO to retrieve.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ConversationDTO> getConversation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Conversation : {}", id);
        Optional<ConversationDTO> conversationDTO = conversationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(conversationDTO);
    }

    @GetMapping("/{id}/messages")
    public ResponseEntity<List<MessageDTO>> getConversationMessages(@PathVariable("id") Long conversationId) {
        LOG.debug("REST request to get Messages of Conversation : {}", conversationId);

        if (!conversationRepository.existsById(conversationId)) {
            throw new BadRequestAlertException("Conversation not found", ENTITY_NAME, "idnotfound");
        }

        List<MessageDTO> messages = conversationService.findAllMessagesByConversationId(conversationId);

        return ResponseEntity.ok(messages);
    }

    /**
     * {@code DELETE  /conversations/:id} : delete the "id" conversation.
     *
     * @param id the id of the conversationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConversation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Conversation : {}", id);
        conversationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/my")
    public List<ConversationDTO> getMyVibes() {
        LOG.debug("REST request to fetch current user's convos");
        return conversationService.grabMyConvos();
    }
}
