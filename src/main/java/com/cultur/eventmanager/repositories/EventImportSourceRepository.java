package com.cultur.eventmanager.repositories;

import com.cultur.eventmanager.entities.EventImportSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by shantanu on 1/5/17.
 */
@Repository
public interface EventImportSourceRepository extends JpaRepository<EventImportSource, Integer> {

    EventImportSource findByName(String importSrcName);

    @Query(value = "SELECT max(ie.id) as maxImportSourceId FROM EventImportSource eis" +
            "  INNER JOIN ImportEvent ie ON (eis.id = ie.eventImportSource) WHERE eis.name = :importSrcName")
    Integer getMaxImportSourceId(@Param("importSrcName") String importSrcName);
}
