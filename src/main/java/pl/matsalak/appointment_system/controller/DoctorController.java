package pl.matsalak.appointment_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.matsalak.appointment_system.domain.entity.Doctor;
import pl.matsalak.appointment_system.domain.entity.User;
import pl.matsalak.appointment_system.dto.DoctorDTO;
import pl.matsalak.appointment_system.service.DoctorService;
import pl.matsalak.appointment_system.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<DoctorDTO> createDoctor(@Valid @RequestBody DoctorDTO doctorDTO) {
        User user = userService.getUserById(doctorDTO.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        Doctor doctor = Doctor.builder()
            .user(user)
            .specialization(doctorDTO.getSpecialization())
            .licenseNumber(doctorDTO.getLicenseNumber())
            .bio(doctorDTO.getBio())
            .available(doctorDTO.getAvailable() != null ? doctorDTO.getAvailable() : true)
            .build();
        
        Doctor createdDoctor = doctorService.createDoctor(doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDTO(createdDoctor));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) {
        return doctorService.getDoctorById(id)
            .map(doctor -> ResponseEntity.ok(mapToDTO(doctor)))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        List<DoctorDTO> doctors = doctorService.getAllDoctors().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/available")
    public ResponseEntity<List<DoctorDTO>> getAvailableDoctors() {
        List<DoctorDTO> doctors = doctorService.getAvailableDoctors().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<DoctorDTO>> getDoctorsBySpecialization(@PathVariable String specialization) {
        List<DoctorDTO> doctors = doctorService.getDoctorsBySpecialization(specialization).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/specialization/{specialization}/available")
    public ResponseEntity<List<DoctorDTO>> getAvailableDoctorsBySpecialization(@PathVariable String specialization) {
        List<DoctorDTO> doctors = doctorService.getAvailableDoctorsBySpecialization(specialization).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(doctors);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDTO> updateDoctor(@PathVariable Long id, @Valid @RequestBody DoctorDTO doctorDTO) {
        return doctorService.getDoctorById(id).map(existingDoctor -> {
            existingDoctor.setSpecialization(doctorDTO.getSpecialization());
            existingDoctor.setLicenseNumber(doctorDTO.getLicenseNumber());
            existingDoctor.setBio(doctorDTO.getBio());
            existingDoctor.setAvailable(doctorDTO.getAvailable());
            Doctor updated = doctorService.updateDoctor(existingDoctor);
            return ResponseEntity.ok(mapToDTO(updated));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        if (doctorService.doctorExists(id)) {
            doctorService.deleteDoctor(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private DoctorDTO mapToDTO(Doctor doctor) {
        return DoctorDTO.builder()
            .id(doctor.getId())
            .specialization(doctor.getSpecialization())
            .licenseNumber(doctor.getLicenseNumber())
            .bio(doctor.getBio())
            .available(doctor.getAvailable())
            .userId(doctor.getUser().getId())
            .build();
    }
}
