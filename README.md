# 📋 System Rezerwacji Wizyt - Spring Boot

Kompletny system zarządzania rezerwacją wizyt lekarskich z autentykacją i autoryzacją.

## ✨ Cechy

- ✅ **3 Encje**: User, Doctor, Appointment
- ✅ **Relacje**: OneToMany, OneToOne (bidirectional)
- ✅ **Role**: PATIENT, DOCTOR, ADMIN
- ✅ **Walidacja**: Jakarta Validation
- ✅ **Lombok**: Automatyczne gettery/settery
- ✅ **Spring Security**: Kontrola dostępu
- ✅ **Spring Data JPA**: ORM z Hibernate
- ✅ **PostgreSQL**: Relacyjna baza danych
- ✅ **REST API**: Pełne CRUD operacje

## 📁 Struktura Katalogów

```
src/main/java/pl/matsalak/appointment_system/
├── domain/
│   ├── entity/
│   │   ├── User.java
│   │   ├── Doctor.java
│   │   └── Appointment.java
│   └── enums/
│       ├── UserRole.java
│       └── AppointmentStatus.java
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
├── dto/
│   ├── UserDTO.java
│   ├── DoctorDTO.java
│   └── AppointmentDTO.java
└── config/
    └── SecurityConfig.java
```

## 🚀 Uruchomienie

### Wymagania
- Java 17+
- Maven 3.8+
- PostgreSQL 12+

### Konfiguracja Bazy Danych

1. **Utwórz bazę danych:**
```sql
CREATE DATABASE appointment_db;
```

2. **Edytuj `application.yml`:**
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/appointment_db
    username: postgres
    password: twoje-hasło
```

### Uruchomienie Aplikacji

```bash
# Kompilacja
mvn clean compile

# Uruchomienie
mvn spring-boot:run

# Lub budowanie JAR
mvn clean package
java -jar target/appointment-system-0.0.1-SNAPSHOT.jar
```

Aplikacja będzie dostępna pod: **http://localhost:8080**

## 📖 Dokumentacja API

### Użytkownicy
- `GET /api/users` - Wszystkich użytkowników
- `GET /api/users/{id}` - Użytkownika po ID
- `GET /api/users/role/{role}` - Użytkowników po roli
- `POST /api/users` - Utworzenie użytkownika
- `PUT /api/users/{id}` - Aktualizacja użytkownika
- `DELETE /api/users/{id}` - Usunięcie użytkownika

### Lekarze
- `GET /api/doctors` - Wszystkich lekarzy
- `GET /api/doctors/{id}` - Lekarza po ID
- `GET /api/doctors/available` - Dostępnych lekarzy
- `GET /api/doctors/specialization/{spec}` - Lekarzy po specjalizacji
- `POST /api/doctors` - Utworzenie profilu lekarza
- `PUT /api/doctors/{id}` - Aktualizacja profilu
- `DELETE /api/doctors/{id}` - Usunięcie profilu

### Wizyty
- `GET /api/appointments` - Wszystkich wizyt
- `GET /api/appointments/{id}` - Wizyty po ID
- `GET /api/appointments/patient/{id}` - Wizyt pacjenta
- `GET /api/appointments/doctor/{id}` - Wizyt lekarza
- `POST /api/appointments` - Rezerwacja wizyty
- `PUT /api/appointments/{id}` - Aktualizacja wizyty
- `PUT /api/appointments/{id}/confirm` - Potwierdzenie wizyty
- `PUT /api/appointments/{id}/cancel` - Anulowanie wizyty
- `DELETE /api/appointments/{id}` - Usunięcie wizyty

## 🔒 Role i Uprawnienia

| Akcja | PATIENT | DOCTOR | ADMIN |
|-------|---------|--------|-------|
| Przeglądaj lekarzy | ✅ | ✅ | ✅ |
| Rezerwuj wizytę | ✅ | ✅ | ✅ |
| Potwierdź wizytę | ✅ | ✅ | ✅ |
| Zarządzaj użytkownikami | ❌ | ❌ | ✅ |

## 📊 Przykłady Żądań

### Utworzenie Pacjenta
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jan",
    "lastName": "Kowalski",
    "email": "jan@example.com",
    "phoneNumber": "123456789",
    "role": "PATIENT"
  }'
```

