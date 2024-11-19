package ru.hogwarts.school.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping("/avatar")
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "/{studentId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long studentId, @RequestParam MultipartFile avatar) throws IOException {
        avatarService.uploadAvatar(studentId, avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/avatar-from-db")
    public ResponseEntity<byte[]> downloadAvatarFromDb(@PathVariable Long id) {
        return avatarService.prepareAvatarResponseFromDb(id);
    }

    @GetMapping(value = "/{id}/avatar-from-file")
    public void downloadAvatarFromFile(@PathVariable Long id, HttpServletResponse response) throws IOException {
        avatarService.prepareAvatarResponseFromFile(id, response);
    }

    @GetMapping
    public ResponseEntity<List<Avatar>>getAllAvatar(@RequestParam("page") Integer pageNumber,
                                                    @RequestParam("size") Integer pageSize){
        List<Avatar> avatars = avatarService.getAllAvatar(pageNumber,pageSize);
        return ResponseEntity.ok(avatars);
    }
}