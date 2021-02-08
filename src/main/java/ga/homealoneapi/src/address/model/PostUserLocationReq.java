package ga.homealoneapi.src.address.model;

import lombok.AccessLevel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import ga.homealoneapi.config.BaseException;
import ga.homealoneapi.config.BaseResponseStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC) // Unit Test 를 위해 PUBLIC
@Getter
public class PostUserLocationReq {

    private double latitude;
    private double longitude;
}
