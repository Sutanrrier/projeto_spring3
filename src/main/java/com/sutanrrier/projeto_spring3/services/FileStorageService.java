package com.sutanrrier.projeto_spring3.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.sutanrrier.projeto_spring3.config.FileStorageConfig;

@Service
public class FileStorageService {

	private final Path fileStorageLocation;
	
	@Autowired
	public FileStorageService(FileStorageConfig fileStorageConfig) {
		Path path = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath().normalize();
		
		this.fileStorageLocation = path;
		
		try {
			Files.createDirectories(this.fileStorageLocation);
			
		} catch (Exception e) {
			System.out.println("Erro! Não foi possivel criar o diretório! " + e);
		}
	}
	
	public String storeFile(MultipartFile file) {
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		
		try {
			if(filename.contains("..")) {
				System.out.println("Erro! Nome do arquivo contem path invalido!");
			}
			
			Path targetLocation = this.fileStorageLocation.resolve(filename);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			
		} catch (Exception e) {
			System.out.println("Erro! Não foi possivel salvar o arquivo! " + e);
		}
		
		return filename;
	}
	
	public Resource loadFileAsResource(String filename) {
		try {
			Path filePath = this.fileStorageLocation.resolve(filename).normalize();
			Resource resource = new UrlResource(filePath.toUri());
		
			if(resource.exists()) {
				return resource;
			}
			else {
				System.out.println("Erro! Arquivo não encontrado!");
			}
		} catch (Exception e) {
			System.out.println("Erro! Arquivo não encontrado!");
		}
		
		return null;
	}
}
