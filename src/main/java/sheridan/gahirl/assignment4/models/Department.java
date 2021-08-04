package sheridan.gahirl.assignment4.models;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
	
	private Integer DEPARTMENT_ID;
	private String DEPARTMENT_NAME;
	private Integer MANAGER_ID;
	private Integer LOCATION_ID;

}
