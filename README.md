# 🐶 Kennel Backend API - Spring Boot

This is the backend service for **Kennel**, a dog-focused social media and marketplace application. The backend is built using **Spring Boot** and provides secure authentication, user role management, email verification, password reset via OTP, and more.

---

## 🚀 Features

### 🔐 Authentication & Registration

- **User Registration** with OTP verification via **Mailgun**
- **Secure Login** using JWT (access + refresh tokens)
- **Rate Limiting** on critical endpoints (like login, OTP resend)
- **Password Reset Flow**:
  - User requests reset (OTP sent)
  - User verifies OTP
  - User sets new password using a secure one-time token

---

### ⚙️ Components and Architecture

- **Spring Security** for authentication and authorization
- **JWT Token Provider** for stateless session handling
- **Mailgun Integration** for sending email verifications and OTPs
- **Custom Annotations** with **Spring AOP** for features like soft deletion
- **Validation** using Jakarta Bean Validation (`@NotNull`, `@Email`, etc.)
- **Exception Handling** using `@ControllerAdvice` for global error responses
- **DTOs & Entities** are cleanly separated

---

### 👥 Roles and Permissions

- `DOG_OWNER`
- `KENNEL_OWNER`
- `CLINIC_VET`
- `CLINIC_DOCTOR`

Each role has clearly defined behavior, with routes secured using method-level annotations like `@PreAuthorize`.

---

### 🧼 Soft Delete

- Soft delete is implemented using a boolean `isDeleted` flag.
- Custom AOP logic intercepts delete operations and sets the flag instead of permanently deleting data.

---

## 🛠️ Tech Stack

- **Spring Boot 3+**
- **Spring Security**
- **JWT**
- **Spring Data JPA**
- **Mailgun**
- **Rate Limiter (Bucket4J )**
- **Lombok**
- **MapStruct**
- **Validation API**
- **Dockerized**

---

## 📌 API Endpoints Overview

| Method | Endpoint                         | Description                      |
| ------ | -------------------------------- | -------------------------------- |
| POST   | `/auth/register`                 | Register a new user              |
| POST   | `/auth/login`                    | Authenticate user                |
| POST   | `/auth/verify-user`              | Verify email with OTP            |
| POST   | `/auth/resend-verification-code` | Resend OTP email                 |
| POST   | `/auth/forgot`                   | Initiate forgot password process |
| POST   | `/auth/verify-change-token`      | Verify OTP for password reset    |
| PATCH  | `/auth/set-new-password`         | Set new password                 |
| POST   | `/auth/refresh`                  | Refresh access token             |

> All routes are protected with role-based security and rate-limited where necessary.

---

## 🧪 Testing

- Uses `SpringBootTest` for integration tests.
- Test coverage includes:
  - OTP generation
  - Email flow with mocks
  - Token validation and refresh
  - Edge cases like duplicate email, invalid OTP

---

## 💡 Why This Project Stands Out

- ✅ **Secure & Scalable**: Implements modern security best practices.
- ✅ **Modular Design**: Easy to extend to new features or roles.
- ✅ **Clean Code**: Uses DTOs, service layers, and validation throughout.
- ✅ **Real-world Ready**: Integrates email verification, forgot password flow, and soft deletion.
- ✅ **Robust Error Handling**: Consistent error responses using `@ControllerAdvice`.

---

## 🧭 Getting Started

1. Clone the repo:
   ```bash
   git clone https://github.com/pawanacharya0123/kennel.git
   cd kennel/backend
   ```
