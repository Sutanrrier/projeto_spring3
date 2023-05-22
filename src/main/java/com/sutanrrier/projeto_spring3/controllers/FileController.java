package com.sutanrrier.projeto_spring3.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sutanrrier.projeto_spring3.services.FileStorageService;
import com.sutanrrier.projeto_spring3.vo.UploadFileVO;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/v1/file")
@Tag(name = "File Endpoint", description = "Endpoint para upload/download de arquivos")
public class FileController {

	@Autowired
	private FileStorageService fileStorageService;
	
	@GetMapping("/downloadFile/{filename:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String filename, HttpServletRequest request) {
		Resource resource = fileStorageService.loadFileAsResource(filename);
		String contentType = "";
		
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (Exception e) {
			System.out.println("Erro! Não foi possivel determinar contentType!");
		}
		
		if(contentType.isBlank()) {
			contentType = "application/octet-string";
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
	
	@PostMapping("/uploadFile")
	public UploadFileVO uploadFile(@RequestParam("file") MultipartFile file) {
		String filename = fileStorageService.storeFile(file);
		
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/api/v1/file/downloadFile/")
				.path(filename)
				.toUriString();
		
		return new UploadFileVO(filename, fileDownloadUri, file.getContentType(), file.getSize());
	}
	
	@PostMapping("/uploadMultipleFiles")
	public List<UploadFileVO> uploadFile(@RequestParam("files") MultipartFile[] files) {
		
		return Arrays.asList(files).stream().map(file -> uploadFile(file)).collect(Collectors.toList());	
	}
	
}
