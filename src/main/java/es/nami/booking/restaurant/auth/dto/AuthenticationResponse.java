package es.nami.booking.restaurant.auth.dto;

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
public class AuthenticationResponse {

    private String userEmail;
    private String accessToken;
    private String refreshToken;

}
