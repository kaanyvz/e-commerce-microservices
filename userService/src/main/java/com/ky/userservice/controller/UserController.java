package com.ky.userservice.controller;

import com.ky.userservice.dto.Me;
import com.ky.userservice.dto.UserDto;

import com.ky.userservice.exc.HttpResponse;
import com.ky.userservice.service.UserService;

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

    public UserController(UserService userService) {
        this.userService = userService;
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

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImageToFIleSystem(@RequestPart("image") MultipartFile file,
                                                          @RequestParam String email) {
        return ResponseEntity.ok().body(userService.uploadImgToTheSystem(email, file));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String id) {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("image/png"))
                .body(userService.downloadImageFromFileSystem(id));
    }


}
