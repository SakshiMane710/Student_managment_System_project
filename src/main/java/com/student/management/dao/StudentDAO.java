package com.student.management.dao;

import com.student.management.model.Student;
import com.student.management.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    /**
     * Inserts a new student into the database.
     *
     * @param student The student object containing details to be added.
     * @return true if insertion was successful, false otherwise.
     */
    public boolean addStudent(Student student) {
        String query = "INSERT INTO students (first_name, last_name, email, phone, enrollment_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, student.getFirstName());
            preparedStatement.setString(2, student.getLastName());
            preparedStatement.setString(3, student.getEmail());
            preparedStatement.setString(4, student.getPhone());
            
            // Convert java.time.LocalDate to java.sql.Date
            preparedStatement.setDate(5, Date.valueOf(student.getEnrollmentDate()));

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Database error during student insertion.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all students from the database.
     *
     * @return A list of Student objects.
     */
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM students ORDER BY id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Student student = new Student(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getDate("enrollment_date").toLocalDate()
                );
                students.add(student);
            }

        } catch (SQLException e) {
            System.err.println("Database error during student retrieval.");
            e.printStackTrace();
        }

        return students;
    }

    /**
     * Retrieves a student by their ID.
     *
     * @param id The ID of the student to search for.
     * @return A Student object if found, null otherwise.
     */
    public Student getStudentById(int id) {
        String query = "SELECT * FROM students WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Student(
                            resultSet.getInt("id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("email"),
                            resultSet.getString("phone"),
                            resultSet.getDate("enrollment_date").toLocalDate()
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during student retrieval by ID.");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves a student by their email address.
     *
     * @param email The email of the student to search for.
     * @return A Student object if found, null otherwise.
     */
    public Student getStudentByEmail(String email) {
        String query = "SELECT * FROM students WHERE email = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Student(
                            resultSet.getInt("id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("email"),
                            resultSet.getString("phone"),
                            resultSet.getDate("enrollment_date").toLocalDate()
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during student retrieval by email.");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Updates an existing student in the database.
     *
     * @param student The student object containing updated details.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateStudent(Student student) {
        String query = "UPDATE students SET first_name = ?, last_name = ?, email = ?, phone = ?, enrollment_date = ? WHERE id = ?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, student.getFirstName());
            preparedStatement.setString(2, student.getLastName());
            preparedStatement.setString(3, student.getEmail());
            preparedStatement.setString(4, student.getPhone());
            preparedStatement.setDate(5, Date.valueOf(student.getEnrollmentDate()));
            preparedStatement.setInt(6, student.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Database error during student update.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a student from the database.
     *
     * @param studentId The ID of the student to delete.
     * @return true if the deletion was successful, false otherwise.
     */
    public boolean deleteStudent(int studentId) {
        String query = "DELETE FROM students WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, studentId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Database error during student deletion.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves the total count of students in the database.
     *
     * @return The number of students.
     */
    public int getTotalStudentCount() {
        String query = "SELECT COUNT(*) FROM students";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
             
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Database error during student count retrieval.");
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Searches for students by their first or last name (partial match).
     *
     * @param name The name to search for.
     * @return A list of matching Student objects.
     */
    public List<Student> searchStudentsByName(String name) {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM students WHERE first_name LIKE ? OR last_name LIKE ? ORDER BY id DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            String searchPattern = "%" + name + "%";
            preparedStatement.setString(1, searchPattern);
            preparedStatement.setString(2, searchPattern);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Student student = new Student(
                            resultSet.getInt("id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("email"),
                            resultSet.getString("phone"),
                            resultSet.getDate("enrollment_date").toLocalDate()
                    );
                    students.add(student);
                }
            }

        } catch (SQLException e) {
            System.err.println("Database error during student search by name.");
            e.printStackTrace();
        }

        return students;
    }
}
