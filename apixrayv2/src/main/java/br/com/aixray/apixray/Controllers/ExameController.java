package br.com.aixray.apixray.Controllers;

import br.com.aixray.apixray.Models.*;
import br.com.aixray.apixray.Repositories.ExameRepository;
import br.com.aixray.apixray.Services.S3Services;
import br.com.aixray.apixray.Utils.CustomMultipartFile;
import br.com.aixray.apixray.Utils.JSONStringConverterToImagem;
import br.com.aixray.apixray.Utils.JSONStringConverterToPaciente;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/exame")
public class ExameController {

    JSONStringConverterToPaciente jsonStringConverterToPaciente;
    JSONStringConverterToImagem jsonStringConverterToImagem;
    ExameRepository exameRepository;
    S3Services s3Services;

    RestTemplate restTemplate;

    public ExameController(JSONStringConverterToPaciente jsonStringConverterToPaciente,
                           JSONStringConverterToImagem jsonStringConverterToImagem,
                           S3Services s3Services,
                           ExameRepository exameRepository,
                           RestTemplate restTemplate) {
        this.jsonStringConverterToPaciente = jsonStringConverterToPaciente;
        this.jsonStringConverterToImagem = jsonStringConverterToImagem;
        this.s3Services = s3Services;
        this.exameRepository = exameRepository;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public ResponseEntity getAllExames() {
        return ResponseEntity.ok(exameRepository.findAll());
    }


    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity postExame(@RequestParam Optional<MultipartFile> xray,
                                    @RequestPart String detalhesPaciente,
                                    @RequestPart String followUp) throws IOException {
        //TODO FAZER FOLLOW UP COM BASE NO PACIENTE

        Paciente paciente = jsonStringConverterToPaciente.convert(detalhesPaciente);
        Imagem imagem = new Imagem();

        Exame exame = new Exame();
        exame.setIdExame(UUID.randomUUID().toString());
        exame.setPaciente(paciente);
        exame.setResultado("");
        exame.setImagem(imagem);
        exame.setFollowUp(Integer.parseInt(followUp));

        Date data = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        exame.setTimestampInclusao(data.getTime());
        exame.setDataRegistro(sdf.format(data));

        if(xray.isPresent()) {
            MultipartFile imagemXrayUpload = xray.get();
            String filename = imagemXrayUpload.getOriginalFilename();
            String name = new Date().getTime()+"."+filename.substring(filename.lastIndexOf(".")+1);

            imagem.setPathImagem(name);
            String url = "http://ec2-54-243-123-65.compute-1.amazonaws.com:5000/process_image";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", new FileSystemResource(convert(imagemXrayUpload)));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<Map<String, Object>>() {});

            // Processando retorno da API
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();

                List<String> top_findings = (List<String>) responseBody.get("top_findings");
                String resultados = "";
                for(String finding : top_findings) {
                    resultados += finding+"|";
                }
                exame.setResultado(resultados);

                // Obtendo a imagem codificada em base64 do retorno
                String base64Image = (String) responseBody.get("cam_image");

                // Decodificando a imagem base64
                byte[] imageBytes = Base64.getDecoder().decode(base64Image);

                // Criando um MultipartFile personalizado
                MultipartFile multipartFile = new CustomMultipartFile(
                        imageBytes,
                        "file",
                        "filename.png",
                        "image/png"
                );

                // Salvando as imagens no Amazon S3
                s3Services.uploadFile("images_exame/"+name,imagemXrayUpload);
                String imageKey = "images_exame_retorno/" + name;
                s3Services.uploadFile(imageKey, multipartFile);

                exameRepository.save(exame);
            } else {
                // Trate possíveis erros na resposta da API
            }

            return ResponseEntity.ok(exame);
        }

        return ResponseEntity.ok("Imagem não salvada");
    }

