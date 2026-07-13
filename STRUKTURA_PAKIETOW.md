# Struktura Pakietów - System Rezerwacji Wizyt

## 📁 Architektura Projektu

```
pl.matsalak.appointment_system/
├── domain/
│   ├── entity/
│   │   ├── User.java              # Encja użytkownika z rolami
│   │   ├── Doctor.java            # Encja lekarza
│   │   └── Appointment.java       # Encja wizyty
│   └── enums/
│       ├── UserRole.java          # Role: PATIENT, DOCTOR, ADMIN
│       └── AppointmentStatus.java # Statusy wizyt
├── repository/
│   ├── UserRepository.java
│   ├── DoctorRepository.java
│   └── AppointmentRepository.java
├── service/
│   ├── UserService.java
│   ├── DoctorService.java
│   └── AppointmentService.java
├── controller/
│   ├── UserController.java
│   ├── DoctorController.java
│   └── AppointmentController.java
└── dto/
    ├── UserDTO.java
    ├── DoctorDTO.java
    └── AppointmentDTO.java
```

## 🔑 Klucze Relacji

### User (Użytkownik)
- **@OneToOne** → Doctor (opcjonalnie, jeśli rola = DOCTOR)
- **@OneToMany** → Appointment (wizyty pacjenta)
- **Role**: PATIENT, DOCTOR, ADMIN

### Doctor (Lekarz)
- **@OneToOne** → User (odwrotna relacja, bidirectional)
- **@OneToMany** → Appointment (wizyty lekarza)

### Appointment (Wizyta)
- **@ManyToOne** → User (pacjent)
- **@ManyToOne** → Doctor (lekarz)
- **Status**: SCHEDULED, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED, NO_SHOW

## 🛡️ Walidacja

### User
- firstName, lastName: 2-50 znaków
- email: unikalny, format email
- phoneNumber: 9-20 znaków
- password: min 6 znaków

### Doctor
- specialization: 3-100 znaków
- licenseNumber: 5-20 znaków, unikalny
- bio: max 500 znaków

### Appointment
- appointmentDateTime: przyszła data
- durationMinutes: obowiązkowe
- notes: max 500 znaków
- patient i doctor: obowiązkowe

## 🔧 Technologie

- **Spring Data JPA** - ORM, repozytoria
- **Lombok** - Anotacje @Getter, @Setter, @Builder
- **Jakarta Validation** - @NotBlank, @Email, @Size, @Future
- **PostgreSQL** - Baza danych (skonfigurowana w pom.xml)
- **Spring Security** - Autoryzacja po rolach

## 📝 Przykład Użycia

```java
// Utworzenie pacjenta
User patient = User.builder()
    .firstName("Jan")
    .lastName("Kowalski")
    .email("jan@example.com")
    .phoneNumber("123456789")
    .password("password123")
    .role(UserRole.PATIENT)
    .build();

// Utworzenie lekarza
User doctorUser = User.builder()
    .firstName("Dr")
    .lastName("Nowak")
    .email("dr.nowak@example.com")
    .phoneNumber("987654321")
    .password("password123")
    .role(UserRole.DOCTOR)
    .build();

Doctor doctor = Doctor.builder()
    .user(doctorUser)
    .specialization("Kardiologia")
    .licenseNumber("LIC123456")
    .available(true)
    .build();

// Rezerwacja wizyty
Appointment appointment = Appointment.builder()
    .patient(patient)
    .doctor(doctor)
    .appointmentDateTime(LocalDateTime.now().plusDays(7))
    .durationMinutes(30)
    .notes("Kontrola zdrowia")
    .status(AppointmentStatus.SCHEDULED)
    .build();
```

## 🚀 Następne Kroki

1. Dodaj **Controllers** do obsługi HTTP requestów
2. Skonfiguruj **Security** dla ról
3. Dodaj **Exception handling** i error responses
4. Napisz **Unit/Integration testy**
5. Dodaj **Pagination** do list repozytoriów
6. Skonfiguruj **aplikację** w application.yml/properties

## 📦 Zależności

Projekt zawiera już wszystkie niezbędne zależności:
- spring-boot-starter-data-jpa
- spring-boot-starter-validation
- spring-boot-starter-security
- lombok
- postgresql

Brak dodatkowych zależności do zainstalowania ✅
