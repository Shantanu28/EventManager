package com.cultur.eventmanager.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by shantanu on 1/5/17.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "import_events")
public class ImportEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "event_import_source_id")
    private EventImportSource eventImportSource;

    @Column(name = "workflow_state")
    private String workflowState;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "number_of_events_imported")
    private Integer numberOfEventsImported;

    @Column(name = "failure_notes")
    private String failureNotes;
}
