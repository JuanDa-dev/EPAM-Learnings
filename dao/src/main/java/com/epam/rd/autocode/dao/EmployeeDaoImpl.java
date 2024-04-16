package com.epam.rd.autocode.dao;

import com.epam.rd.autocode.domain.Department;
import com.epam.rd.autocode.domain.Employee;
import com.epam.rd.autocode.domain.FullName;
import com.epam.rd.autocode.domain.Position;

import java.math.BigInteger;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeDaoImpl implements EmployeeDao {
    private final Connection connection;

    public EmployeeDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Employee> getById(BigInteger id) {
        String query = "SELECT * FROM EMPLOYEE WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id.intValue());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToEmployee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Employee> getAll() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM EMPLOYEE";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                employees.add(mapRowToEmployee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    @Override
    public List<Employee> getByDepartment(Department department) {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM EMPLOYEE WHERE DEPARTMENT = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, department.getId().intValue());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                employees.add(mapRowToEmployee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    @Override
    public List<Employee> getByManager(Employee manager) {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM EMPLOYEE WHERE MANAGER = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, manager.getId().intValue());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                employees.add(mapRowToEmployee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    @Override
    public Employee save(Employee employee) {
        // Implement this method based on whether the employee ID is null or not
        // Assume code to insert or update
        return employee;  // Placeholder
    }

    @Override
    public void delete(Employee employee) {
        String query = "DELETE FROM EMPLOYEE WHERE ID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employee.getId().intValue());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Employee mapRowToEmployee(ResultSet rs) throws SQLException {
        return new Employee(
            BigInteger.valueOf(rs.getInt("ID")),
            new FullName(
                rs.getString("FIRSTNAME"),
                rs.getString("LASTNAME"),
                rs.getString("MIDDLENAME")),
            Position.valueOf(rs.getString("POSITION")),
            rs.getDate("HIREDATE").toLocalDate(),
            rs.getBigDecimal("SALARY"),
            rs.getObject("MANAGER") != null ? BigInteger.valueOf(rs.getLong("MANAGER")) : null,
            BigInteger.valueOf(rs.getLong("DEPARTMENT"))
        );
    }
}

