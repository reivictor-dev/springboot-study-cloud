package com.study.study_spring.data.dto.v1;

import java.io.Serializable;

public class UploadFileResponseDTO implements Serializable  {

    private static final long serialVersionUID = 1L;

    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;

    public UploadFileResponseDTO(String fileName, String fileDownloadUri, String fileType, long size) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
    }

    public UploadFileResponseDTO() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
        result = prime * result + ((fileDownloadUri == null) ? 0 : fileDownloadUri.hashCode());
        result = prime * result + ((fileType == null) ? 0 : fileType.hashCode());
        result = prime * result + (int) (size ^ (size >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UploadFileResponseDTO other = (UploadFileResponseDTO) obj;
        if (fileName == null) {
            if (other.fileName != null)
                return false;
        } else if (!fileName.equals(other.fileName))
            return false;
        if (fileDownloadUri == null) {
            if (other.fileDownloadUri != null)
                return false;
        } else if (!fileDownloadUri.equals(other.fileDownloadUri))
            return false;
        if (fileType == null) {
            if (other.fileType != null)
                return false;
        } else if (!fileType.equals(other.fileType))
            return false;
        if (size != other.size)
            return false;
        return true;
    }

    
}
