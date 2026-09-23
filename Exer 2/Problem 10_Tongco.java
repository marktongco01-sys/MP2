import java.util.Scanner;

// ==========================================
// STUDENT CLASS
// ==========================================
class Student {
    private String studentId;
    private String fullName;

    // Constructor
    public Student(String studentId, String fullName) {
        this.studentId = studentId;
        this.fullName = fullName;
    }

    // Getters
    public String getStudentId() {
        return studentId;
    }

    public String getFullName() {
        return fullName;
    }
}


// ==========================================
// COURSE CLASS
// ==========================================
class Course {
    private String courseCode;
    private String title;
    private int capacity;

    // Constructor
    public Course(String courseCode, String title, int capacity) {
        this.courseCode = courseCode;
        this.title = title;
        this.capacity = capacity;
    }

    // Getters
    public String getCourseCode() {
        return courseCode;
    }

    public String getTitle() {
        return title;
    }

    public int getCapacity() {
        return capacity;
    }
}


// ==========================================
// ENROLLMENT CLASS
// ==========================================
class Enrollment {
    private Student student;
    private Course course;
    private double grade;

    // Static counter for total enrollments
    private static int enrollmentCount = 0;

    // Constructor
    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.grade = -1;

        enrollmentCount++;
    }

    // Getters
    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public double getGrade() {
        return grade;
    }

    // Controlled setter for grade
    public boolean setGrade(double grade) {

        if (grade < 0 || grade > 100) {
            return false;
        }

        this.grade = grade;
        return true;
    }

    // Get enrollment status
    public String getStatus() {

        if (grade == -1) {
            return "NOT YET GRADED";
        }
        else if (grade >= 75) {
            return "PASSED";
        }
        else {
            return "FAILED";
        }
    }

    // Static getter
    public static int getEnrollmentCount() {
        return enrollmentCount;
    }
}


// ==========================================
// UNIVERSITY SYSTEM CLASS
// ==========================================
class UniversitySystem {

    private Student[] students;
    private Course[] courses;
    private Enrollment[] enrollments;

    private int studentCount;
    private int courseCount;

    // Constructor
    public UniversitySystem(int maxStudents,
                            int maxCourses,
                            int maxEnrollments) {

        students = new Student[maxStudents];
        courses = new Course[maxCourses];
        enrollments = new Enrollment[maxEnrollments];

        studentCount = 0;
        courseCount = 0;
    }


    // ==========================================
    // ADD STUDENT
    // ==========================================
    public void addStudent(Student student) {

        if (studentCount >= students.length) {
            System.out.println("Cannot add student: storage is full.");
            return;
        }

        students[studentCount] = student;
        studentCount++;

        System.out.println("Student added successfully.");
    }


    // ==========================================
    // ADD COURSE
    // ==========================================
    public void addCourse(Course course) {

        if (courseCount >= courses.length) {
            System.out.println("Cannot add course: storage is full.");
            return;
        }

        courses[courseCount] = course;
        courseCount++;

        System.out.println("Course added successfully.");
    }


    // ==========================================
    // FIND STUDENT
    // ==========================================
    private Student findStudent(String id) {

        for (int i = 0; i < studentCount; i++) {

            if (students[i].getStudentId().equalsIgnoreCase(id)) {
                return students[i];
            }
        }

        return null;
    }


    // ==========================================
    // FIND COURSE
    // ==========================================
    private Course findCourse(String code) {

        for (int i = 0; i < courseCount; i++) {

            if (courses[i].getCourseCode().equalsIgnoreCase(code)) {
                return courses[i];
            }
        }

        return null;
    }


    // ==========================================
    // COUNT STUDENTS IN COURSE
    // ==========================================
    private int countStudentsInCourse(Course course) {

        int count = 0;

        for (int i = 0; i < Enrollment.getEnrollmentCount(); i++) {

            if (enrollments[i] != null
                    && enrollments[i].getCourse() == course) {

                count++;
            }
        }

        return count;
    }


    // ==========================================
    // CHECK DUPLICATE ENROLLMENT
    // ==========================================
    private boolean alreadyEnrolled(Student student,
                                    Course course) {

        for (int i = 0; i < Enrollment.getEnrollmentCount(); i++) {

            if (enrollments[i] != null
                    && enrollments[i].getStudent() == student
                    && enrollments[i].getCourse() == course) {

                return true;
            }
        }

        return false;
    }


