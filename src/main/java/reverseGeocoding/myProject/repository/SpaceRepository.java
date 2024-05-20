package reverseGeocoding.myProject.repository;

import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reverseGeocoding.myProject.domain.entity.Space;

@Repository
public interface SpaceRepository extends JpaRepository<Space, Long> {
    public boolean existsByPoint(Point point);
}
