package com.hr.onboard.controller;

import com.hr.onboard.controller.constraint.auth.AuthenticatedApi;
import com.hr.onboard.data.user.UserProfile;
import com.hr.onboard.dto.UserDto;
import com.hr.onboard.exception.UserDoesNotExist;
import com.hr.onboard.service.user.UserService;
import com.hr.onboard.service.user.profile.ProfileService;
import com.hr.onboard.utils.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;

    @Autowired
    ProfileService profileService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){
        UserDto savedUser = userService.createUser(userDto);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long userId){
        UserDto userDto = userService.getUserById(userId);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUser() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    @PutMapping("{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long userId, @RequestBody UserDto updatedUser){
        UserDto userDto = userService.updateUser(userId, updatedUser);
        return ResponseEntity.ok(userDto);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId){
        userService.deleteUser(userId);
        return ResponseEntity.ok("User has been deleted");
    }

    // profile stage

    @Operation(
            summary = "get profile of login user",
            description = "this is allowed to any authenticated user")
    @AuthenticatedApi
    @SecurityRequirements({
            @SecurityRequirement(name = "jwt"),
            @SecurityRequirement(name = "jwt-in-cookie")
    })
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "user profile",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserProfile.class))),
            })
    @RequestMapping(path = "/profile", method = RequestMethod.GET)
    public ResponseEntity profile() {
        Map<String, Object> response = new HashMap<>();
        try {
            return ResponseEntity.ok(profileService.getProfile(AuthUtil.currentUserDetail().getId()));
        } catch (UserDoesNotExist ex) {
            // will occur if user is not in db but the userDetail is loaded before this method
            // with JwtAuthenticationFilter, so only the db corrupt will cause this
            response.put("message", "unknown error, please try again later !");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
