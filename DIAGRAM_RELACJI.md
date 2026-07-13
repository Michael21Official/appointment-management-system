# Diagram Relacji - System Rezerwacji Wizyt

## Entity Relationship Diagram (ERD)

```
┌─────────────────────┐
│      USER           │
├─────────────────────┤
│ id (PK)             │
│ firstName           │
│ lastName            │
│ email (UNIQUE)      │
│ phoneNumber         │
│ password            │
│ role (ENUM)         │ ◄─── PATIENT, DOCTOR, ADMIN
│ active              │
│ createdAt           │
│ updatedAt           │
└─────────────────────┘
        │ 1
        │
        │ OneToOne (Optional - jeśli role = DOCTOR)
        │
        │ 1
        ▼
┌─────────────────────┐
│      DOCTOR         │
├─────────────────────┤
│ id (PK)             │
│ user_id (FK)        │
│ specialization      │
│ licenseNumber (UQ)  │
│ bio                 │
│ available           │
│ createdAt           │
│ updatedAt           │
└─────────────────────┘
        │ 1
        │
        │ OneToMany
        │
        ▼ *
┌──────────────────────────┐
│    APPOINTMENT           │
├──────────────────────────┤
│ id (PK)                  │
│ patient_id (FK)          │
│ doctor_id (FK)           │
│ appointmentDateTime      │
│ durationMinutes          │
│ notes                    │
│ status (ENUM)            │ ◄─── SCHEDULED, CONFIRMED...
│ createdAt                │
│ updatedAt                │
└──────────────────────────┘
        ▲ *
        │
        │ ManyToOne
        │
        │ 1
┌─────────────────────┐
│      USER           │
│ (PATIENT)           │
└─────────────────────┘


Legend:
PK = Primary Key
FK = Foreign Key
UQ = Unique
ENUM = Enumeration
```

## Relacje Szczegółowe

### 1️⃣ User ↔ Doctor (OneToOne, Optional)
```
User (role = DOCTOR)
  └─ 1:1 ─ Doctor
        └─ specialization, licenseNumber, bio
```
**Bidirectional:**
- User.doctor (Optional<Doctor>)
- Doctor.user (required)
- CascadeType.ALL, orphanRemoval = true

### 2️⃣ Doctor → Appointment (OneToMany)
```
Doctor
  ├─ 1:N ─ Appointment 1
  ├─ 1:N ─ Appointment 2
  └─ 1:N ─ Appointment N
```
**Właściwości:**
- Doctor.appointments (Set<Appointment>)
- Appointment.doctor (required)
- Fetch: LAZY
- Cascade: ALL, orphanRemoval = true

### 3️⃣ User (Patient) → Appointment (OneToMany)
```
User (role = PATIENT)
  ├─ 1:N ─ Appointment 1
  ├─ 1:N ─ Appointment 2
  └─ 1:N ─ Appointment N
```
**Właściwości:**
- User.appointments (Set<Appointment>)
- Appointment.patient (required)
- Fetch: LAZY
- Cascade: ALL, orphanRemoval = true

## Tabel SQL (PostgreSQL)

```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'PATIENT',
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE doctors (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    specialization VARCHAR(100) NOT NULL,
    license_number VARCHAR(20) NOT NULL UNIQUE,
    bio TEXT,
    available BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE appointments (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    appointment_date_time TIMESTAMP NOT NULL,
    duration_minutes INTEGER NOT NULL,
    notes TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE CASCADE
);

CREATE INDEX idx_appointments_patient_id ON appointments(patient_id);
CREATE INDEX idx_appointments_doctor_id ON appointments(doctor_id);
CREATE INDEX idx_appointments_status ON appointments(status);
CREATE INDEX idx_doctors_specialization ON doctors(specialization);
CREATE INDEX idx_users_email ON users(email);
```

## Enumeracje

### UserRole
- `PATIENT` - Pacjent rezerwujący wizyty
- `DOCTOR` - Lekarz prowadzący wizyty
- `ADMIN` - Administrator systemu

### AppointmentStatus
- `SCHEDULED` - Wizyta zaplanowana
- `CONFIRMED` - Wizyta potwierdzona
- `IN_PROGRESS` - Wizyta w trakcie
- `COMPLETED` - Wizyta ukończona
- `CANCELLED` - Wizyta anulowana
- `NO_SHOW` - Pacjent się nie pojawił

## Ograniczenia i Walidacja

| Encja | Pole | Walidacja | Typ |
|-------|------|-----------|-----|
| User | firstName | 2-50 znaków | @NotBlank @Size |
| User | lastName | 2-50 znaków | @NotBlank @Size |
| User | email | Unikalny format | @Email @NotBlank |
| User | phoneNumber | 9-20 znaków | @NotBlank @Size |
| User | password | Min 6 znaków | @NotBlank @Size |
| Doctor | specialization | 3-100 znaków | @NotBlank @Size |
| Doctor | licenseNumber | Unikalny, 5-20 | @NotBlank @Size |
| Appointment | appointmentDateTime | Przyszła data | @Future @NotNull |
| Appointment | durationMinutes | Wymagane | @NotNull |
| Appointment | notes | Max 500 znaków | @Size |

## Operacje Kaskadem

### OnDelete CASCADE
- Usunięcie User (DOCTOR) usuwa Doctor
- Usunięcie Doctor usuwa wszystkie Appointments
- Usunięcie User (PATIENT) usuwa wszystkie Appointments

### OrphanRemoval = true
- Usunięcie Appointment ze zbioru usuwa rekord z BD
- Zapewnia spójność danych

## Strategia Pobierania (Fetch)

**LAZY** - Opóźnione ładowanie
- Doctor.appointments nie są ładowane z Doctor
- Pobieranie tylko na żądanie
- Zapobiega N+1 problem

**Zalecenia:**
```java
// ✅ Dobrze - Lazy loading
Doctor doctor = doctorRepository.findById(1L);

// ⚠️ Ostrożnie - Z JOIN FETCH w custom query
@Query("SELECT d FROM Doctor d JOIN FETCH d.appointments WHERE d.id = :id")
Doctor findWithAppointments(@Param("id") Long id);
```