    // ==========================================
    // ENROLL STUDENT
    // ==========================================
    public void enrollStudent(String studentId,
                              String courseCode) {

        Student student = findStudent(studentId);
        Course course = findCourse(courseCode);

        // Check if student exists
        if (student == null) {
            System.out.println("Enrollment rejected: student not found.");
            return;
        }

        // Check if course exists
        if (course == null) {
            System.out.println("Enrollment rejected: course not found.");
            return;
        }

        // Check duplicate
        if (alreadyEnrolled(student, course)) {
            System.out.println(
                "Enrollment rejected: student is already enrolled."
            );
            return;
        }

        // Check capacity
        if (countStudentsInCourse(course) >= course.getCapacity()) {
            System.out.println(
                "Enrollment rejected: course capacity is full."
            );
            return;
        }

        // Check enrollment array
        if (Enrollment.getEnrollmentCount() >= enrollments.length) {
            System.out.println(
                "Enrollment rejected: enrollment storage is full."
            );
            return;
        }

        // Create enrollment
        enrollments[Enrollment.getEnrollmentCount()]
            = new Enrollment(student, course);

        System.out.println(
            "Enrollment successful: "
            + student.getFullName()
            + " -> "
            + course.getCourseCode()
        );
    }


    // ==========================================
    // ASSIGN / UPDATE GRADE
    // ==========================================
    public void assignGrade(String studentId,
                            String courseCode,
                            double grade) {

        // Validate grade first
        if (grade < 0 || grade > 100) {
            System.out.println(
                "Grade rejected: grade must be from 0 to 100."
            );
            return;
        }

        Student student = findStudent(studentId);
        Course course = findCourse(courseCode);

        if (student == null) {
            System.out.println("Grade rejected: student not found.");
            return;
        }

        if (course == null) {
            System.out.println("Grade rejected: course not found.");
            return;
        }

        // Search for enrollment
        for (int i = 0; i < Enrollment.getEnrollmentCount(); i++) {

            if (enrollments[i].getStudent() == student
                    && enrollments[i].getCourse() == course) {

                enrollments[i].setGrade(grade);

                System.out.printf(
                    "Grade updated: %.2f - %s%n",
                    grade,
                    enrollments[i].getStatus()
                );

                return;
            }
        }

        System.out.println(
            "Grade rejected: student is not enrolled in this course."
        );
    }


    // ==========================================
    // STUDENT REPORT
    // ==========================================
    public void studentReport(String studentId) {

        Student student = findStudent(studentId);

        if (student == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println();
        System.out.println("===== STUDENT REPORT =====");
        System.out.println(
            "Student ID: " + student.getStudentId()
        );
        System.out.println(
            "Name: " + student.getFullName()
        );

        double totalGrades = 0;
        int gradedCount = 0;
        boolean hasEnrollment = false;

        System.out.println();
        System.out.printf(
            "%-10s %-25s %-10s %-20s%n",
            "Code",
            "Course",
            "Grade",
            "Status"
        );

        System.out.println(
            "------------------------------------------------------------"
        );

        // Search all enrollments
        for (int i = 0; i < Enrollment.getEnrollmentCount(); i++) {

            Enrollment enrollment = enrollments[i];

            if (enrollment.getStudent() == student) {

                hasEnrollment = true;

                String gradeText;

                if (enrollment.getGrade() == -1) {
                    gradeText = "N/A";
                }
                else {
                    gradeText = String.format(
                        "%.2f",
                        enrollment.getGrade()
                    );

                    totalGrades += enrollment.getGrade();
                    gradedCount++;
                }

                System.out.printf(
                    "%-10s %-25s %-10s %-20s%n",
                    enrollment.getCourse().getCourseCode(),
                    enrollment.getCourse().getTitle(),
                    gradeText,
                    enrollment.getStatus()
                );
            }
        }

        if (!hasEnrollment) {
            System.out.println("No enrolled courses.");
        }

        // Average of graded courses only
        if (gradedCount > 0) {

            double average = totalGrades / gradedCount;

            System.out.printf(
                "%nAverage of graded courses: %.2f%n",
                average
            );

        }
        else {
            System.out.println(
                "\nAverage of graded courses: N/A"
            );
        }
    }


    // ==========================================
    // COURSE ROSTER
    // ==========================================
    public void courseRoster(String courseCode) {

        Course course = findCourse(courseCode);

        if (course == null) {
            System.out.println("Course not found.");
            return;
        }

        System.out.println();
        System.out.println("===== COURSE ROSTER =====");

        System.out.println(
            "Course: "
            + course.getCourseCode()
            + " - "
            + course.getTitle()
        );

        System.out.println(
            "Capacity: " + course.getCapacity()
        );

        System.out.println();

        boolean hasStudents = false;

        System.out.printf(
            "%-12s %-25s %-10s %-20s%n",
            "Student ID",
            "Name",
            "Grade",
            "Status"
        );

        System.out.println(
            "------------------------------------------------------------"
        );

        // Search all enrollments
        for (int i = 0; i < Enrollment.getEnrollmentCount(); i++) {

            Enrollment enrollment = enrollments[i];

            if (enrollment.getCourse() == course) {

                hasStudents = true;

                String gradeText;

                if (enrollment.getGrade() == -1) {
                    gradeText = "N/A";
                }
                else {
                    gradeText = String.format(
                        "%.2f",
                        enrollment.getGrade()
                    );
                }

                System.out.printf(
                    "%-12s %-25s %-10s %-20s%n",
                    enrollment.getStudent().getStudentId(),
                    enrollment.getStudent().getFullName(),
                    gradeText,
                    enrollment.getStatus()
                );
            }
        }

        if (!hasStudents) {
            System.out.println("No students enrolled.");
        }
    }


    // ==========================================
    // SYSTEM SUMMARY
    // ==========================================
    public void systemSummary() {

        int gradedEnrollments = 0;

        // Count graded enrollments
        for (int i = 0; i < Enrollment.getEnrollmentCount(); i++) {

            if (enrollments[i].getGrade() != -1) {
                gradedEnrollments++;
            }
        }

        System.out.println();
        System.out.println("===== SYSTEM SUMMARY =====");

        System.out.println(
            "Total students: " + studentCount
        );

        System.out.println(
            "Total courses: " + courseCount
        );

        System.out.println(
            "Total enrollments: "
            + Enrollment.getEnrollmentCount()
        );

        System.out.println(
            "Graded enrollments: "
            + gradedEnrollments
        );
    }
}


// ==========================================
// MAIN CLASS
// ==========================================
public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        // ==========================================
        // Initial Records
        // ==========================================

