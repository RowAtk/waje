package com.rowatk.waje.handlers

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/users")
class UserController {

    @GetMapping("/me")
    suspend fun me(@AuthenticationPrincipal principal: Principal) : Profile {
        return Profile(principal.name);
    }

    data class Profile(val username: String)
}
