package ru.practicum.ewm.compilation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events_compilation")
@Entity
public class EventCompilation {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "events_compilation_id")
    private Long eventId;

    @Column(name = "compilation_id")
    private Long compilationId;

    public EventCompilation(Long eventId, Long compilationId) {
        this.eventId = eventId;
        this.compilationId = compilationId;
    }
}
