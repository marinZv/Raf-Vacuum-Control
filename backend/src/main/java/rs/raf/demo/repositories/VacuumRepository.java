package rs.raf.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.raf.demo.domain.entities.vacuum.Vacuum;
import rs.raf.demo.domain.entities.vacuum.VacuumStatus;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
public interface VacuumRepository extends JpaRepository<Vacuum, Long> {



    @Query("select v from Vacuum v where v.createdBy.email = :email " +
            "and (:vacuumName is null or v.name like %:vacuumName%) " +
            "and ((:statusList) is null or v.status in (:statusList)) " +
            "and (cast(:dateFrom as date) is null or v.createdDate >= :dateFrom) " +
            "and (cast(:dateTo as date) is null or v.createdDate <= :dateTo) " +
            "and v.active = true ")
    List<Vacuum> findAllVacuumsByParameters(String email, String vacuumName, Collection<VacuumStatus> statusList, Date dateFrom, Date dateTo);

}
