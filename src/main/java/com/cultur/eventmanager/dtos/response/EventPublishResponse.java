package com.cultur.eventmanager.dtos.response;

import com.cultur.eventmanager.enums.ResponseStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by shantanu on 29/4/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EventPublishResponse {

    private ResponseStatus status;

}
