package es.nami.booking.restaurant.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import es.nami.booking.restaurant.annotation.JsonToString;
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
public class CreateRestaurantGroupRequest {

    @JsonProperty("isNewAccount")
    private boolean isNewAccount;
    private String adminUserEmail;
    private String adminPassword;
    private String groupName;
    private Restaurant firstRestaurant;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonToString
    public static class Restaurant {

        private String name;

    }

}
