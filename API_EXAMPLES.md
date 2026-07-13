# API REST - Przykłady Użycia

## 🔐 Konfiguracja Żądań

```
Base URL: http://localhost:8080/api
Content-Type: application/json
```

## 👥 Użytkownicy (User API)

### 1. Utworzenie nowego użytkownika
```http
POST /api/users
Content-Type: application/json

{
  "firstName": "Jan",
  "lastName": "Kowalski",
  "email": "jan.kowalski@example.com",
  "phoneNumber": "123456789",
  "role": "PATIENT"
}
```

**Response 201:**
```json
{
  "id": 1,
  "firstName": "Jan",
  "lastName": "Kowalski",
  "email": "jan.kowalski@example.com",
  "phoneNumber": "123456789",
  "role": "PATIENT",
  "active": true
}
```

### 2. Pobranie użytkownika po ID
```http
GET /api/users/1
```

### 3. Pobranie wszystkich użytkowników
```http
GET /api/users
```

### 4. Pobranie użytkowników po roli
```http
GET /api/users/role/DOCTOR
GET /api/users/role/PATIENT
GET /api/users/role/ADMIN
```

### 5. Aktualizacja użytkownika
```http
PUT /api/users/1
Content-Type: application/json

{
  "firstName": "Jan",
  "lastName": "Kowalski Updated",
  "phoneNumber": "987654321"
}
```

### 6. Usunięcie użytkownika
```http
DELETE /api/users/1
```

---

## 👨‍⚕️ Lekarze (Doctor API)

### 1. Utworzenie profilu lekarza
```http
POST /api/doctors
Content-Type: application/json

{
  "userId": 2,
  "specialization": "Kardiologia",
  "licenseNumber": "LIC-2024-001",
  "bio": "Doświadczony kardiolog z 10-letnim stażem",
  "available": true
}
```

**Response 201:**
```json
{
  "id": 1,
  "specialization": "Kardiologia",
  "licenseNumber": "LIC-2024-001",
  "bio": "Doświadczony kardiolog z 10-letnim stażem",
  "available": true,
  "userId": 2
}
```

### 2. Pobranie lekarza po ID
```http
GET /api/doctors/1
```

### 3. Pobranie wszystkich lekarzy
```http
GET /api/doctors
```

### 4. Pobranie dostępnych lekarzy
```http
GET /api/doctors/available
```

### 5. Pobranie lekarzy po specjalizacji
```http
GET /api/doctors/specialization/Kardiologia
```

### 6. Pobranie dostępnych lekarzy po specjalizacji
```http
GET /api/doctors/specialization/Kardiologia/available
```

### 7. Aktualizacja profilu lekarza
```http
PUT /api/doctors/1
Content-Type: application/json

{
  "specialization": "Kardiologia",
  "licenseNumber": "LIC-2024-001",
  "bio": "Doświadczony kardiolog",
  "available": false
}
```

### 8. Usunięcie profilu lekarza
```http
DELETE /api/doctors/1
```

---

## 📅 Wizyty (Appointment API)

### 1. Rezerwacja wizyty
```http
POST /api/appointments
Content-Type: application/json

{
  "patientId": 1,
  "doctorId": 1,
  "appointmentDateTime": "2024-08-15T10:30:00",
  "durationMinutes": 30,
  "notes": "Konsultacja kardiologiczna",
  "status": "SCHEDULED"
}
```

**Response 201:**
```json
{
  "id": 1,
  "appointmentDateTime": "2024-08-15T10:30:00",
  "durationMinutes": 30,
  "notes": "Konsultacja kardiologiczna",
  "status": "SCHEDULED",
  "patientId": 1,
  "doctorId": 1
}
```

### 2. Pobranie wizyty po ID
```http
GET /api/appointments/1
```

### 3. Pobranie wszystkich wizyt
```http
GET /api/appointments
```

