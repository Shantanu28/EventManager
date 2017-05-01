package com.cultur.eventmanager.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by shantanu on 1/5/17.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "event_import_sources")
public class EventImportSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "site_url")
    private String siteUrl;

    private String token;

    private String description;

    private String comments;

    @Column(name = "import_type_id")
    private Integer importTypeId;

    @Column(name = "import_region_id")
    private Integer importRegionId;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "eventImportSource")
    private List<ImportEvent> importEventList;

}
