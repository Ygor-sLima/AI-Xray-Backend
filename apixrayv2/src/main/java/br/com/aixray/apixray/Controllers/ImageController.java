package br.com.aixray.apixray.Controllers;

import br.com.aixray.apixray.Services.S3Services;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/imagem")
public class ImageController {

    final String PATH_FOLDER_S3 = "images_exame/";
    final String PATH_FOLDER_RESULTADO_S3 = "images_exame_retorno/";
    S3Services s3Services;

    public ImageController(S3Services s3Services) {
        this.s3Services = s3Services;
    }

    @GetMapping("/{nome}")
    public ResponseEntity downloadImagem(@PathVariable String nome)  throws IOException {
        S3Object stream = s3Services.downloadFile(PATH_FOLDER_S3+nome);
        byte[] content = IOUtils.toByteArray(stream.getObjectContent());
        ByteArrayResource resource = new ByteArrayResource(content);
        return ResponseEntity.status(200)
                .contentLength(content.length)
                .header("Content-type", "application/octet-stream")
                .body(resource);
    }

    @GetMapping("/resultado/{nome}")
    public ResponseEntity downloadResultado(@PathVariable String nome)  throws IOException {
        S3Object stream = s3Services.downloadFile(PATH_FOLDER_RESULTADO_S3+nome);
        byte[] content = IOUtils.toByteArray(stream.getObjectContent());
        ByteArrayResource resource = new ByteArrayResource(content);
        return ResponseEntity.status(200)
                .contentLength(content.length)
                .header("Content-type", "application/octet-stream")
                .body(resource);
    }
}
