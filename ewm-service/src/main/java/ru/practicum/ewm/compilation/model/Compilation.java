package ru.practicum.ewm.compilation.model;

import lombok.*;
import ru.practicum.ewm.event.model.Event;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "compilations")
@Entity
public class Compilation {

    @Id
    @Column(name = "compilation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pinned")
    private boolean pinned;

    @Column(name = "title")
    private String title;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "compilation_events", joinColumns = @JoinColumn(name = "compilation_events_id"), inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Event> eventList;

}