# Task Manager App

Ung dung quan ly cong viec (Task Manager) — du an portfolio full-stack gom **Android (Kotlin + Jetpack Compose)** va **Backend (Node.js + Express + MongoDB)**.

---

## Tong quan

| Lop | Cong nghe |
|---|---|
| **Android** | Kotlin, Jetpack Compose, Retrofit, Coroutines, ViewModel |
| **Backend** | Node.js, Express, Mongoose, JWT, express-validator |
| **Database** | MongoDB |

## Tinh nang

### Android
- Dang nhap / Dang ky voi JWT.
- Luu token local (DataStore).
- CRUD cong viec: Xem, Them, Sua (day du truong), Xoa (co xac nhan).
- Loading state chi tiet theo tung thao tac.
- Parse loi Retrofit de hien thong bao than thien.
- Toan bo text UI chuan hoa qua `strings.xml`.

### Backend
- Auth: Register, Login voi JWT.
- CRUD Task bao ve bang middleware auth.
- Validate du lieu: status (`todo/in_progress/done`), priority (`low/medium/high`), dueDate (ISO8601).
- Response chuan hoa: `{ success, message, data }`.
- Thong bao loi bang tieng Viet.

## Kien truc

```
Task_ManagerAPP/
├── app/                          # Android app (Kotlin + Compose)
│   └── src/main/java/.../
│       ├── data/
│       │   ├── model/            # TaskItem, AuthModels, ApiResponse
│       │   ├── network/          # Retrofit ApiClient, Services, ErrorUtils
│       │   ├── repository/       # AuthRepository, TaskRepository
│       │   └── session/          # SessionManager (token)
│       └── ui/
│           ├── auth/             # AuthScreen, AuthViewModel
│           ├── task/             # TaskScreen, TaskViewModel
│           └── theme/            # Color, Theme, Type
├── Task_Manager_backend/
│   └── task_manager_backend/
│       ├── bin/www               # Entry point
│       ├── config/db.js          # MongoDB connection
│       ├── middleware/auth.js    # JWT middleware
│       ├── models/               # User, Task schemas
│       ├── routes/               # auth, tasks routes
│       └── utils/response.js    # sendSuccess / sendError helpers
└── PLAN.md                       # Project plan & checklist
```

## API Endpoints

| Method | Path | Mo ta | Auth |
|---|---|---|---|
| GET | `/api/health` | Health check | No |
| POST | `/api/auth/register` | Dang ky | No |
| POST | `/api/auth/login` | Dang nhap | No |
| GET | `/api/tasks` | Lay danh sach task | Yes |
| POST | `/api/tasks` | Tao task moi | Yes |
| PUT | `/api/tasks/:id` | Sua task | Yes |
| DELETE | `/api/tasks/:id` | Xoa task | Yes |

## Cai dat & Chay

### Backend

```bash
cd Task_Manager_backend/task_manager_backend
cp .env.example .env   # Sua MONGO_URI va JWT_SECRET
npm install
npm run dev            # Dev voi nodemon
```

File `.env` can co:

```
MONGO_URI=mongodb://127.0.0.1:27017/task_manager
JWT_SECRET=supersecret123
PORT=3000
```

### Android

1. Mo project bang Android Studio.
2. Dam bao emulator dang chay va backend dang listen tren port 3000.
3. Base URL mac dinh: `http://10.0.2.2:3000/api/` (emulator -> localhost).
4. Build va Run.

## Tac gia

- **Pham Duc Kien** — FPT Polytechnic (PH60001)
