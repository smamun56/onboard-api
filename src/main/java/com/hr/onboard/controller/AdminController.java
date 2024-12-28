package com.hr.onboard.controller;


import com.hr.onboard.controller.constraint.auth.ApiAllowsTo;
import com.hr.onboard.data.ErrorMessageResponse;
import com.hr.onboard.data.PageList;
import com.hr.onboard.data.PageOfUserProfile;
import com.hr.onboard.data.PageRequest;
import com.hr.onboard.data.admin.request.ChangeUserRoleRequest;
import com.hr.onboard.data.admin.request.UserIdRequest;
import com.hr.onboard.data.user.UserProfile;
import com.hr.onboard.entity.enums.Role;
import com.hr.onboard.exception.InvalidOperation;
import com.hr.onboard.exception.UserDoesNotExist;
import com.hr.onboard.service.user.auth.ActivationService;
import com.hr.onboard.service.user.auth.RoleService;
import com.hr.onboard.service.user.profile.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(path = "/api/admin")
public class AdminController {
    @Autowired
    RoleService roleService;

    @Autowired
    ActivationService activationService;

    @Autowired
    ProfileService profileService;

    @Operation()
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "403",
                            description = """
                                <ul>
                                <li>you are not ADMIN</li>
                                <li>target user==request user</li>
                                <li>target role==original role</li>
                                <li>target user is the only ADMIN</li>
                                </ul>""",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema (implementation = ErrorMessageResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "target user is not exist",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessageResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description =
                                    "after success, target user will be logged out "
                                            + "by revoke all access and refresh tokens",
                            content = @Content
                    )

            }
    )
    @RequestMapping(path = "", method = RequestMethod.POST)
    public ResponseEntity changeRole(@Valid @RequestBody ChangeUserRoleRequest request){
        try {
            roleService.changeRoleOf(request.getId(), Role.valueOf(request.getRole()));
            return ResponseEntity.ok().build();
        } catch (InvalidOperation e) {
            return new ResponseEntity<>(new ErrorMessageResponse(e.getMessage()), HttpStatus.FORBIDDEN);
        } catch (UserDoesNotExist e) {
            return new ResponseEntity<>(new ErrorMessageResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "activate target user", description = "this is only allowed to ADMIN")
    @ApiAllowsTo(roles = Role.ADMIN)
    @SecurityRequirements({
            @SecurityRequirement(name = "jwt"),
            @SecurityRequirement(name = "jwt-in-cookie")
    })
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "403",
                            description =
                                    """
                                        <ul>
                                        <li>you are not ADMIN</li>
                                        <li>target user==request user</li>
                                        <li>target user is already active</li>
                                        </ul>""",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "target user is not exist",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(
                            responseCode = "200",
                            description =
                                    "after success, target user will be logged out "
                                            + "by revoke all access and refresh tokens",
                            content = @Content),
            })
    @RequestMapping(path = "/activateUser", method = RequestMethod.POST)
    public ResponseEntity activateUser(@Valid @org.springframework.web.bind.annotation.RequestBody UserIdRequest request) {
        try {
            activationService.activateUser(request.getId());
            return ResponseEntity.ok().build();
        } catch (InvalidOperation e) {
            return new ResponseEntity<>(new ErrorMessageResponse(e.getMessage()), HttpStatus.FORBIDDEN);
        } catch (UserDoesNotExist e) {
            return new ResponseEntity<>(new ErrorMessageResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "deactivate target user", description = "this is only allowed to ADMIN")
    @ApiAllowsTo(roles = Role.ADMIN)
    @SecurityRequirements({
            @SecurityRequirement(name = "jwt"),
            @SecurityRequirement(name = "jwt-in-cookie")
    })
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "403",
                            description =
                                    """
                                        <ul>
                                        <li>you are not ADMIN</li>
                                        <li>target user==request user</li>
                                        <li>target user is already inactive</li>
                                        </ul>""",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "target user is not exist",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(
                            responseCode = "200",
                            description =
                                    "after success, target user will be logged out "
                                            + "by revoke all access and refresh tokens",
                            content = @Content),
            })
    @RequestMapping(path = "/deactivateUser", method = RequestMethod.POST)
    public ResponseEntity deactivateUser(@Valid @org.springframework.web.bind.annotation.RequestBody UserIdRequest request) {
        try {
            activationService.deactivateUser(request.getId());
            return ResponseEntity.ok().build();
        } catch (InvalidOperation e) {
            return new ResponseEntity<>(new ErrorMessageResponse(e.getMessage()), HttpStatus.FORBIDDEN);
        } catch (UserDoesNotExist e) {
            return new ResponseEntity<>(new ErrorMessageResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "get all user profiles with page param",
            description = "this is only allowed to ADMIN")
    @ApiAllowsTo(roles = Role.ADMIN)
    @SecurityRequirements({
            @SecurityRequirement(name = "jwt"),
            @SecurityRequirement(name = "jwt-in-cookie")
    })
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "403",
                            description =
                                    """
                                        <ul>
                                        <li>you are not ADMIN</li>
                                        </ul>""",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessageResponse.class))),
                    @ApiResponse(
                            responseCode = "200",
                            description = "get all user profiles with page param",
                            content =
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = PageOfUserProfile.class)))
            })
    @RequestMapping(path = "/getUserList", method = RequestMethod.GET)
    public ResponseEntity getAllUserProfiles(@ParameterObject @Valid PageRequest request) {
        PageList<UserProfile> pageList =
                profileService.getAllUserProfilesWithPage(request.getPage(), request.getSize());
        return ResponseEntity.ok(new PageOfUserProfile(pageList));
    }

}
