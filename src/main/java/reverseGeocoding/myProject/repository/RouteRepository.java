package reverseGeocoding.myProject.repository;

import org.locationtech.jts.geom.LineString;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reverseGeocoding.myProject.domain.entity.Route;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    public boolean existsByLineString(LineString line);
}
