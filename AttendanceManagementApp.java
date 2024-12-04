import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class Student {
    String name;
    ArrayList<AttendanceRecord> attendance;

    Student(String name) {
        this.name = name;
        this.attendance = new ArrayList<>();
    }

    void markAttendance(Date date, boolean present) {
        attendance.add(new AttendanceRecord(date, present));
    }

    double getAttendancePercentage() {
        long totalClasses = attendance.size();
        long attendedClasses = attendance.stream().filter(record -> record.present).count();
        return (attendedClasses / (double) totalClasses) * 100;
    }

    String getAttendanceReport() {
        StringBuilder report = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        for (AttendanceRecord record : attendance) {
            report.append("Date: ").append(sdf.format(record.date)).append(", Present: ").append(record.present).append("\n");
        }
        report.append("Attendance Percentage: ").append(String.format("%.2f", getAttendancePercentage())).append("%\n");
        return report.toString();
    }
}

class AttendanceRecord {
    Date date;
    boolean present;

    AttendanceRecord(Date date, boolean present) {
        this.date = date;
        this.present = present;
    }
}

class AttendanceManagementSystem {
    ArrayList<Student> students;
    int attendanceThreshold = 75;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    AttendanceManagementSystem() {
        students = new ArrayList<>();
    }

    void addStudent(String name) {
        students.add(new Student(name));
    }

    void markAttendance(String name, String dateString, boolean present) throws ParseException {
        Date date = dateFormat.parse(dateString);
        for (Student student : students) {
            if (student.name.equalsIgnoreCase(name)) {
                student.markAttendance(date, present);
                return;
            }
        }
        System.out.println("Student not found.");
    }

    void markAttendanceForAll(String dateString) throws ParseException {
        Date date = dateFormat.parse(dateString);
        StringBuilder reports = new StringBuilder();
        for (Student student : students) {
            int present = JOptionPane.showConfirmDialog(null, "Is " + student.name + " present?", "Mark Attendance for All", JOptionPane.YES_NO_OPTION);
            student.markAttendance(date, present == JOptionPane.YES_OPTION);
            reports.append("Attendance Report for ").append(student.name).append(":\n")
                    .append(student.getAttendanceReport()).append("\n");
        }
        JOptionPane.showMessageDialog(null, reports.toString());
    }

    void viewAttendance(String name) {
        for (Student student : students) {
            if (student.name.equalsIgnoreCase(name)) {
                double percentage = student.getAttendancePercentage();
                String report = "Student Name: " + student.name + "\nAttendance: " + student.attendance.size() + " (" + String.format("%.2f", percentage) + "%)\n";
                JOptionPane.showMessageDialog(null, report, "View Attendance", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        JOptionPane.showMessageDialog(null, "Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    void viewAllAttendance() {
        StringBuilder reports = new StringBuilder();
        for (Student student : students) {
            double percentage = student.getAttendancePercentage();
            reports.append("Student Name: ").append(student.name).append("\nAttendance: ").append(student.attendance.size()).append(" (").append(String.format("%.2f", percentage)).append("%)\n");
        }
        JOptionPane.showMessageDialog(null, reports.toString(), "View All Attendance", JOptionPane.INFORMATION_MESSAGE);
    }

    void sendLowAttendanceNotifications() {
        StringBuilder notifications = new StringBuilder();
        for (Student student : students) {
            double percentage = student.getAttendancePercentage();
            if (percentage < attendanceThreshold) {
                notifications.append("Notification: ").append(student.name).append(" has low attendance (").append(String.format("%.2f", percentage)).append("%)\n");
            }
        }
        JOptionPane.showMessageDialog(null, notifications.toString(), "Low Attendance Notifications", JOptionPane.WARNING_MESSAGE);
    }
}

public class AttendanceManagementApp {
    static AttendanceManagementSystem ams = new AttendanceManagementSystem();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Attendance Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new GridLayout(5, 1));

        JButton addStudentButton = new JButton("Add New Student");
        JButton markAttendanceForAllButton = new JButton("Mark Attendance for All");
        JButton viewAttendanceButton = new JButton("View Attendance");
        JButton viewAllAttendanceButton = new JButton("View All Attendance");
        JButton sendNotificationsButton = new JButton("Send Notifications");

        addStudentButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Enter student name:");
            ams.addStudent(name);
            JOptionPane.showMessageDialog(frame, "Student added successfully!");
        });

        markAttendanceForAllButton.addActionListener(e -> {
            String date = JOptionPane.showInputDialog("Enter date (dd-MM-yyyy):");
            try {
                ams.markAttendanceForAll(date);
                JOptionPane.showMessageDialog(frame, "Attendance marked for all students!");
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid date format.");
            }
        });

        viewAttendanceButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Enter student name:");
            ams.viewAttendance(name);
        });

        viewAllAttendanceButton.addActionListener(e -> ams.viewAllAttendance());

        sendNotificationsButton.addActionListener(e -> ams.sendLowAttendanceNotifications());

        frame.add(addStudentButton);
        frame.add(markAttendanceForAllButton);
        frame.add(viewAttendanceButton);
        frame.add(viewAllAttendanceButton);
        frame.add(sendNotificationsButton);

        frame.setVisible(true);
    }
}
