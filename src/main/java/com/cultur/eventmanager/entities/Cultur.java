package com.cultur.eventmanager.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Created by shantanu on 3/5/17.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "culturs")
public class Cultur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "cultur", fetch = FetchType.EAGER)
    private List<CulturKeyword> culturKeywordList;


    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "culturs_events", joinColumns = { @JoinColumn(name = "cultur_id") }, inverseJoinColumns = { @JoinColumn(name = "event_id") })
    private List<Event> eventList;


}
