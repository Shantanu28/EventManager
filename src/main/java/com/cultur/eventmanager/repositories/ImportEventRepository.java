package com.cultur.eventmanager.repositories;

import com.cultur.eventmanager.entities.ImportEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by shantanu on 1/5/17.
 */
@Repository
public interface ImportEventRepository extends JpaRepository<ImportEvent, Integer> {

    @Modifying
    @Query(value = "UPDATE ImportEvent importEvent SET importEvent.workflowState = :workflowState," +
            " importEvent.createdAt = :timestamp, importEvent.updatedAt = :timestamp WHERE importEvent.id = :importEventId")
    void updateTimestamp(@Param("workflowState") String workflowState, @Param("timestamp") Timestamp timestamp, @Param("importEventId") Integer importEventId);
}
