package ga.homealoneapi.src.address.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
public class GetRecentlyLocationRes {
    private final double latitude;
    private final double longitude;
    private final String parcelAddressing;
    private final String streetAddressing;

    public GetRecentlyLocationRes(String parcelAddressing, String streetAddressing, double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.parcelAddressing = parcelAddressing;
        this.streetAddressing = streetAddressing;
    }

}
