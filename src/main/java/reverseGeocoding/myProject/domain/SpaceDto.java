package reverseGeocoding.myProject.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.locationtech.jts.geom.Point;

@Data
@AllArgsConstructor
public class SpaceDto {
    private Point point;
    private String roadAddr;

    public Space toEntity() {
        return Space.builder()
                .point(point)
                .roadAddr(roadAddr)
                .build();
    }

}
