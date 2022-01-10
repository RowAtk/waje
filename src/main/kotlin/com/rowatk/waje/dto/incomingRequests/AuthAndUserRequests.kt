package com.rowatk.waje.dto.incomingRequests

import com.rowatk.waje.dto.DTO

class LoginRequest(val username: String, val password: String) : DTO;

class RegisterRequest(val username: String, val email: String, val password: String,): DTO
