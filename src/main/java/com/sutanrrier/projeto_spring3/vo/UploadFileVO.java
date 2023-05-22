package com.sutanrrier.projeto_spring3.vo;

import java.io.Serializable;

public class UploadFileVO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String filename;
	private String fileDownloadUri;
	private String fileType;
	private Long size;

	public UploadFileVO() {

	}

	public UploadFileVO(String filename, String fileDownloadUri, String fileType, Long size) {
		this.filename = filename;
		this.fileDownloadUri = fileDownloadUri;
		this.fileType = fileType;
		this.size = size;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFileDownloadUri() {
		return fileDownloadUri;
	}

	public void setFileDownloadUri(String fileDownloadUri) {
		this.fileDownloadUri = fileDownloadUri;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

}
