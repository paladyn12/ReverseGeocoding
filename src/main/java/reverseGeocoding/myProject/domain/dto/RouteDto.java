package reverseGeocoding.myProject.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import reverseGeocoding.myProject.domain.entity.Route;
import reverseGeocoding.myProject.domain.entity.Space;

@Data
@AllArgsConstructor
public class RouteDto {
    private LineString lineString;
    private String stdRoadAddr;
    private String tgRoadAddr;

    public Route toEntity() {
        return Route.builder()
                .lineString(lineString)
                .stdRoadAddr(stdRoadAddr)
                .tgRoadAddr(tgRoadAddr)
                .build();
    }

}
