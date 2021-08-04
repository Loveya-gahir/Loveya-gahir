package sheridan.gahirl.assignment4.database;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import sheridan.gahirl.assignment4.models.*;

@Repository
public class DatabaseAccess {

	@Autowired
	private NamedParameterJdbcTemplate jdbc;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;



	public User findUserAccount(String email) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "SELECT * FROM SEC_USER where email = :email";
		parameters.addValue("email", email);

		ArrayList<User> users = (ArrayList<User>) jdbc.query(query, parameters,
				new BeanPropertyRowMapper<User>(User.class));
		
		System.out.println(users.get(0));
		if (users.size() > 0)
			return users.get(0);
		else
			return null;
	}

	public List<String> getRolesById(long userId) {
		ArrayList<String> roles = new ArrayList<String>();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		String query = "SELECT user_role.userId, sec_role.roleName " + " FROM user_role, sec_role "
				+ " WHERE user_role.roleId = sec_role.roleId" + " AND userId = :userId";
		parameters.addValue("userId", userId);

		List<Map<String, Object>> rows = jdbc.queryForList(query, parameters);

		for (Map<String, Object> row : rows) {
			roles.add((String) row.get("roleName"));
		}
		return roles;
	}

	public List<Employee> getAllEmployees() {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM EMPLOYEES";
		return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Employee>(Employee.class));
	}

	public Employee getEmployeeById(int id) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("id", id);
		String query = "SELECT * FROM EMPLOYEES where EMPLOYEE_ID = :id";
		return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Employee>(Employee.class)).get(0);
	}

	public List<Department> getAllDepartments() {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		String query = "SELECT * FROM DEPARTMENTS";
		return jdbc.query(query, namedParameters, new BeanPropertyRowMapper<Department>(Department.class));
	}

	public boolean insertEmployee(int employeeId, String firstName, String lastName, String email, String phone,
			String hireDate, String jobId, double salary, double commission, int managerId, int departmentId) {

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("EMPLOYEE_ID", employeeId);
		namedParameters.addValue("FIRST_NAME", firstName);
		namedParameters.addValue("LAST_NAME", lastName);
		namedParameters.addValue("EMAIL", email);
		namedParameters.addValue("PHONE_NUMBER", phone);
		namedParameters.addValue("HIRE_DATE", hireDate);
		namedParameters.addValue("JOB_ID", jobId);
		namedParameters.addValue("SALARY", salary);
		namedParameters.addValue("COMMISSION_PCT", commission);
		namedParameters.addValue("MANAGER_ID", managerId);
		namedParameters.addValue("DEPARTMENT_ID", departmentId);

		String query = "INSERT INTO EMPLOYEES(EMPLOYEE_ID,FIRST_NAME,LAST_NAME,EMAIL,PHONE_NUMBER,HIRE_DATE,JOB_ID,SALARY,COMMISSION_PCT,MANAGER_ID,DEPARTMENT_ID) VALUES (:EMPLOYEE_ID,:FIRST_NAME,:LAST_NAME,:EMAIL,:PHONE_NUMBER,:HIRE_DATE,:JOB_ID,:SALARY,:COMMISSION_PCT,:MANAGER_ID,:DEPARTMENT_ID)";
		int result = jdbc.update(query, namedParameters);

		return result > 0;
	}

	public boolean updateEmployee(int employeeId, int managerId) {

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("EMPLOYEE_ID", employeeId);

		namedParameters.addValue("MANAGER_ID", managerId);

		String query = "UPDATE EMPLOYEES SET MANAGER_ID  = :MANAGER_ID WHERE EMPLOYEE_ID = :EMPLOYEE_ID";
		int result = jdbc.update(query, namedParameters);

		return result > 0;
	}

	public boolean deleteEmployee(int employeeId) {

		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("EMPLOYEE_ID", employeeId);

		String query = "DELETE FROM EMPLOYEES  WHERE EMPLOYEE_ID = :EMPLOYEE_ID";
		int result = jdbc.update(query, namedParameters);

		return result > 0;
	}

}
