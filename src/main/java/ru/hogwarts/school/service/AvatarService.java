package ru.hogwarts.school.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exception.AvatarByStudentIdNotFoundException;
import ru.hogwarts.school.exception.FileNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class AvatarService {
    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;

    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        Student student = studentRepository.getById(studentId);
        Path filePath = Path.of(avatarsDir, student + "." + getExtensions(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = findAvatar(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());
        avatarRepository.save(avatar);
    }

    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public Avatar findAvatar(Long id) {
        return avatarRepository.findById(id)
                .orElseThrow(() -> new AvatarByStudentIdNotFoundException(id));
    }

    public byte[] getAvatarDataFromDb(Long id) {
        Avatar avatar = findAvatar(id);
        return avatar.getData();
    }

    public InputStream getAvatarDataFromFile(Long id) throws IOException {
        Avatar avatar = findAvatar(id);
        Path path = Path.of(avatar.getFilePath());
        return Files.newInputStream(path);
    }

    public String getAvatarMediaType(Long id) {
        Avatar avatar = findAvatar(id);
        return avatar.getMediaType();
    }

    public long getAvatarFileSize(Long id) {
        Avatar avatar = findAvatar(id);
        return avatar.getFileSize();
    }

    public ResponseEntity<byte[]> prepareAvatarResponseFromDb(Long id) {
        byte[] avatarData = getAvatarDataFromDb(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(getAvatarMediaType(id)));
        headers.setContentLength(avatarData.length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatarData);
    }

    public void prepareAvatarResponseFromFile(Long id, HttpServletResponse response) throws IOException {
        try (InputStream is = getAvatarDataFromFile(id);
             OutputStream os = response.getOutputStream()) {

            response.setStatus(HttpStatus.OK.value());
            response.setContentType(getAvatarMediaType(id));
            response.setContentLength((int) getAvatarFileSize(id));

            is.transferTo(os);
        }
    }

    public List<Avatar> getAllAvatar(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return avatarRepository.findAll(pageRequest).getContent();
    }
}