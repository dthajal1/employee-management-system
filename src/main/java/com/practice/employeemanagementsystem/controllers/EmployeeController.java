package com.practice.employeemanagementsystem.controllers;

import com.practice.employeemanagementsystem.commands.EmployeeCommand;
import com.practice.employeemanagementsystem.exceptions.NotFoundException;
import com.practice.employeemanagementsystem.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Slf4j
@Controller
public class EmployeeController {

    private static final String EMPLOYEE_FORM_URL = "employees/createupdateform";

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeService.getEmployees());
        return "employees/list";
    }

    @GetMapping("/employees/{id}/view")
    public String viewEmployeeDetail(@PathVariable String id, Model model) {
        model.addAttribute("employee", employeeService.findEmployeeById(Long.valueOf(id)));
        return "employees/detail";
    }

    @GetMapping("/employees/new")
    public String createEmployee(Model model) {
        model.addAttribute("employeeCommand", new EmployeeCommand());
        model.addAttribute("action", "Add Employee");
        return EMPLOYEE_FORM_URL;
    }

    @PostMapping("/employees/handleform")
    public String handleCreateOrUpdate(@Valid @ModelAttribute EmployeeCommand employeeCommand,
                                       BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            log.debug("Error while Binding EmployeeCommand to Employee");
            bindingResult.getAllErrors().forEach(objectError -> {
                log.debug(objectError.getDefaultMessage());
            });
            model.addAttribute("action", "Add Employee");
            return EMPLOYEE_FORM_URL;
        }
        EmployeeCommand savedEmployeeCommand = employeeService.saveEmployeeCommand(employeeCommand);
        return "redirect:/employees/" + savedEmployeeCommand.getId() + "/view";
    }

    @GetMapping("/employees/{id}/update")
    public String updateEmployee(@PathVariable String id, Model model) {
        model.addAttribute("employeeCommand", employeeService.findEmployeeCommandById(Long.valueOf(id)));
        model.addAttribute("action", "Update Employee");
        return EMPLOYEE_FORM_URL;
    }

    @GetMapping("/employees/{id}/delete")
    public String deleteEmployee(@PathVariable String id) {
        employeeService.deleteEmployeeById(Long.valueOf(id));
        return "redirect:/employees";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFoundException(Exception exception) {
        ModelAndView modelAndView = new ModelAndView("404error");
        modelAndView.addObject("exception", exception);
        return modelAndView;
    }

}
