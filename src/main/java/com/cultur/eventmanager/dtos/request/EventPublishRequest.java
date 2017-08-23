package com.cultur.eventmanager.dtos.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 *
 * Created by shantanu on 29/4/17.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("Event publish request")
public class EventPublishRequest {
	@JsonProperty("source_url")
	@NotEmpty
	@ApiModelProperty(value = "the source website url", required = true)
	private String officialWebSiteUrl;

	@JsonProperty("name")
	@NotEmpty
	@ApiModelProperty(value = "Event title", required = true)
	private String eventName;

	@JsonProperty("description")
	@NotEmpty
	@Size(min = 20)
	@ApiModelProperty(value = "Event description", required = true)
	private String eventDescription;

	@JsonProperty("start")
	@NotEmpty
	@ApiModelProperty(value = "Event start time in GMT/UTC", required = true)
	private String eventStartTime;

	@JsonProperty("end")
	private String eventEndTime;

	@JsonProperty("timezone")
	@NotEmpty
	@ApiModelProperty(value = "Timezone of the event location", required = true)
	private String eventTimeZone;

	@JsonProperty("venue_name")
	@NotEmpty
	@ApiModelProperty(value = "Name of the venue of the event", required = true)
	private String eventVenueName;

	@JsonProperty("address")
	@NotEmpty
	@ApiModelProperty(value = "Address of event", required = true)
	private String eventAddress;

	@JsonProperty("remote_photo_url")
	@ApiModelProperty(value = "Url of the event banner/image/photo")
	private String eventPhotoUrl;

	@JsonProperty("street")
	private String street;

	@JsonProperty("city")
	private String city;

	@JsonProperty("state")
	private String state;

	@JsonProperty("country")
	private String country;

	@JsonProperty("zip")
	private String zip;

	@JsonProperty("price")
	private String eventPrice;

	@JsonProperty("ticket_link")
	private String eventTicketLink;

	@JsonProperty("longitude")
	@NotEmpty
	private String longitude;

	@JsonProperty("latitude")
	@NotEmpty
	private String latitude;

	@JsonProperty("import_src_name")
	@NotEmpty
	private String importSrcName;

	@JsonProperty("import_event_id")
	private String importEventId;

	@JsonProperty("import_secret")
	private String importSecret;

	@JsonProperty("import_status")
	private String importStatus;

	@JsonProperty(value = "event-type")
	private String eventType = "";

	@JsonProperty(value = "recur_type")
	private String recurType = "";

	@JsonProperty(value = "recur_every_x_days")
	private String recurEveryXDays = "";

	@JsonProperty(value = "recur_every_x_weeks")
	private String recurEveryXWeeks = "";

	@JsonProperty(value = "start-date")
	private String startDate = "";

	@JsonProperty(value = "start-time")
	private String startTime = "";

	@JsonProperty(value = "end-date")
	private String endDate = "";

	@JsonProperty(value = "end-time")
	private String endTime = "";

	@JsonProperty(value = "recurring-start-time")
	private String recurringStartTime = "";

	@JsonProperty(value = "recurring-end-time")
	private String recurringEndTime = "";

	@JsonProperty(value = "recurring-start-date")
	private String recurringStartDate = "";

	@JsonProperty(value = "recurring-end-date")
	private String recurringEndDate = "";

	@JsonProperty(value = "recur_weekly_on_weekdays")
	private String recurWeeklyOnWeekdays = "";

	@Override
	public String toString() {
		return "EventPublishRequest{" +
				"officialWebSiteUrl='" + officialWebSiteUrl + '\'' +
				", eventName='" + eventName + '\'' +
				", eventDescription='" + eventDescription + '\'' +
				", eventStartTime='" + eventStartTime + '\'' +
				", eventEndTime='" + eventEndTime + '\'' +
				", eventTimeZone='" + eventTimeZone + '\'' +
				", eventVenueName='" + eventVenueName + '\'' +
				", eventAddress='" + eventAddress + '\'' +
				", eventPhotoUrl='" + eventPhotoUrl + '\'' +
				", street='" + street + '\'' +
				", city='" + city + '\'' +
				", state='" + state + '\'' +
				", country='" + country + '\'' +
				", zip='" + zip + '\'' +
				", eventPrice='" + eventPrice + '\'' +
				", eventTicketLink='" + eventTicketLink + '\'' +
				", longitude='" + longitude + '\'' +
				", latitude='" + latitude + '\'' +
				", importSrcName='" + importSrcName + '\'' +
				", importEventId='" + importEventId + '\'' +
				", importSecret='" + importSecret + '\'' +
				", importStatus='" + importStatus + '\'' +
				", eventType='" + eventType + '\'' +
				", recurType='" + recurType + '\'' +
				", recurEveryXDays='" + recurEveryXDays + '\'' +
				", recurEveryXWeeks='" + recurEveryXWeeks + '\'' +
				", startDate='" + startDate + '\'' +
				", startTime='" + startTime + '\'' +
				", endDate='" + endDate + '\'' +
				", endTime='" + endTime + '\'' +
				", recurringStartTime='" + recurringStartTime + '\'' +
				", recurringEndTime='" + recurringEndTime + '\'' +
				", recurringStartDate='" + recurringStartDate + '\'' +
				", recurringEndDate='" + recurringEndDate + '\'' +
				", recurWeeklyOnWeekdays='" + recurWeeklyOnWeekdays + '\'' +
				'}';
	}
}