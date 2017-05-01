package com.cultur.eventmanager.repositories;

import com.cultur.eventmanager.entities.ImportEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by shantanu on 1/5/17.
 */
@Repository
public interface ImportEventRepository extends JpaRepository<ImportEvent, Integer> {

}
