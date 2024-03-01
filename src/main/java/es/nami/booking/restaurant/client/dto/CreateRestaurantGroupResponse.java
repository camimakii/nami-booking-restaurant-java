package es.nami.booking.restaurant.client.dto;

import es.nami.booking.restaurant.annotation.JsonToString;
import es.nami.booking.restaurant.auth.dto.AuthenticationResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonToString
public class CreateRestaurantGroupResponse {

    private long newRestaurantGroupId;
    private long newRestaurantId;
    private AuthenticationResponse auth;

}
