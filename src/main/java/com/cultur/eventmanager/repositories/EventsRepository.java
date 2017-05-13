package com.cultur.eventmanager.repositories;

import com.cultur.eventmanager.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by shantanu on 29/4/17.
 */
@Repository
public interface EventsRepository extends JpaRepository<Event, Integer> {

    List<Event> findByNameIgnoreCaseAndLatitudeAndLongitude(String name, double latitude, double longitude);

    @Query(value = "SELECT event.id as maxImportSourceId FROM Event event" +
            "  WHERE event.statusId = :statusId")
    List<Integer> findByStatusId(@Param("statusId") Integer statusId);
}
