package com.epam.rd.autocode.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.epam.rd.autocode.ConnectionSource;

public class DaoFactory {
    // Now the factory does not store the connection but retrieves it when needed.

    public EmployeeDao employeeDAO() throws SQLException {
        Connection connection = ConnectionSource.instance().createConnection();
        return new EmployeeDaoImpl(connection);
    }

    public DepartmentDao departmentDAO() throws SQLException {
        Connection connection = ConnectionSource.instance().createConnection();
        return new DepartmentDaoImpl(connection);
    }
}
