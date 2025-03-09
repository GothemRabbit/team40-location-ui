package bham.team.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link bham.team.domain.Message} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MessageDTO implements Serializable {

    private Long id;

    @Lob
    private String content;

    @NotNull
    private Instant timestamp;

    @NotNull
    private Boolean isRead;

    private ConversationDTO convo;

    private UserDetailsDTO sender;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public ConversationDTO getConvo() {
        return convo;
    }

    public void setConvo(ConversationDTO convo) {
        this.convo = convo;
    }

    public UserDetailsDTO getSender() {
        return sender;
    }

    public void setSender(UserDetailsDTO sender) {
        this.sender = sender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageDTO)) {
            return false;
        }

        MessageDTO messageDTO = (MessageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, messageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MessageDTO{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            ", isRead='" + getIsRead() + "'" +
            ", convo=" + getConvo() +
            ", sender=" + getSender() +
            "}";
    }
}
