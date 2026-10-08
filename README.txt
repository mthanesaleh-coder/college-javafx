================================================================================
                    🎓 COLLEGE MANAGEMENT SYSTEM
                         نظام إدارة الكلية
                    دليل المشروع الكامل
================================================================================

================================================================================
📌 1. نظرة عامة على المشروع
================================================================================

اسم المشروع: College Management System
اللغة: Java 17
نوع التطبيق: Desktop Application
الواجهة: JavaFX
قاعدة البيانات: Microsoft SQL Server
إدارة المشروع: Maven

الهدف:
نظام إدارة كلية يسمح بـ:
- تسجيل دخول آمن (3 أدوار)
- إدارة الطلاب (عرض، إضافة، تعديل، حذف)
- إدارة الدكاترة (عرض، إضافة، تعديل، حذف)
- إدارة المواد (عرض، إضافة، تعديل، حذف)
- إحصائيات فورية من قاعدة البيانات
- دعم كامل للغة العربية


================================================================================
📁 2. هيكل المشروع
================================================================================

college-javafx/
│
├── pom.xml                          ← إعدادات Maven والمكتبات
├── README.txt                       ← هذا الملف
│
└── src/
    └── main/
        ├── java/
        │   └── com/college/
        │       │
        │       ├── Main.java                 ← نقطة البداية
        │       │
        │       ├── database/                 ← طبقة الاتصال بقاعدة البيانات
        │       │   ├── DatabaseConnection.java   ← الاتصال بـ SQL Server
        │       │   ├── UserDAO.java              ← عمليات المستخدمين
        │       │   ├── StudentDAO.java           ← عمليات الطلاب
        │       │   ├── DoctorDAO.java            ← عمليات الدكاترة
        │       │   └── CourseDAO.java            ← عمليات المواد
        │       │
        │       ├── utils/                    ← أدوات مساعدة
        │       │   ├── AlertHelper.java          ← نوافذ التنبيه
        │       │   └── Session.java              ← جلسة المستخدم الحالي
        │       │
        │       └── views/                    ← الواجهات (5 واجهات)
        │           ├── LoginView.java            ← شاشة تسجيل الدخول
        │           ├── DashboardView.java        ← الصفحة الرئيسية
        │           ├── StudentsView.java         ← إدارة الطلاب
        │           ├── DoctorsView.java          ← إدارة الدكاترة
        │           └── CoursesView.java          ← إدارة المواد
        │
        └── resources/
            ├── css/                          ← ملفات التصميم
            ├── fxml/                         ← (غير مستخدم)
            └── images/                       ← الصور


================================================================================
🔐 3. الحسابات الافتراضية
================================================================================

+---------------------------------------------+
|  Role    : Admin (مدير)                     |
|  Username: admin                            |
|  Password: admin123                         |
|  الصلاحيات: كل شيء                          |
+---------------------------------------------+

+---------------------------------------------+
|  Role    : Doctor (أستاذ)                   |
|  Username: doctor                           |
|  Password: doctor123                        |
|  الصلاحيات: عرض + إضافة نفسه كدكتور         |
+---------------------------------------------+

+---------------------------------------------+
|  Role    : Student (طالب)                   |
|  Username: student                          |
|  Password: student123                       |
|  الصلاحيات: عرض فقط                         |
+---------------------------------------------+


================================================================================
🚀 4. طريقة التشغيل
================================================================================

المتطلبات:
- JDK 17 أو أحدث
- Maven 3.8+
- SQL Server + SSMS
- قاعدة بيانات CollegeDB

خطوات التشغيل:

1. افتح Terminal/PowerShell

2. انتقل لمجلد المشروع:
   cd C:\Users\hossn\Desktop\college-javafx

3. شغّل المشروع:
   mvn clean javafx:run


================================================================================
📊 5. شرح الصفحات الخمس
================================================================================

================================================================================
📄 5.1 صفحة LoginView - تسجيل الدخول
================================================================================

الملف: src/main/java/com/college/views/LoginView.java

