package com.cultur.eventmanager.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id")
    private Venue venue;

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

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "start")
    private Timestamp start;

    @Column(name = "\"end\"")
    private Timestamp end;

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

    @ManyToMany(fetch = FetchType.LAZY, mappedBy="eventList", cascade = CascadeType.PERSIST)
    private List<Cultur> culturList;

    public Event(EventBuilder eventBuilder) {
        this.name = eventBuilder.name;
        this.description = eventBuilder.description;
        this.createdAt = eventBuilder.createdAt;
        this.updatedAt = eventBuilder.updatedAt;
        this.latitude = eventBuilder.latitude;
        this.longitude = eventBuilder.longitude;
        this.address = eventBuilder.address;
        this.venue = eventBuilder.venue;
        this.sourceUrl = eventBuilder.sourceUrl;
        this.published = eventBuilder.published;
        this.statusId = eventBuilder.statusId;
        this.price = eventBuilder.price;
        this.ticketLink = eventBuilder.ticketLink;
        this.importEventId = eventBuilder.importEventId;
        this.userId = eventBuilder.userId;
        this.start = eventBuilder.start;
        this.end = eventBuilder.end;
        this.photoSourceUrl = eventBuilder.photoSourceUrl;
        this.recurType = eventBuilder.recurType;
        this.recurAllDay = eventBuilder.recurAllDay;
        this.recurEveryXDays = eventBuilder.recurEveryXDays;
        this.recurWeeklyOnWeekdays = eventBuilder.recurWeeklyOnWeekdays;
        this.recurEveryXWeeks = eventBuilder.recurEveryXWeeks;
        this.recurMonthlyType = eventBuilder.recurMonthlyType;
        this.recurMonthlyOnDays = eventBuilder.recurMonthlyOnDays;
        this.recurMonthlyOnWeekdays = eventBuilder.recurMonthlyOnWeekdays;
        this.recurMonthlyOnWeeknum = eventBuilder.recurMonthlyOnWeeknum;
        this.recurEveryXMonths = eventBuilder.recurEveryXMonths;
        this.timezone = eventBuilder.timezone;
        this.culturList = eventBuilder.culturList;
    }

    public static class EventBuilder {
        private Integer id;
        private String name;
        private String description;
        private Timestamp createdAt;
        private Timestamp updatedAt;
        private Double latitude;
        private Double longitude;
        private String address;
        private Venue venue;
        private String sourceUrl;
        private Boolean published;
        private Integer statusId;
        private BigDecimal price;
        private String ticketLink;
        private Integer importEventId;
        private Integer userId;
        private Timestamp start;
        private Timestamp end;
        private String photoSourceUrl;
        private String recurType;
        private Boolean recurAllDay;
        private Integer recurEveryXDays;
        private String recurWeeklyOnWeekdays;
        private Integer recurEveryXWeeks;
        private String recurMonthlyType;
        private String recurMonthlyOnDays;
        private String recurMonthlyOnWeekdays;
        private String recurMonthlyOnWeeknum;
        private Integer recurEveryXMonths;
        private String timezone;
        private List<Cultur> culturList;

        public EventBuilder() {
        }

        public Event build() {
            return new Event(this);
        }

        public EventBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public EventBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public EventBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public EventBuilder withCreatedAt(Timestamp createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public EventBuilder withUpdatedAt(Timestamp updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public EventBuilder withLatitude(Double latitude) {
            this.latitude = latitude;
            return this;
        }

        public EventBuilder withLongitude(Double longitude) {
            this.longitude = longitude;
            return this;
        }

        public EventBuilder withAddress(String address) {
            this.address = address;
            return this;
        }

        public EventBuilder withVenue(Venue venue) {
            this.venue = venue;
            return this;
        }

        public EventBuilder withSourceUrl(String sourceUrl) {
            this.sourceUrl = sourceUrl;
            return this;
        }

        public EventBuilder withPublished(Boolean published) {
            this.published = published;
            return this;
        }

        public EventBuilder withStatusId(Integer statusId) {
            this.statusId = statusId;
            return this;
        }

        public EventBuilder withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public EventBuilder withTicketLink(String ticketLink) {
            this.ticketLink = ticketLink;
            return this;
        }

        public EventBuilder withImportEventId(Integer importEventId) {
            this.importEventId = importEventId;
            return this;
        }

        public EventBuilder withUserId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public EventBuilder withStart(Timestamp start) {
            this.start = start;
            return this;
        }

        public EventBuilder withEnd(Timestamp end) {
            this.end = end;
            return this;
        }

        public EventBuilder withPhotoSourceUrl(String photoSourceUrl) {
            this.photoSourceUrl = photoSourceUrl;
            return this;
        }

        public EventBuilder withRecurType(String recurType) {
            this.recurType = recurType;
            return this;
        }

        public EventBuilder withRecurAllDay(Boolean recurAllDay) {
            this.recurAllDay = recurAllDay;
            return this;
        }

        public EventBuilder withRecurEveryXDays(Integer recurEveryXDays) {
            this.recurEveryXDays = recurEveryXDays;
            return this;
        }

        public EventBuilder withRecurWeeklyOnWeekdays(String recurWeeklyOnWeekdays) {
            this.recurWeeklyOnWeekdays = recurWeeklyOnWeekdays;
            return this;
        }

        public EventBuilder withRecurEveryXWeeks(Integer recurEveryXWeeks) {
            this.recurEveryXWeeks = recurEveryXWeeks;
            return this;
        }

        public EventBuilder withRecurMonthlyType(String recurMonthlyType) {
            this.recurMonthlyType = recurMonthlyType;
            return this;
        }

        public EventBuilder withRecurMonthlyOnDays(String recurMonthlyOnDays) {
            this.recurMonthlyOnDays = recurMonthlyOnDays;
            return this;
        }

        public EventBuilder withRecurMonthlyOnWeekdays(String recurMonthlyOnWeekdays) {
            this.recurMonthlyOnWeekdays = recurMonthlyOnWeekdays;
            return this;
        }

        public EventBuilder withRecurMonthlyOnWeeknum(String recurMonthlyOnWeeknum) {
            this.recurMonthlyOnWeeknum = recurMonthlyOnWeeknum;
            return this;
        }

        public EventBuilder withRecurEveryXMonths(Integer recurEveryXMonths) {
            this.recurEveryXMonths = recurEveryXMonths;
            return this;
        }

        public EventBuilder withTimezone(String timezone) {
            this.timezone = timezone;
            return this;
        }

        public EventBuilder withCulturList(List<Cultur> culturList) {
            this.culturList = culturList;
            return this;
        }
    }
}
