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

    public ImportEvent(ImportEventBuilder builder) {
        this.eventImportSource = builder.eventImportSource;
        this.workflowState = builder.workflowState;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.numberOfEventsImported = builder.numberOfEventsImported;
        this.failureNotes = builder.failureNotes;
    }

    public static class ImportEventBuilder {

        private Integer id;
        private EventImportSource eventImportSource;
        private String workflowState;
        private Timestamp createdAt;
        private Timestamp updatedAt;
        private Integer numberOfEventsImported;
        private String failureNotes;

        public ImportEventBuilder() {

        }

        public ImportEvent build() {
            return new ImportEvent(this);
        }

        public ImportEventBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public ImportEventBuilder withEventImportSource(EventImportSource eventImportSource) {
            this.eventImportSource = eventImportSource;
            return this;
        }

        public ImportEventBuilder withWorkflowState(String workflowState) {
            this.workflowState = workflowState;
            return this;
        }

        public ImportEventBuilder withCreatedAt(Timestamp createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ImportEventBuilder withUpdatedAt(Timestamp updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public ImportEventBuilder withNumberOfEventsImported(Integer numberOfEventsImported) {
            this.numberOfEventsImported = numberOfEventsImported;
            return this;
        }

        public ImportEventBuilder withFailureNotes(String failureNotes) {
            this.failureNotes = failureNotes;
            return this;
        }
    }


}