### 4. Pobranie wizyt pacjenta
```http
GET /api/appointments/patient/1
```

### 5. Pobranie wizyt lekarza
```http
GET /api/appointments/doctor/1
```

### 6. Aktualizacja wizyty
```http
PUT /api/appointments/1
Content-Type: application/json

{
  "appointmentDateTime": "2024-08-15T14:00:00",
  "durationMinutes": 45,
  "notes": "Konsultacja - zmiana godziny",
  "status": "SCHEDULED"
}
```

### 7. Potwierdzenie wizyty
```http
PUT /api/appointments/1/confirm
```

**Response 200:**
```json
{
  "id": 1,
  "appointmentDateTime": "2024-08-15T10:30:00",
  "durationMinutes": 30,
  "notes": "Konsultacja kardiologiczna",
  "status": "CONFIRMED",
  "patientId": 1,
  "doctorId": 1
}
```

### 8. Anulowanie wizyty
```http
PUT /api/appointments/1/cancel
```

### 9. Usunięcie wizyty
```http
DELETE /api/appointments/1
```

---

## 🔒 Bezpieczeństwo (Security)

### Reguły Dostępu

| Endpoint | PATIENT | DOCTOR | ADMIN | ANONYMOUS |
|----------|---------|--------|-------|-----------|
| GET /doctors | ✅ | ✅ | ✅ | ✅ |
| POST /doctors | ❌ | ❌ | ✅ | ❌ |
| POST /appointments | ✅ | ✅ | ✅ | ❌ |
| GET /appointments | ✅ | ✅ | ✅ | ❌ |
| PUT /users/:id | ✅ (own) | ✅ (own) | ✅ | ❌ |
| DELETE /users/:id | ❌ | ❌ | ✅ | ❌ |

### Błędy HTTP

```json
// 400 Bad Request - Błąd walidacji
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "details": {
    "firstName": "First name must be between 2 and 50 characters"
  }
}

// 401 Unauthorized - Brak autoryzacji
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Authentication required"
}

// 403 Forbidden - Brak uprawnień
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access denied"
}

// 404 Not Found - Zasób nie znaleziony
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Resource not found"
}

// 409 Conflict - Konflikt (np. duplikat email)
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "Email already exists"
}
```

---

## 📝 Scenariusze Użycia

### Scenariusz 1: Pacjent rezerwuje wizytę

```bash
# 1. Pacjent loguje się lub tworzy konto
POST /api/users
{
  "firstName": "Jan",
  "lastName": "Kowalski",
  "email": "jan@example.com",
  "phoneNumber": "123456789",
  "role": "PATIENT"
}

# 2. Pacjent przegląda dostępnych lekarzy kardiologów
GET /api/doctors/specialization/Kardiologia/available

# 3. Pacjent rezerwuje wizytę
POST /api/appointments
{
  "patientId": 1,
  "doctorId": 1,
  "appointmentDateTime": "2024-08-15T10:30:00",
  "durationMinutes": 30,
  "notes": "Pierwsza konsultacja"
}

# 4. Pacjent potrafi zobaczyć swoje wizyty
GET /api/appointments/patient/1
```

### Scenariusz 2: Lekarz zarządza wizytami

```bash
# 1. Lekarz przegląda swoje wizyty
GET /api/appointments/doctor/1

# 2. Lekarz potwierdza wizytę
PUT /api/appointments/1/confirm

# 3. Lekarz anuluje wizytę
PUT /api/appointments/2/cancel
```

### Scenariusz 3: Administrator zarządza użytkownikami

```bash
# 1. Admin potrafi zobaczyć wszystkich użytkowników
GET /api/users

# 2. Admin potrafi zobaczyć wszystkich pacjentów
GET /api/users/role/PATIENT

# 3. Admin potrafi zobaczyć wszystkich lekarzy
GET /api/users/role/DOCTOR

# 4. Admin usuwać użytkownika
DELETE /api/users/1
```