        System.out.print("Enter number of students: ");
        int numberOfStudents = input.nextInt();
        input.nextLine();

        System.out.print("Enter number of courses: ");
        int numberOfCourses = input.nextInt();
        input.nextLine();

        UniversitySystem system =
            new UniversitySystem(
                numberOfStudents,
                numberOfCourses,
                100
            );

        // ==========================================
        // Input Students
        // ==========================================

        System.out.println("\n===== ENTER STUDENTS =====");

        for (int i = 0; i < numberOfStudents; i++) {

            System.out.print("Student ID: ");
            String id = input.nextLine();

            System.out.print("Full name: ");
            String name = input.nextLine();

            Student student = new Student(id, name);

            system.addStudent(student);
        }

        // ==========================================
        // Input Courses
        // ==========================================

        System.out.println("\n===== ENTER COURSES =====");

        for (int i = 0; i < numberOfCourses; i++) {

            System.out.print("Course code: ");
            String code = input.nextLine();

            System.out.print("Course title: ");
            String title = input.nextLine();

            System.out.print("Capacity: ");
            int capacity = input.nextInt();
            input.nextLine();

            Course course =
                new Course(code, title, capacity);

            system.addCourse(course);
        }

        // ==========================================
        // MENU
        // ==========================================

        int choice;

        do {

            System.out.println();
            System.out.println("===== UNIVERSITY ENROLLMENT SYSTEM =====");
            System.out.println("1 - Enroll Student");
            System.out.println("2 - Assign/Update Grade");
            System.out.println("3 - Student Report");
            System.out.println("4 - Course Roster");
            System.out.println("5 - System Summary");
            System.out.println("0 - Exit");

            System.out.print("Enter choice: ");
            choice = input.nextInt();
            input.nextLine();

            // ======================================
            // OPTION 1
            // ======================================
            if (choice == 1) {

                System.out.print("Student ID: ");
                String studentId = input.nextLine();

                System.out.print("Course Code: ");
                String courseCode = input.nextLine();

                system.enrollStudent(
                    studentId,
                    courseCode
                );
            }

            // ======================================
            // OPTION 2
            // ======================================
            else if (choice == 2) {

                System.out.print("Student ID: ");
                String studentId = input.nextLine();

                System.out.print("Course Code: ");
                String courseCode = input.nextLine();

                System.out.print("Grade (0-100): ");
                double grade = input.nextDouble();
                input.nextLine();

                system.assignGrade(
                    studentId,
                    courseCode,
                    grade
                );
            }

            // ======================================
            // OPTION 3
            // ======================================
            else if (choice == 3) {

                System.out.print("Student ID: ");
                String studentId = input.nextLine();

                system.studentReport(studentId);
            }

            // ======================================
            // OPTION 4
            // ======================================
            else if (choice == 4) {

                System.out.print("Course Code: ");
                String courseCode = input.nextLine();

                system.courseRoster(courseCode);
            }

            // ======================================
            // OPTION 5
            // ======================================
            else if (choice == 5) {

                system.systemSummary();
            }

            // ======================================
            // OPTION 0
            // ======================================
            else if (choice == 0) {

                System.out.println(
                    "Exiting system..."
                );
            }

            // ======================================
            // INVALID
            // ======================================
            else {

                System.out.println(
                    "Invalid choice."
                );
            }

        } while (choice != 0);

        input.close();
    }
}
