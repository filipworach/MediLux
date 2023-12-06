package com.MediLux.MediLux.Service;

import com.MediLux.MediLux.Model.Employee;
import com.MediLux.MediLux.Model.Patient;
import com.MediLux.MediLux.Model.Role;
import com.MediLux.MediLux.Repositories.EmployeeRepository;
import com.MediLux.MediLux.Repositories.PatientRepository;
import com.MediLux.MediLux.Repositories.RoleRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    private final PatientRepository patientRepository;
    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;

    public MyUserDetailsService(PatientRepository patientRepository, RoleRepository roleRepository, EmployeeRepository employeeRepository) {
        this.patientRepository = patientRepository;
        this.roleRepository = roleRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Patient> patient = patientRepository.findById(email);
        Optional<Employee> employee = employeeRepository.findById(email);

        if (patient.isPresent()) {
            Optional<Role> role = roleRepository.findByName(patient.get().getRole().getName());
            return new org.springframework.security.core.userdetails.User(
                    patient.get().getEmail(), patient.get().getPassword(), true, true, true,
                    true, getRoles(Collections.singletonList(patient.get().getRole())));
        } else if (employee.isPresent()) {
            Optional<Role> role = roleRepository.findByName(employee.get().getRole().getName());
            return new org.springframework.security.core.userdetails.User(
                    employee.get().getEmail(), employee.get().getPassword(), true, true, true,
                    true, getRoles(Collections.singletonList(employee.get().getRole())));
        } else {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
    }

    private Collection<GrantedAuthority> getRoles(Collection<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }
}