    @GetMapping("/countExameAno")
    public ResponseEntity getCountLast12Months() {
        HashMap<String, Object> retornoHashmap = new HashMap<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 12; i++) {
            Date data = calendar.getTime();
            String dataFormatada = sdf.format(data);
            retornoHashmap.put(dataFormatada, exameRepository.countAllByDataRegistroStartsWith(dataFormatada));
            calendar.add(Calendar.MONTH, -1);
        }
        return ResponseEntity.ok(retornoHashmap);
    }

    @GetMapping("/countGenero")
    public ResponseEntity getCountByGender() {
        HashMap<String, Object> retornoHashmap = new HashMap<>();
        List<Exame> examesMes = exameRepository.findAllByDataRegistroStartsWith("2023/");
        retornoHashmap.put("Masculino",
                examesMes
                        .stream()
                        .filter(exame -> exame.getPaciente().getGeneroPaciente().equals("M"))
                        .count());
        retornoHashmap.put("Feminino",
                examesMes
                        .stream()
                        .filter(exame -> exame.getPaciente().getGeneroPaciente().equals("F"))
                        .count());
        return ResponseEntity.ok(retornoHashmap);
    }

    @PostMapping("/darFeedback/{idExame}")
    public ResponseEntity darFeedback(@RequestBody FeedbackDTO detalhesFeedback,
                                      @PathVariable String idExame) {
        Exame exame = exameRepository.findExameByIdExame(idExame).get();
        Feedback feedback = new Feedback();
        feedback.setRetornoModelo(detalhesFeedback.getRetornoModelo());
        feedback.setRetornoMedico(detalhesFeedback.getRetornoMedico());

        Date data = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");

        feedback.setTimestampInclusao((int) data.getTime());
        feedback.setDataRegistro(sdf.format(data));

        exame.novoFeedback(feedback);
        exameRepository.save(exame);
        return ResponseEntity.ok(exame);
    }

    @GetMapping("/countFeedbacks")
    public ResponseEntity getFeedbackLast12Months() {
        HashMap<String, Object> retornoHashmap = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 12; i++) {
            Date data = calendar.getTime();
            String dataFormatada = sdf.format(data);
            List<Exame> examesMes = exameRepository.findAllByDataRegistroStartsWith(dataFormatada);

            int feedbacks = examesMes.stream().map(Exame::getFeedbacks)
                                            .filter(Objects::nonNull)
                                            .mapToInt(List::size)
                                            .sum();
            retornoHashmap.put(dataFormatada, feedbacks);
            calendar.add(Calendar.MONTH, -1);
        }
        return ResponseEntity.ok(retornoHashmap);
    }

    @GetMapping("/countRightFeedbacks")
    public ResponseEntity getRightFeedbackLast12Months() {
        //TODO só puxar do banco quando feedback não nulo
        HashMap<String, Object> retornoHashmap = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 12; i++) {
            Date data = calendar.getTime();
            String dataFormatada = sdf.format(data);
            List<Exame> examesMes = exameRepository.findAllByDataRegistroStartsWith(dataFormatada);

            int rightFeedbacks = examesMes.stream()
                    .filter(Objects::nonNull)
                    .mapToInt(Exame::countRightFeedbacks).sum();

            retornoHashmap.put(dataFormatada, rightFeedbacks);
            calendar.add(Calendar.MONTH, -1);
        }
        return ResponseEntity.ok(retornoHashmap);
    }

    @GetMapping("/countAllExames")
    public ResponseEntity getCountAllExames(){
        return ResponseEntity.ok(exameRepository.count());
    }

    @GetMapping("/countAllFeedbacks")
    public ResponseEntity getCountAllFeedbacks(){
        List<Exame> examesMes = exameRepository.findAllByFeedbacksNotNull();

        int feedbacks = examesMes.stream().map(Exame::getFeedbacks)
                .mapToInt(List::size)
                .sum();
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/countAllRightFeedbacks")
    public ResponseEntity getCountAllRightFeedbacks(){
        List<Exame> examesMes = exameRepository.findAllByFeedbacksNotNull();

        int feedbacks = examesMes.stream()
                .mapToInt(Exame::countRightFeedbacks)
                .sum();
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/countDoencas")
    public ResponseEntity getContagemPorDoenca() {
        Iterable<Exame> exames = exameRepository.findAll();
        HashMap<String, Integer> retornoHashmap = new HashMap<>();
        exames.forEach(exame -> {
            exame.splitResultado().forEach(doenca -> {
                if(retornoHashmap.containsKey(doenca)) {
                    retornoHashmap.replace(doenca, retornoHashmap.get(doenca)+1);
                } else {
                    retornoHashmap.put(doenca, 1);
                }
            });
        });
        return ResponseEntity.ok(retornoHashmap);
    }

    @GetMapping("/countFaixaEtaria")
    public ResponseEntity getCountFaixaEtaria() {
        Iterable<Exame> exames = exameRepository.findAll();
        HashMap<String, Integer> retornoHashmap = new HashMap<>();
        exames.forEach(exame -> {
            String faixaPaciente = exame.getPaciente().faixaEtaria();
            if(retornoHashmap.containsKey(faixaPaciente)) {
                retornoHashmap.replace(faixaPaciente, retornoHashmap.get(faixaPaciente)+1);
            } else {
                retornoHashmap.put(faixaPaciente, 1);
            }
        });
        return ResponseEntity.ok(retornoHashmap);
    }

    // Método para obter a imagem como array de bytes
    private byte[] getImageAsByteArray(MultipartFile file) throws IOException {
        return file.getBytes();
    }

    private File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}