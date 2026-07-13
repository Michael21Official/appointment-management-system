package pl.matsalak.appointment_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.matsalak.appointment_system.domain.entity.Doctor;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByLicenseNumber(String licenseNumber);

    List<Doctor> findBySpecialization(String specialization);

    List<Doctor> findByAvailableTrue();

    List<Doctor> findBySpecializationAndAvailableTrue(String specialization, Boolean available);
}
