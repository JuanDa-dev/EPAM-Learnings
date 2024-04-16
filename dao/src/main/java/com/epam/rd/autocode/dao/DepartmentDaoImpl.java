package com.epam.rd.autocode.dao;

import com.epam.rd.autocode.domain.Department;

import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DepartmentDaoImpl implements DepartmentDao {
    private final Connection connection;

    public DepartmentDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Department> getById(BigInteger id) {
        String query = "SELECT * FROM DEPARTMENT WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id.intValue());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(new Department(
                        BigInteger.valueOf(rs.getInt("ID")),
                        rs.getString("NAME"),
                        rs.getString("LOCATION")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Department> getAll() {
        List<Department> departments = new ArrayList<>();
        String query = "SELECT * FROM DEPARTMENT";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                departments.add(new Department(
                        BigInteger.valueOf(rs.getInt("ID")),
                        rs.getString("NAME"),
                        rs.getString("LOCATION")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departments;
    }

    @Override
    public Department save(Department department) {
        if (department.getId() == null) {
            // Insert new department
            String sql = "INSERT INTO DEPARTMENT (NAME, LOCATION) VALUES (?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, department.getName());
                stmt.setString(2, department.getLocation());
                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating department failed, no rows affected.");
                }
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        department.setId(BigInteger.valueOf(generatedKeys.getLong(1)));
                    } else {
                        throw new SQLException("Creating department failed, no ID obtained.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Update existing department
            String sql = "UPDATE DEPARTMENT SET NAME = ?, LOCATION = ? WHERE ID = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, department.getName());
                stmt.setString(2, department.getLocation());
                stmt.setInt(3, department.getId().intValue());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return department;
    }

    @Override
    public void delete(Department department) {
        String query = "DELETE FROM DEPARTMENT WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, department.getId().intValue());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