### Rezerwacja Wizyty
```bash
curl -X POST http://localhost:8080/api/appointments \
  -H "Content-Type: application/json" \
  -d '{
    "patientId": 1,
    "doctorId": 1,
    "appointmentDateTime": "2024-08-15T10:30:00",
    "durationMinutes": 30,
    "notes": "Konsultacja"
  }'
```

## 🗄️ Baza Danych

### User (Użytkownicy)
- `id` - Identyfikator (PK)
- `firstName` - Imię (2-50 znaków)
- `lastName` - Nazwisko (2-50 znaków)
- `email` - Email (unikalny)
- `phoneNumber` - Telefon (9-20 znaków)
- `password` - Hasło
- `role` - Rola (PATIENT, DOCTOR, ADMIN)
- `active` - Czy aktywny
- `createdAt` - Data utworzenia
- `updatedAt` - Data aktualizacji

### Doctor (Lekarze)
- `id` - Identyfikator (PK)
- `userId` - Referencja do User (FK, unique)
- `specialization` - Specjalizacja (3-100 znaków)
- `licenseNumber` - Numer licencji (unikalny)
- `bio` - Biografia (max 500 znaków)
- `available` - Czy dostępny
- `createdAt` - Data utworzenia
- `updatedAt` - Data aktualizacji

### Appointment (Wizyty)
- `id` - Identyfikator (PK)
- `patientId` - Referencja do User (pacjent)
- `doctorId` - Referencja do Doctor
- `appointmentDateTime` - Data i godzina wizyty
- `durationMinutes` - Czas trwania (minuty)
- `notes` - Notatki (max 500 znaków)
- `status` - Status wizyty
- `createdAt` - Data utworzenia
- `updatedAt` - Data aktualizacji

## 📝 Enumeracje

**UserRole:**
- PATIENT - Pacjent
- DOCTOR - Lekarz
- ADMIN - Administrator

**AppointmentStatus:**
- SCHEDULED - Zaplanowana
- CONFIRMED - Potwierdzona
- IN_PROGRESS - W trakcie
- COMPLETED - Ukończona
- CANCELLED - Anulowana
- NO_SHOW - Pacjent się nie pojawił

## 📚 Dodatkowa Dokumentacja

- 📄 [STRUKTURA_PAKIETOW.md](./STRUKTURA_PAKIETOW.md) - Szczegółowy opis pakietów
- 📊 [DIAGRAM_RELACJI.md](./DIAGRAM_RELACJI.md) - Diagram ERD i relacji
- 🔗 [API_EXAMPLES.md](./API_EXAMPLES.md) - Przykłady żądań REST

## 🔧 Technologie

- **Spring Boot** 3.5.16
- **Spring Data JPA** - ORM
- **Spring Security** - Bezpieczeństwo
- **Jakarta Validation** - Walidacja
- **Lombok** - Anotacje
- **PostgreSQL** - Baza danych
- **Maven** - Build tool

## 📝 Wersja

- **Project**: appointment-system
- **Version**: 0.0.1-SNAPSHOT
- **Java**: 17
- **Spring Boot**: 3.5.16

## 🤝 Contributing

Projekt jest gotowy do dalszego rozwoju. Możliwe ulepszenia:
- Dodanie JWT authentication
- Paginacja w listach
- Filtrowanie wizyt
- Email notifications
- Calendar integration
- Unit/Integration testy

## 📄 Licencja

MIT License - patrz LICENSE

---

**Zbudowano z ❤️ przy użyciu Spring Boot**
