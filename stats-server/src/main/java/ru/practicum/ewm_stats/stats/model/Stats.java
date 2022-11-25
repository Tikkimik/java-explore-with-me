package ru.practicum.ewm_stats.stats.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stat")
public class Stats {

    @Id
    @Column(name = "stat_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "app")
    private String app;

    @NotNull
    @Column(name = "uri")
    private String uri;

    @NotNull
    @Column(name = "id")
    private String ip;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

}
