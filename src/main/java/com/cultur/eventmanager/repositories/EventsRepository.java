package com.cultur.eventmanager.repositories;

import com.cultur.eventmanager.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by shantanu on 29/4/17.
 */
@Repository
public interface EventsRepository extends JpaRepository<Event, Integer> {
}
