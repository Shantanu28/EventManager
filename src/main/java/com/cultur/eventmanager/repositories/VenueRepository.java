package com.cultur.eventmanager.repositories;

import com.cultur.eventmanager.entities.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by shantanu on 2/5/17.
 */
@Repository
public interface VenueRepository extends JpaRepository<Venue, Integer> {

    List<Venue> findByLatitudeAndLongitude(double latitude, double longitude);
}
