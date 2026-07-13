package pl.matsalak.appointment_system.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.matsalak.appointment_system.domain.entity.Appointment;
import pl.matsalak.appointment_system.domain.enums.AppointmentStatus;
import pl.matsalak.appointment_system.repository.AppointmentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public Appointment createAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    @Transactional(readOnly = true)
    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsByPatientId(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsByDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return appointmentRepository.findByAppointmentDateTimeBetween(startDateTime, endDateTime);
    }

    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsByDoctorAndDateRange(Long doctorId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return appointmentRepository.findByDoctorIdAndAppointmentDateTimeBetween(doctorId, startDateTime, endDateTime);
    }

    @Transactional(readOnly = true)
    public List<Appointment> getAppointmentsByPatientAndDateRange(Long patientId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return appointmentRepository.findByPatientIdAndAppointmentDateTimeBetween(patientId, startDateTime, endDateTime);
    }

    public Appointment updateAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean appointmentExists(Long id) {
        return appointmentRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment cancelAppointment(Long appointmentId) {
        return appointmentRepository.findById(appointmentId).map(appointment -> {
            appointment.setStatus(AppointmentStatus.CANCELLED);
            return appointmentRepository.save(appointment);
        }).orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
    }

    public Appointment confirmAppointment(Long appointmentId) {
        return appointmentRepository.findById(appointmentId).map(appointment -> {
            appointment.setStatus(AppointmentStatus.CONFIRMED);
            return appointmentRepository.save(appointment);
        }).orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
    }
}
