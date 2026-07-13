package pl.matsalak.appointment_system.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.matsalak.appointment_system.domain.enums.AppointmentStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDTO {

    private Long id;

    @NotNull(message = "Appointment date and time is required")
    @Future(message = "Appointment must be scheduled in the future")
    private LocalDateTime appointmentDateTime;

    @NotNull(message = "Duration in minutes is required")
    private Integer durationMinutes;

    @Size(max = 500)
    private String notes;

    private AppointmentStatus status;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;
}
