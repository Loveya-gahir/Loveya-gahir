package sheridan.gahirl.assignment4.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sheridan.gahirl.assignment4.database.DatabaseAccess;
import sheridan.gahirl.assignment4.models.Employee;
import sheridan.gahirl.assignment4.services.UserDetailsServiceImpl;



@Controller
public class HomeController {
	@Autowired
	private UserDetailsServiceImpl userService;
	
	@Autowired
	private DatabaseAccess da;
	
	@GetMapping("/login")
	public String getLogin() {
		return "login";
	}

	@GetMapping("/")
	public String getIndex() {
		return "index";
	}

	@GetMapping("/access-denied")
	public String accessDenied() {
		return "/error/access-denied.html";
	}
	
	@GetMapping("/view")
	public String getView(Model model) {
		model.addAttribute("employees", da.getAllEmployees());
		model.addAttribute("departments", da.getAllDepartments());
		return "view";
	}
	@GetMapping("/add")
	public String getInsert(Model model) {
		model.addAttribute("employees", da.getAllEmployees());
		model.addAttribute("departments", da.getAllDepartments());
		return "insert";
	}
	@GetMapping("/update-employee/{id}")
	public String getUpdateEmployee(@PathVariable Integer id ,Model model) {
		model.addAttribute("employees", da.getAllEmployees());
		model.addAttribute("departments", da.getAllDepartments());
		model.addAttribute("employee" , da.getEmployeeById(id));
		return "update";
	}
	
	@PostMapping("/save")
	public String saveEmployee(@RequestParam Integer employeeId, 
			@RequestParam String fname, @RequestParam String lname,
			@RequestParam String email,@RequestParam String phone, 
			@RequestParam String hireDate,@RequestParam String jobId,
			@RequestParam Double salary,@RequestParam Double commission,
			@RequestParam Integer managerId,@RequestParam Integer departmentId
			,Model model) {
		
		boolean result = da.insertEmployee( employeeId, 
				fname, lname, email, phone,
				hireDate, jobId, salary, 
				commission,managerId, departmentId); 

		
		model.addAttribute("message", result ? "New Employee inserted" : "unable to insert new employee");
		
		if(result)
			return "redirect:view";
		else 
			return "redirect:insert";
	}
	
	@PostMapping("/update")
	public String updateEmployee(@RequestParam Integer employeeId, 
			@RequestParam Integer managerId,Model model) {
		
		boolean result = da.updateEmployee( employeeId, managerId); 

		model.addAttribute("message", result ? "Employee Updated" : "unable to update employee");
		
		if(result)
			return "redirect:view";
		else 
			return "redirect:update-employee/"+employeeId;
	}
	
	
	@GetMapping("/admin/delete/{id}")
	public String getDeleteEmployee(@PathVariable Integer id ,Model model) {
		model.addAttribute("employee" , da.getEmployeeById(id));
		return "delete";
	}
	
	@PostMapping("/delete")
	public String updateEmployee(@RequestParam Integer employeeId,Model model) {
		
		boolean result = da.deleteEmployee( employeeId); 

		model.addAttribute("message", result ? "Employee Deleted" : "unable to delete employee");
		
		if(result)
			return "redirect:view";
		else 
			return "redirect:/admin/delete/"+employeeId;
	}
}