الهدف:
الواجهة الأولى التي يراها المستخدم عند تشغيل البرنامج.

المحتوى:
- خلفية بتدرج بنفسجي/أزرق
- شعار "College"
- حقل اسم المستخدم (Username)
- حقل كلمة المرور (Password)
- زر "Sign In"
- تلميح بالحساب الافتراضي

كيف تعمل:
1. المستخدم يدخل اسم المستخدم وكلمة المرور
2. عند الضغط على Sign In:
   - يتم استدعاء UserDAO.authenticate(username, password)
   - UserDAO يبحث في جدول users في SQL Server
   - إذا وُجد:
     * Session.setUser(username, role)
     * الانتقال إلى DashboardView
   - إذا لم يوجد:
     * عرض رسالة خطأ


================================================================================
📄 5.2 صفحة DashboardView - الصفحة الرئيسية
================================================================================

الملف: src/main/java/com/college/views/DashboardView.java

الهدف:
لوحة تحكم رئيسية تعرض إحصائيات وروابط سريعة.

المحتوى:
- Sidebar (قائمة جانبية):
  * College System (شعار)
  * Dashboard (نشط - أزرق)
  * Students
  * Doctors
  * Courses
  * Logout (أحمر)

- Top Bar:
  * العنوان "Dashboard"
  * Welcome, [username] ([role])

- Welcome Banner:
  * "Welcome back, [username]!"
  * "Here's what's happening in your college today."

- Stats Cards (4 بطاقات):
  * Students: عدد من StudentDAO.getStudentCount()      - لون أزرق
  * Doctors:  عدد من DoctorDAO.getDoctorCount()        - لون أخضر
  * Courses:  عدد من CourseDAO.getCourseCount()        - لون برتقالي
  * Departments: 8 (ثابت)                              - لون بنفسجي

- Quick Actions (4 أزرار):
  * Add Student  → ينتقل لـ StudentsView
  * Add Doctor   → ينتقل لـ DoctorsView
  * Add Course   → ينتقل لـ CoursesView
  * Refresh      → يحدّث الأرقام

الأرقام الحقيقية:
كل الأرقام في البطاقات تأتي من SQL Server مباشرة.


================================================================================
📄 5.3 صفحة StudentsView - إدارة الطلاب
================================================================================

الملف: src/main/java/com/college/views/StudentsView.java

الهدف:
عرض وإدارة الطلاب.

الواجهة:
- Search Box (بحث فوري)
- زر "+ Add Student" (للأدمن فقط)
- جدول:
  * Student No.
  * Name
  * Email
  * Major
  * Level
  * Actions (تعديل/حذف - للأدمن فقط)

الصلاحيات:
- Admin: عرض + بحث + إضافة + تعديل + حذف
- Doctor: عرض + بحث فقط
- Student: عرض + بحث فقط

كيف تعمل:
1. عند فتح الصفحة:
   - StudentDAO.getAllStudents() → قائمة الطلاب من SQL
   - يتم عرضهم في الجدول

2. البحث:
   - StudentDAO.searchStudents(query)

3. الإضافة:
   - StudentDAO.addStudent(new Student(...))

4. التعديل:
   - StudentDAO.updateStudent(updated)

5. الحذف:
   - StudentDAO.deleteStudent(id)


================================================================================
📄 5.4 صفحة DoctorsView - إدارة الدكاترة
================================================================================

الملف: src/main/java/com/college/views/DoctorsView.java

الهدف:
عرض وإدارة الدكاترة.

الواجهة:
- Search Box (بحث)
- زر "+ Add Doctor" (للأدمن والدكتور)
- جدول:
  * Name
  * Email
  * Specialty
  * Actions (تعديل/حذف - للأدمن فقط)

الصلاحيات:
- Admin: عرض + بحث + إضافة + تعديل + حذف
- Doctor: عرض + بحث + إضافة نفسه (الاسم مقفل)
- Student: عرض + بحث فقط

كيف تعمل:
1. عند فتح الصفحة:
   - DoctorDAO.getAllDoctors() → قائمة الدكاترة
   
