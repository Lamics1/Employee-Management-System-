package com.example.employeemanagementsystem.Controller;

import com.example.employeemanagementsystem.Api.ApiResponse;
import com.example.employeemanagementsystem.Model.Employee;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    ArrayList<Employee> employees = new ArrayList<>();


    @GetMapping("/get")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(200).body(employees);
    }


    @PutMapping("/add")
    public ResponseEntity<?> addEmployee(@Valid@RequestBody Employee employee, Errors errors) {
        if (errors.hasErrors())
            return ResponseEntity.status(400).body(errors.getFieldError().getDefaultMessage());

        employees.add(employee);
        return ResponseEntity.status(200).body(new ApiResponse("Employee added successfully"));
    }


    @PutMapping("/update/{index}")
    public ResponseEntity<?> updateEmployee(@PathVariable int index, @Valid @RequestBody Employee employee) {
        if (index < 0 || index >= employees.size()) {
            return ResponseEntity.badRequest().body("Invalid index");
        }
        employees.set(index, employee);
        return ResponseEntity.status(200).body(new ApiResponse("Employee updated successfully"));
    }


    @DeleteMapping("/delete/{index}")
    public ResponseEntity<?> deleteEmployee(@PathVariable int index) {
        if (index < 0 || index >= employees.size())
            return ResponseEntity.status(400).body("Invalid index");

        employees.remove(index);
        return ResponseEntity.status(200).body(new ApiResponse("Employee deleted successfully"));
    }


    @GetMapping("/position")
    public ResponseEntity<?> searchByPosition(@RequestParam String position) {
        if (!position.equals("supervisor") && !position.equals("coordinator"))
            return ResponseEntity.status(400).body("Invalid position");

        ArrayList<Employee> result = new ArrayList<>();
        for (Employee e : employees) {
            if (e.getPosition().equalsIgnoreCase(position)) {
                result.add(e);
            }
        }
        return ResponseEntity.status(200).body(result);
    }


    @GetMapping("/age")
    public ResponseEntity<?> getByAgeRange(@RequestParam int minAge, @RequestParam int maxAge) {
        if (minAge < 0 || maxAge < 0 || minAge > maxAge)
            return ResponseEntity.status(400).body("Invalid age range");

        ArrayList<Employee> result = new ArrayList<>();
        for (Employee e : employees) {
            if (e.getAge() >= minAge && e.getAge() <= maxAge) {
                result.add(e);
            }
        }
        return ResponseEntity.status(200).body(result);
    }


    @PutMapping("/leave/{index}")
    public ResponseEntity<?> applyLeave(@PathVariable int index) {
        if (index < 0 || index >= employees.size()) {
            return ResponseEntity.status(400).body("Invalid index");
        }
        Employee e = employees.get(index);
        if (e.isOnLeave())
            return ResponseEntity.status(400).body("Employee is already on leave");
        if (e.getAnnualLeave() < 1)
            return ResponseEntity.status(400).body("No annual leave remaining");

        e.setOnLeave(true);
        e.setAnnualLeave(e.getAnnualLeave() - 1);
        return ResponseEntity.status(200).body(new ApiResponse("Leave applied successfully"));
    }

    @GetMapping("/noleave")
    public ResponseEntity<?> getNoLeaveEmployees() {
        ArrayList<Employee> result = new ArrayList<>();
        for (Employee e : employees) {
            if (e.getAnnualLeave() == 0) {
                result.add(e);
            }
        }
        return ResponseEntity.status(200).body(result);
    }

    @PutMapping("/promote")
    public ResponseEntity<?> promoteEmployee(@RequestParam String supervisorId, @RequestParam String employeeId){
        Employee requester = null;
        Employee target = null;

        for (Employee e : employees) {
            if (e.getId().equals(supervisorId)) requester = e;
            if (e.getId().equals(employeeId)) target = e;
        }

        if (requester == null || target == null)
            return ResponseEntity.status(400).body("One or both employees not found");

        if (!requester.getPosition().equals("supervisor"))
            return ResponseEntity.status(400).body("Only supervisors can promote");

        if (target.getAge() < 30)
            return ResponseEntity.status(400).body("Employee must be at least 30");

        if (target.isOnLeave())
            return ResponseEntity.status(400).body("Employee must not be on leave");

        target.setPosition("supervisor");
        return ResponseEntity.status(200).body(new ApiResponse("Employee promoted successfully"));
    }
}

