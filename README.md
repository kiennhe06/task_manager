# 🚀 Task Manager Fullstack (Android & Node.js)

**Task Manager** là một giải pháp quản lý công việc toàn diện, được xây dựng với kiến trúc hiện đại, tập trung vào hiệu năng, tính bảo mật và trải nghiệm người dùng mượt mà trên nền tảng Android.

---

## 🌟 Tính năng tiêu biểu (Key Features)

### 🔐 Hệ thống bảo mật & Xác thực
- **Xác thực JWT:** Triển khai luồng đăng nhập/đăng ký an toàn với JSON Web Token.
- **Token Management:** Tự động đính kèm Token vào Header thông qua OkHttp Interceptors, đảm bảo tính nhất quán cho mọi API Request.

### 📅 Quản lý công việc thông minh
- **Full CRUD Operations:** Thêm mới, chỉnh sửa chi tiết, xóa (có xác nhận) và cập nhật trạng thái công việc thời gian thực.
- **Phân loại ưu tiên:** Hỗ trợ gắn nhãn Priority (High, Medium, Low) và Status (Todo, In-progress, Done).
- **Dashboard Thống kê:** Màn hình tổng quan hiển thị trực quan số lượng công việc theo từng trạng thái.

### 🎨 Trải nghiệm người dùng đỉnh cao
- **Material Design 3:** Giao diện hiện đại, tinh gọn với các thành phần của Material 3.
- **Responsive UI:** Tự động điều chỉnh giao diện theo kích thước màn hình, hỗ trợ Dark Mode và Dynamic Colors.
- **Localization:** Hỗ trợ đa ngôn ngữ (Tiếng Việt) với hệ thống quản lý string tập trung.

---

## 🛠 Kiến trúc & Công nghệ (Tech Stack)

### **Android Application**
- **Language:** Kotlin 1.9+
- **UI Framework:** Jetpack Compose (Declarative UI)
- **Architecture:** MVVM (Model-View-ViewModel) + Repository Pattern.
- **Networking:** Retrofit 2 + OkHttp 4 (RESTful API).
- **Asynchronous:** Kotlin Coroutines & StateFlow (Quản lý trạng thái UI luồng bất đồng bộ).
- **Error Handling:** Triển khai một lớp Wrapper `ApiResponse<T>` để chuẩn hóa mọi phản hồi từ Server và xử lý lỗi tập trung.

### **Backend Server**
- **Runtime:** Node.js (Express.js Framework).
- **Database:** MongoDB Atlas (NoSQL) kết hợp Mongoose ODM.
- **Authentication:** Passport.js / JWT Strategy.
- **Validation:** Kiểm soát dữ liệu đầu vào nghiêm ngặt cho các trường Task & Auth.

---

## 🏗 Cấu trúc dự án (Project Structure)

```text
app/src/main/java/fpl/ph60001/task_managerapp/
├── data/
│   ├── model/         # Data classes (Task, User, ApiResponse)
│   ├── network/       # Retrofit Services & Interceptors
│   └── repository/    # Cầu nối giữa Data Source và ViewModel
├── ui/
│   ├── task/          # Màn hình quản lý Task & List
│   ├── dashboard/     # Màn hình thống kê dữ liệu
│   ├── auth/          # Màn hình Đăng nhập/Đăng ký
│   └── theme/         # Cấu hình Color, Type, Shape (Material 3)
└── MainActivity.kt    # Entry point & Navigation
```

---

## 🚀 Hướng dẫn cài đặt (Setup Guide)

### 1. Phía Server (Backend)
```bash
# Di chuyển vào thư mục backend
cd Task_Manager_backend

# Cài đặt dependencies
npm install

# Tạo file .env và cấu hình MONGODB_URI, JWT_SECRET
# Chạy server
npm start
```

### 2. Phía Ứng dụng (Android)
- Mở project bằng **Android Studio (Ladybug trở lên)**.
- Thay đổi `BASE_URL` trong logic Network thành IP cục bộ của bạn (Ví dụ: `http://192.168.1.5:3000/api/`).
- Build và chạy trên Emulator hoặc thiết bị thật.

---

## 🔮 Định hướng phát triển (Roadmap)
- [ ] **Offline Mode:** Tích hợp Jetpack Room để hỗ trợ làm việc không cần mạng.
- [ ] **Push Notifications:** Thông báo nhắc nhở khi công việc sắp đến hạn (DueDate).
- [ ] **Unit Testing:** Triển khai bộ Test Case hoàn chỉnh cho ViewModel và Repository.
- [ ] **Dependency Injection:** Áp dụng Hilt để quản lý Dependency chuyên nghiệp hơn.

---
