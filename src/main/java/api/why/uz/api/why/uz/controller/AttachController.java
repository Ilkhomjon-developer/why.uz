package api.why.uz.api.why.uz.controller;

import api.why.uz.api.why.uz.dto.AttachDTO;
import api.why.uz.api.why.uz.service.AttachService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/attach")
@RequiredArgsConstructor
public class AttachController {

    private final AttachService attachService;

    @PreAuthorize("hasAnyRole('ADMIN', 'PUBLISHER', 'MODERATOR')")
    @PostMapping(value="/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AttachDTO> upload(@RequestParam("file") MultipartFile file){
        return ResponseEntity.ok(attachService.uploadFile(file));
    }

    @GetMapping("/open/{id:.+}")
    public ResponseEntity<Resource> open(@PathVariable String id){
        return attachService.open(id);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable String id){
        return attachService.download(id);
    }

}
