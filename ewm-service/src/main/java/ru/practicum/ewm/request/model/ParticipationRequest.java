package ru.practicum.ewm.request.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "requests")
@Entity
public class ParticipationRequest {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created", nullable = false)
    private LocalDateTime created;
    @Column(name = "event_id", nullable = false)
    private Long eventId;
    @Column(name = "requester_id", nullable = false)
    private Long requesterId;
    @Column(name = "status", nullable = false)
    private String status;

    public ParticipationRequest(LocalDateTime created, Long eventId, Long requesterId, String status) {
        this.created = created;
        this.eventId = eventId;
        this.requesterId = requesterId;
        this.status = status;
    }
}
