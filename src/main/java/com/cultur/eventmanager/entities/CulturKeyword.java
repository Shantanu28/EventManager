package com.cultur.eventmanager.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by shantanu on 3/5/17.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "cultur_keywords")
public class CulturKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cultur_id")
    private Cultur cultur;

    private String keyword;
}