2. الإضافة:
   - Admin: يكتب الاسم يدوياً
   - Doctor: الاسم يُعبأ تلقائياً بـ username (مقفل)
   - DoctorDAO.addDoctor(new Doctor(...))


================================================================================
📄 5.5 صفحة CoursesView - إدارة المواد
================================================================================

الملف: src/main/java/com/college/views/CoursesView.java

الهدف:
عرض وإدارة المواد الدراسية.

الواجهة:
- Search Box (بحث)
- زر "+ Add Course" (للأدمن فقط)
- جدول:
  * Code
  * Course Name
  * Credit Hours
  * Doctor
  * Actions (تعديل/حذف - للأدمن فقط)

الصلاحيات:
- Admin: عرض + بحث + إضافة + تعديل + حذف
- Doctor: عرض + بحث فقط
- Student: عرض + بحث فقط

كيف تعمل:
1. عند فتح الصفحة:
   - CourseDAO.getAllCourses() → قائمة المواد
   
2. الإضافة:
   - CourseDAO.addCourse(new Course(...))
   
3. البحث:
   - CourseDAO.searchCourses(query)


================================================================================
🗄️ 6. قاعدة البيانات
================================================================================

Database: CollegeDB (SQL Server)
Server: 127.0.0.1 (localhost)
Authentication: SQL Server (sa / Admin123!)

الجداول:

1. users
   - id (INT, PK, Identity)
   - username (NVARCHAR(50), UNIQUE)
   - password (NVARCHAR(100))
   - role (NVARCHAR(20))  ← ADMIN / DOCTOR / STUDENT

2. students
   - id (INT, PK, Identity)
   - student_number (NVARCHAR(20), UNIQUE)
   - full_name (NVARCHAR(100))
   - email (NVARCHAR(100))
   - major (NVARCHAR(100))
   - level (INT)

3. doctors
   - id (INT, PK, Identity)
   - name (NVARCHAR(100))
   - email (NVARCHAR(100))
   - specialty (NVARCHAR(100))

4. courses
   - id (INT, PK, Identity)
   - code (NVARCHAR(20), UNIQUE)
   - name (NVARCHAR(100))
   - credit_hours (INT)
   - doctor_name (NVARCHAR(100))

ملاحظة مهمة:
كل الحقول نصوص استخدام NVARCHAR لدعم اللغة العربية.


================================================================================
💻 7. ملفات Java الأساسية
================================================================================

7.1 Main.java
- نقطة بداية البرنامج
- يفتح LoginView

7.2 DatabaseConnection.java
- String SERVER = "127.0.0.1"
- String DATABASE = "CollegeDB"
- String USER = "sa"
- String PASSWORD = "Admin123!"
- يدعم Unicode:
  * characterEncoding=UTF-8
  * sendStringParametersAsUnicode=true

7.3 UserDAO.java
- authenticate(username, password) → User أو null

7.4 StudentDAO.java
- getAllStudents() → List<Student>
- addStudent(Student) → boolean
- updateStudent(Student) → boolean
- deleteStudent(int) → boolean
- searchStudents(String) → List<Student>
- getStudentCount() → int

7.5 DoctorDAO.java
- getAllDoctors() → List<Doctor>
- addDoctor(Doctor) → boolean
- updateDoctor(Doctor) → boolean
- deleteDoctor(int) → boolean
- searchDoctors(String) → List<Doctor>
- getDoctorCount() → int

7.6 CourseDAO.java
- getAllCourses() → List<Course>
- addCourse(Course) → boolean
- updateCourse(Course) → boolean
- deleteCourse(int) → boolean
- searchCourses(String) → List<Course>
- getCourseCount() → int

7.7 Session.java
- setUser(username, role)
- getUsername()
- getRole()
- isAdmin() / isDoctor() / isStudent()
- clear()

7.8 AlertHelper.java
- showInfo()
- showError()
- showWarning()


================================================================================
🔧 8. المكتبات المستخدمة (pom.xml)
================================================================================

