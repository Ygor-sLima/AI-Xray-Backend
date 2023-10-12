package br.com.aixray.apixray.Services;

import com.amazonaws.services.s3.model.S3Object;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface S3Services {
    public S3Object downloadFile(String keyName);
    public void uploadFile(String keyName, MultipartFile uploadFilePath) throws IOException;
}