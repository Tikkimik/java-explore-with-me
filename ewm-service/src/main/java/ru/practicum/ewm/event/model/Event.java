package ru.practicum.ewm.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.location.model.Location;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
@Entity
public class Event {

    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "annotation", nullable = false)
    private String annotation;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category", referencedColumnName = "category_id", nullable = false)
    private Category category;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @ManyToOne(optional = false)
    @JoinColumn(name = "initiator", referencedColumnName = "user_id", nullable = false)
    private User initiator;

    @ManyToOne(optional = false)
    @JoinColumn(name = "location", referencedColumnName = "location_id", nullable = false)
    private Location location;

    @Column(name = "paid")
    private boolean paid;

    @Column(name = "participant_limit")
    private Long participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private boolean requestModeration;

    @Column(name = "confirmed_requests")
    private long confirmedRequests;

    @Column(name = "state")
    private String state;

    @Column(name = "title")
    private String title;

    @Column(name = "views")
    private Long views;

    public Event(String annotation, Category category, String description, LocalDateTime eventDate, Location location,
                 boolean paid, Long participantLimit, boolean requestModeration, String title, LocalDateTime createdOn,
                 String state, Long views, User initiator) {

        this.annotation = annotation;
        this.category = category;
        this.description = description;
        this.eventDate = eventDate;
        this.location = location;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.requestModeration = requestModeration;
        this.title = title;
        this.createdOn = createdOn;
        this.state = state;
        this.views = views;
        this.initiator = initiator;
    }

    public Event(String annotation, Category category, String description, LocalDateTime eventDate, boolean paid, Long participantLimit, String title) {
        this.annotation = annotation;
        this.category = category;
        this.description = description;
        this.eventDate = eventDate;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.title = title;
    }
}