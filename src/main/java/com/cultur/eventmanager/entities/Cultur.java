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


    @ManyToMany(fetch = FetchType.LAZY, mappedBy="culturList", cascade = CascadeType.ALL)
    private List<Event> eventList;


}
