package com.supershop;

import com.supershop.Entity.Role;
import com.supershop.Entity.User;
import com.supershop.Repository.RoleRepository;
import com.supershop.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootApplication
public class SupershopApplication {

	public static void main(String[] args) {
		SpringApplication.run(SupershopApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (roleRepository.findRolesByUserId(1L).stream().noneMatch(r -> r.getName().equals("ROLE_ADMIN"))) {
				System.out.println("WARN: ROLE_ADMIN not found. Creating a default admin might fail without pre-existing roles.");
			}
			if (roleRepository.findRolesByUserId(1L).stream().noneMatch(r -> r.getName().equals("ROLE_USER"))) {
				System.out.println("WARN: ROLE_USER not found. Creating a default admin might fail without pre-existing roles.");
			}
			if (userRepository.findByUsername("admin").isEmpty()) {
				Set<Role> adminRoles = new HashSet<>();
				roleRepository.findRolesByUserId(1L).stream()
						.filter(role -> "ROLE_ADMIN".equals(role.getName()))
						.findFirst()
						.ifPresent(adminRoles::add);

				User adminUser = User.builder()
						.username("admin")
						.password(passwordEncoder.encode("admin"))
						.email("admin@shop.com")
						.enabled(true)
						.roles(adminRoles)
						.build();

				userRepository.save(adminUser);
				System.out.println("Created default admin user: admin");
			}
		};
	}
	@Configuration
	@EnableWebSecurity
	@EnableMethodSecurity
	public static class SecurityConfig {

		@Bean
		public PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}
	}

	@Service
	public static class CustomUserDetailsService implements UserDetailsService {

		@Autowired
		private UserRepository userRepository;
		@Autowired
		private RoleRepository roleRepository;

		@Override
		@Transactional
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			User user = userRepository.findByUsername(username)
					.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

			Set<GrantedAuthority> authorities = roleRepository.findRolesByUserId(user.getId()).stream()
					.map(role -> new SimpleGrantedAuthority(role.getName()))
					.collect(Collectors.toSet());

			return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
		}
	}
}
