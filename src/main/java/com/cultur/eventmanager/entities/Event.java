package com.cultur.eventmanager.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by shantanu on 29/4/17.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "address")
    private String address;

    @Column(name = "venue_id")
    private Integer venueId;

    @Column(name = "source_url")
    private String sourceUrl;

    @Column(name = "published")
    private Boolean published;

    @Column(name = "status_id")
    private Integer statusId;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "ticket_link")
    private String ticketLink;

    @Column(name = "import_event_id")
    private Integer importEventId;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "start")
    private Timestamp start;

    @Column(name = "end")
    private Timestamp end;

    @Column(name = "source")
    private String source;

    @Column(name = "photo_source_url")
    private String photoSourceUrl;

    @Column(name = "recur_type")
    private String recurType;

    @Column(name = "recur_all_day")
    private Boolean recurAllDay;

    @Column(name = "recur_every_x_days")
    private Integer recurEveryXDays;

    @Column(name = "recur_weekly_on_weekdays")
    private String recurWeeklyOnWeekdays;

    @Column(name = "recur_every_x_weeks")
    private Integer recurEveryXWeeks;

    @Column(name = "recur_monthly_type")
    private String recurMonthlyType;

    @Column(name = "recur_monthly_on_days")
    private String recurMonthlyOnDays;

    @Column(name = "recur_monthly_on_weekdays")
    private String recurMonthlyOnWeekdays;

    @Column(name = "recur_monthly_on_weeknum")
    private String recurMonthlyOnWeeknum;

    @Column(name = "recur_every_x_months")
    private Integer recurEveryXMonths;

    @Column(name = "timezone")
    private String timezone;
}
