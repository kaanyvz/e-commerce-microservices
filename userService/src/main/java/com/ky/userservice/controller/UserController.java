package com.ky.userservice.controller;

import com.ky.userservice.dto.Me;
import com.ky.userservice.dto.UserDto;

import com.ky.userservice.exc.HttpResponse;
import com.ky.userservice.model.User;
import com.ky.userservice.model.UserPrincipal;
import com.ky.userservice.request.UpdatePasswordRequest;
import com.ky.userservice.request.UpdateUserRequest;
import com.ky.userservice.response.AuthenticationResponse;
import com.ky.userservice.service.UserService;

import com.ky.userservice.util.AuthenticationHelper;
import org.springframework.http.HttpHeaders;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/v1/user")
public class UserController {
    private final UserService userService;
    private final AuthenticationHelper auth;

    public UserController(UserService userService, AuthenticationHelper auth) {
        this.userService = userService;
        this.auth = auth;
    }

    @GetMapping("/getByEmail/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email){
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/me")
    public ResponseEntity<Me> getMe(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader){
        String token = authHeader.substring(7);
        return ResponseEntity.ok(userService.getMe(token));
    }

    @GetMapping("/resetPassword/{email}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable String email){
        userService.resetPassword(email);
        return new ResponseEntity<>(new HttpResponse(
                HttpStatus.OK.value(),
                HttpStatus.OK,
                HttpStatus.OK.getReasonPhrase().toUpperCase(),
                "Your new Password has sent to you email"),
                HttpStatus.OK);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String id) {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("image/png"))
                .body(userService.downloadImageFromFileSystem(id));
    }


    @PostMapping("/upload")
    public ResponseEntity<String> uploadImageToFileSystem(@RequestPart("image") MultipartFile file,
                                                          @RequestParam String email) {
        return ResponseEntity.ok().body(userService.uploadImgToTheSystem(email, file));
    }

    @PutMapping("/updateUser")
    public ResponseEntity<UserDto> updateUser(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) MultipartFile profileImage
    ) {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setEmail(email);
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setProfileImage(profileImage);

        UserDto updatedUser = userService.updateUser(request);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/updatePassword")
    public ResponseEntity<AuthenticationResponse> updatePassword(@RequestBody UpdatePasswordRequest request,
                                                                 @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        String jwtToken = authorizationHeader.substring(7);
        User user = userService.updatePassword(request, jwtToken);
        UserPrincipal userPrincipal = new UserPrincipal(user);
        AuthenticationResponse authenticationResponse = auth.getAuthResponse(userPrincipal);
        return ResponseEntity.ok(authenticationResponse);
    }

    @DeleteMapping("/deleteImage/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable String id){
        try {
            String message = userService.deleteImage(id);
            return ResponseEntity.ok().body(message);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
