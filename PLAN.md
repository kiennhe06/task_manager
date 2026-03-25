# Task Manager Plan (Portfolio)

> Quy uoc cap nhat:
> - Moi khi hoan thanh mot muc, doi trang thai `[ ]` -> `[x]`.
> - Them ngay dong ghi chu vao muc **Nhat ky cap nhat** o cuoi file.
> - Neu phat sinh viec moi, them vao dung nhom chuc nang va de `[ ]`.

## 1) Danh sach chuc nang can co

### 1.1 Backend (Node.js/Express + MongoDB)
- [x] Ket noi MongoDB bang `mongoose`
- [x] Health check `GET /api/health`
- [x] Dang ky `POST /api/auth/register`
- [x] Dang nhap `POST /api/auth/login`
- [x] Middleware xac thuc JWT cho route can dang nhap
- [x] Lay danh sach task `GET /api/tasks`
- [x] Tao task `POST /api/tasks`
- [x] Sua task `PUT /api/tasks/:id`
- [x] Xoa task `DELETE /api/tasks/:id`
- [x] Thong bao loi backend bang tieng Viet
- [x] Chuan hoa response backend theo mau `success/message/data`
- [x] Validate du lieu task day du hon (`status/priority/dueDate`)
- [ ] Test API co ban (auth + task)

### 1.2 Android (Kotlin + Compose)
- [x] Man Dang nhap
- [x] Man Dang ky
- [x] Luu token local sau dang nhap/dang ky
- [x] Noi token dong vao tat ca API request
- [x] Dieu huong theo trang thai token (auth/tasks)
- [x] Hien thi danh sach task
- [x] Them task
- [x] Sua task (dialog)
- [x] Xoa task
- [x] Dang xuat
- [x] Thong bao UI chinh bang tieng Viet
- [x] Parse loi Retrofit de hien message de hieu
- [x] Sua task day du truong `title/description/status/priority/dueDate`
- [x] Xac nhan truoc khi xoa task
- [x] Loading state rieng cho tung hanh dong (them/sua/xoa/tai)
- [x] Dua toan bo text UI vao `strings.xml`
- [ ] Unit test ViewModel co ban

### 1.3 Hoan thien Portfolio/CV
- [x] `README.md` day du (tong quan, kien truc, API, setup)
- [ ] Anh/chup man hinh va video demo
- [ ] Deploy backend (Render/Railway)
- [ ] Cau hinh base URL release cho Android
- [ ] Ban build demo de chia se (APK/AAB)

## 2) Da lam duoc gi den hien tai

### Backend da xong
- Da tao backend API va ket noi MongoDB.
- Da co auth JWT + middleware bao ve route.
- Da co CRUD task day du.
- Da viet thong bao backend bang tieng Viet (khong dau).
- Da chuan hoa response theo format `{ success, message, data }`.
- Da validate du lieu task (status, priority, dueDate).

### Android da xong
- Da co flow auth (dang nhap/dang ky) va luu token.
- Da noi token dong vao API.
- Da co man task: xem, them, sua (day du truong), xoa (co xac nhan), dang xuat.
- Da cap nhat nhieu thong bao UI sang tieng Viet.
- Da parse loi Retrofit hien thi than thien.
- Da co loading state chi tiet cho tung thao tac.
- Da chuan hoa toan bo text vao strings.xml.
- Da tao model ApiResponse<T> de xu ly response wrapper.

## 3) Dang thieu gi (uu tien lam tiep)

### Uu tien cao (P1) — DA XONG
- [x] Parse loi Retrofit de thong bao than thien.
- [x] Nang cap man sua task day du cac truong.
- [x] Them dialog xac nhan xoa task.
- [x] Bo sung loading state chi tiet theo tung thao tac.

### Uu tien trung binh (P2) — DA XONG
- [x] Chuan hoa string vao `strings.xml`.
- [x] Chuan hoa response backend theo mot format thong nhat.
- [x] Don dep architecture (tach state/event ro hon).

### Uu tien cuoi (P3) — DANG LAM
- [x] README portfolio chinh chu.
- [ ] Test co ban (backend + android).
- [ ] Deploy va chot ban demo.

## 4) Nhat ky cap nhat

- 2026-03-24: Khoi tao file plan va liet ke hien trang.
- 2026-03-24: Da cap nhat lai plan theo mau: can co gi / da co gi / thieu gi, kem checklist trang thai.
- 2026-03-25: Hoan thanh P1 (parse loi, dialog xoa, sua day du truong, loading state).
- 2026-03-25: Hoan thanh P2 (strings.xml, chuan hoa response backend, ApiResponse wrapper, validate task).
- 2026-03-25: Hoan thanh README.md day du.
