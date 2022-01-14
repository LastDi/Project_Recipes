package com.example.recipes.authentication;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsUpd extends UserDetails {
    Long getId();
}
