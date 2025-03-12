package com.project.sb_ecommerce.controller;

import com.project.sb_ecommerce.DTOs.Requests.LoginDTO;
import com.project.sb_ecommerce.DTOs.Responses.LoginResponse;
import com.project.sb_ecommerce.DTOs.Responses.MessageResponse;
import com.project.sb_ecommerce.DTOs.Requests.SignupRequest;
import com.project.sb_ecommerce.exceptions.APIException;
import com.project.sb_ecommerce.model.Enums.AppRole;
import com.project.sb_ecommerce.model.Role;
import com.project.sb_ecommerce.model.User;
import com.project.sb_ecommerce.repository.RoleRepository;
import com.project.sb_ecommerce.repository.UserRepository;
import com.project.sb_ecommerce.security.JWT.JwtUtils;
import com.project.sb_ecommerce.security.Services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController
{
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser( @RequestBody LoginDTO loginRequest )
    {
        Authentication authentication;
        try
        {
            authentication = authenticationManager
                    .authenticate( new UsernamePasswordAuthenticationToken( loginRequest.getUsername(), loginRequest.getPassword() ) );
        }
        catch ( AuthenticationException exception )
        {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>( map, HttpStatus.NOT_FOUND );
        }

        SecurityContextHolder.getContext().setAuthentication( authentication );

        UserDetailsImpl userDetails = ( UserDetailsImpl ) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie( userDetails );

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        LoginResponse response = new LoginResponse(userDetails.getUsername(), roles);
        return ResponseEntity.ok().header( HttpHeaders.SET_COOKIE, jwtCookie.toString() )
                .body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser( @Valid @RequestBody SignupRequest signUpRequest )
    {
        if ( userRepository.existsByUsername( ( signUpRequest.getUsername() ) ) )
        {
            return ResponseEntity.badRequest().body( new MessageResponse( "Error: Username is already taken!" ) );
        }

        if ( userRepository.existsByEmail( (signUpRequest.getEmail() ) ) )
        {
            return ResponseEntity.badRequest().body( new MessageResponse( "Error: Email is already in use!" ) );
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode( signUpRequest.getPassword() ) );

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if ( strRoles == null )
        {
            Role userRole = roleRepository.findByRoleName( AppRole.ROLE_USER )
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add( userRole );
        }
        else
        {
            strRoles.forEach(role -> {
                switch (role)
                {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName( AppRole.ROLE_ADMIN )
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "seller":
                        Role modRole = roleRepository.findByRoleName( AppRole.ROLE_SELLER )
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByRoleName( AppRole.ROLE_USER )
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles( roles );
        userRepository.save( user );

        return ResponseEntity.ok( new MessageResponse( "User registered successfully !" ) );
    }

    @GetMapping("/username")
    public ResponseEntity<?> getcurrentAuthenticatedUserName( Authentication authentication )
    {
        if ( authentication != null )
            return new ResponseEntity<>( authentication.getName(), HttpStatus.OK );
        else
            return new ResponseEntity<>( "No user is authenticated !", HttpStatus.NOT_FOUND );
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOutUser(Authentication authentication)
    {
        if (authentication != null)
        {
            ResponseCookie cleanCookie = jwtUtils.getCleanCookie();
            return ResponseEntity.ok().header( HttpHeaders.SET_COOKIE, cleanCookie.toString() )
                    .body( "User logged out successfully !" );
        }
        else
            return new ResponseEntity<>("No active session found !", HttpStatus.NOT_FOUND );
    }
}
