package pl.matsalak.appointment_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.matsalak.appointment_system.domain.entity.Appointment;
import pl.matsalak.appointment_system.domain.entity.Doctor;
import pl.matsalak.appointment_system.domain.entity.User;
import pl.matsalak.appointment_system.dto.AppointmentDTO;
import pl.matsalak.appointment_system.service.AppointmentService;
import pl.matsalak.appointment_system.service.DoctorService;
import pl.matsalak.appointment_system.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserService userService;
    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@Valid @RequestBody AppointmentDTO appointmentDTO) {
        User patient = userService.getUserById(appointmentDTO.getPatientId())
            .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
        
        Doctor doctor = doctorService.getDoctorById(appointmentDTO.getDoctorId())
            .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        
        Appointment appointment = Appointment.builder()
            .patient(patient)
            .doctor(doctor)
            .appointmentDateTime(appointmentDTO.getAppointmentDateTime())
            .durationMinutes(appointmentDTO.getDurationMinutes())
            .notes(appointmentDTO.getNotes())
            .status(appointmentDTO.getStatus())
            .build();
        
        Appointment createdAppointment = appointmentService.createAppointment(appointment);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDTO(createdAppointment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Long id) {
        return appointmentService.getAppointmentById(id)
            .map(appointment -> ResponseEntity.ok(mapToDTO(appointment)))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        List<AppointmentDTO> appointments = appointmentService.getAllAppointments().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByPatient(@PathVariable Long patientId) {
        List<AppointmentDTO> appointments = appointmentService.getAppointmentsByPatientId(patientId).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByDoctor(@PathVariable Long doctorId) {
        List<AppointmentDTO> appointments = appointmentService.getAppointmentsByDoctorId(doctorId).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable Long id, @Valid @RequestBody AppointmentDTO appointmentDTO) {
        return appointmentService.getAppointmentById(id).map(existingAppointment -> {
            existingAppointment.setAppointmentDateTime(appointmentDTO.getAppointmentDateTime());
            existingAppointment.setDurationMinutes(appointmentDTO.getDurationMinutes());
            existingAppointment.setNotes(appointmentDTO.getNotes());
            existingAppointment.setStatus(appointmentDTO.getStatus());
            Appointment updated = appointmentService.updateAppointment(existingAppointment);
            return ResponseEntity.ok(mapToDTO(updated));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<AppointmentDTO> confirmAppointment(@PathVariable Long id) {
        Appointment confirmed = appointmentService.confirmAppointment(id);
        return ResponseEntity.ok(mapToDTO(confirmed));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<AppointmentDTO> cancelAppointment(@PathVariable Long id) {
        Appointment cancelled = appointmentService.cancelAppointment(id);
        return ResponseEntity.ok(mapToDTO(cancelled));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        if (appointmentService.appointmentExists(id)) {
            appointmentService.deleteAppointment(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private AppointmentDTO mapToDTO(Appointment appointment) {
        return AppointmentDTO.builder()
            .id(appointment.getId())
            .appointmentDateTime(appointment.getAppointmentDateTime())
            .durationMinutes(appointment.getDurationMinutes())
            .notes(appointment.getNotes())
            .status(appointment.getStatus())
            .patientId(appointment.getPatient().getId())
            .doctorId(appointment.getDoctor().getId())
            .build();
    }
}
