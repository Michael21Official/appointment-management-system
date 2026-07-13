package pl.matsalak.appointment_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDTO {

    private Long id;

    @NotBlank(message = "Specialization is required")
    @Size(min = 3, max = 100)
    private String specialization;

    @NotBlank(message = "License number is required")
    @Size(min = 5, max = 20)
    private String licenseNumber;

    @Size(max = 500)
    private String bio;

    private Boolean available;
    private Long userId;
}