1. JavaFX Controls (21.0.2)
2. JavaFX FXML (21.0.2)
3. Microsoft SQL Server JDBC (12.6.1.jre11)
4. jBCrypt (0.4) - اختياري


================================================================================
🎯 9. سيناريو استخدام كامل
================================================================================

1. المدير يدخل:
   Username: admin
   Password: admin123

2. يضيف طلاب:
   Students → + Add Student
   - Number: 2024001
   - Name: أحمد محمد
   - Email: ahmed@uni.edu
   - Major: علوم الحاسوب
   - Level: 2

3. يضيف دكاترة:
   Doctors → + Add Doctor

4. يضيف مواد:
   Courses → + Add Course

5. يشاهد الإحصائيات:
   Dashboard → الأرقام تتحدث تلقائياً

6. يسجل خروج:
   Logout → يعود لـ Login

7. الدكتور يدخل:
   - يشاهد الطلاب (عرض فقط)
   - يضيف نفسه كدكتور (الاسم مقفل)
   - يشاهد المواد (عرض فقط)

8. الطالب يدخل:
   - يشاهد كل شيء (عرض فقط)


================================================================================
📊 10. الصلاحيات الكاملة
================================================================================

+------------------+----------+----------+----------+
|   الميزة         |  Admin   |  Doctor  | Student  |
+------------------+----------+----------+----------+
| Students - عرض   |   ✅     |   ✅     |   ✅     |
| Students - إضافة |   ✅     |   ❌     |   ❌     |
| Students - حذف   |   ✅     |   ❌     |   ❌     |
+------------------+----------+----------+----------+
| Doctors - عرض    |   ✅     |   ✅     |   ✅     |
| Doctors - إضافة  |   ✅     |  ✅(نفسه)|   ❌     |
| Doctors - حذف    |   ✅     |   ❌     |   ❌     |
+------------------+----------+----------+----------+
| Courses - عرض    |   ✅     |   ✅     |   ✅     |
| Courses - إضافة  |   ✅     |   ❌     |   ❌     |
| Courses - حذف    |   ✅     |   ❌     |   ❌     |
+------------------+----------+----------+----------+
| Dashboard        |   ✅     |   ✅     |   ✅     |
| Logout           |   ✅     |   ✅     |   ✅     |
+------------------+----------+----------+----------+


================================================================================
⚠️ 11. حل المشاكل الشائعة
================================================================================

المشكلة 1: "Cannot connect to SQL Server"
الحل:
- تأكد من تشغيل SQL Server
- تأكد من تفعيل TCP/IP
- تأكد من Port 1433
- تأكد من sa password = "Admin123!"

المشكلة 2: "العربي يظهر ???? في JavaFX"
الحل:
- تأكد من DatabaseConnection فيه:
  * characterEncoding=UTF-8
  * sendStringParametersAsUnicode=true

المشكلة 3: "class not found"
الحل: mvn clean compile

المشكلة 4: "لا تظهر البيانات الجديدة"
الحل:
- اقفل البرنامج وافتحه
- أو اذهب Dashboard ثم عد للصفحة


================================================================================
📝 12. معلومات للتسليم
================================================================================

اسم المشروع: College Management System
اسم الطالب: [اكتب اسمك]
الرقم الجامعي: [اكتب رقمك]
المادة: [اكتب اسم المادة]
الدكتور: [اكتب اسم الدكتور]
التاريخ: [التاريخ]

المميزات:
- 5 واجهات رسومية احترافية
- قاعدة بيانات SQL Server
- 3 أدوار (Admin, Doctor, Student)
- صلاحيات دقيقة لكل دور
- دعم كامل للغة العربية
- عمليات CRUD كاملة
- بحث فوري
- إحصائيات حقيقية


================================================================================
🎓 نهاية الدليل
================================================================================

هذا الدليل يشرح كل جزء في المشروع.
إذا احتجت أي مساعدة، راجع الكود مباشرة.

بالتوفيق! 🎉
================================================================================