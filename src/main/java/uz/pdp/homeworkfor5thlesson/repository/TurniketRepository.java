package uz.pdp.homeworkfor5thlesson.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.homeworkfor5thlesson.entity.Turniket;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TurniketRepository extends JpaRepository<Turniket, Long> {

    Optional<Turniket> findByTurniketIdAndClosed(UUID turniketId, boolean closed);
    List<Turniket> findAllByTurniketId(UUID turniketId);
}
