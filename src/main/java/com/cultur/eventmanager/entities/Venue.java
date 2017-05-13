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
@Table(name = "venues")
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private double latitude;

    private double longitude;

    private String address;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "user_id")
    private Integer userId;

    private String city;

    private String state;

    private String country;

    private String street;

    private String zip;

    private String timezone;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.PERSIST)
    private List<Event> eventList;

    public Venue(VenueBuilder venueBuilder) {
        this.name = venueBuilder.name;
        this.latitude = venueBuilder.latitude;
        this.longitude = venueBuilder.longitude;
        this.address = venueBuilder.address;
        this.createdAt = venueBuilder.createdAt;
        this.updatedAt = venueBuilder.updatedAt;
        this.userId = venueBuilder.userId;
        this.city = venueBuilder.city;
        this.state = venueBuilder.state;
        this.country = venueBuilder.country;
        this.street = venueBuilder.street;
        this.zip = venueBuilder.zip;
        this.timezone = venueBuilder.timezone;
        this.eventList = venueBuilder.eventList;
    }

    public static class VenueBuilder {
        private Integer id;
        private String name;
        private double latitude;
        private double longitude;
        private String address;
        private Timestamp createdAt;
        private Timestamp updatedAt;
        private Integer userId;
        private String city;
        private String state;
        private String country;
        private String street;
        private String zip;
        private String timezone;
        private List<Event> eventList;

        public VenueBuilder() {
        }

        public Venue build() {
            return new Venue(this);
        }

        public VenueBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public VenueBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public VenueBuilder withLatitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public VenueBuilder withLongitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public VenueBuilder withAddress(String address) {
            this.address = address;
            return this;
        }

        public VenueBuilder withCreatedAt(Timestamp createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public VenueBuilder withUpdatedAt(Timestamp updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public VenueBuilder withUserId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public VenueBuilder withCity(String city) {
            this.city = city;
            return this;
        }

        public VenueBuilder withState(String state) {
            this.state = state;
            return this;
        }

        public VenueBuilder withCountry(String country) {
            this.country = country;
            return this;
        }

        public VenueBuilder withStreet(String street) {
            this.street = street;
            return this;
        }

        public VenueBuilder withZip(String zip) {
            this.zip = zip;
            return this;
        }

        public VenueBuilder withTimezone(String timezone) {
            this.timezone = timezone;
            return this;
        }

        public VenueBuilder withEventList(List<Event> eventList) {
            this.eventList = eventList;
            return this;
        }
    }
}
