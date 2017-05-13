package com.cultur.eventmanager.repositories;

import com.cultur.eventmanager.entities.Cultur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springfox.documentation.annotations.Cacheable;

import java.util.List;

/**
 * Created by shantanu on 3/5/17.
 */
@Repository
public interface CulturRepository extends JpaRepository<Cultur, Integer> {
}