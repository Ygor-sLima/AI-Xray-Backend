package br.com.aixray.apixray.Controllers;

import br.com.aixray.apixray.Models.*;
import br.com.aixray.apixray.Repositories.ExameRepository;
import br.com.aixray.apixray.Services.S3Services;
import br.com.aixray.apixray.Utils.JSONStringConverterToImagem;
import br.com.aixray.apixray.Utils.JSONStringConverterToPaciente;
import br.com.aixray.apixray.Utils.JSONStringConverterToPacienteDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    public ExameController(JSONStringConverterToPaciente jsonStringConverterToPaciente,
                           JSONStringConverterToImagem jsonStringConverterToImagem,
                           S3Services s3Services,
                           ExameRepository exameRepository) {
        this.jsonStringConverterToPaciente = jsonStringConverterToPaciente;
        this.jsonStringConverterToImagem = jsonStringConverterToImagem;
        this.s3Services = s3Services;
        this.exameRepository = exameRepository;
    }

    @GetMapping
    public ResponseEntity getAllExames() {
        return ResponseEntity.ok(exameRepository.findAll());
    }


    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity postExame(@RequestParam Optional<MultipartFile> xray,
                                    @RequestPart String detalhesPaciente,
                                    @RequestPart String detalhesImagem) throws IOException {
        Paciente paciente = jsonStringConverterToPaciente.convert(detalhesPaciente);
        Imagem imagem = jsonStringConverterToImagem.convert(detalhesImagem);
        System.out.println(imagem.toString());

        Exame exame = new Exame();
        exame.setIdExame(UUID.randomUUID().toString());
        //exame.setPaciente(detalhesPaciente);
        exame.setResultado("");
        //exame.setImagem(detalhesImagem);
        if(xray.isPresent()) {
            MultipartFile imagemXrayUpload = xray.get();
            String filename = imagemXrayUpload.getOriginalFilename();
            String name = new Date().getTime()+"."+filename.substring(filename.lastIndexOf(".")+1);
            s3Services.uploadFile(name,imagemXrayUpload);
            imagem.setPathImagem(name);
            return ResponseEntity.ok("Imagem salvada");
        }

        return ResponseEntity.ok("Imagem não salvada");
    }

    @GetMapping("/countExameAno")
    public ResponseEntity getCountLast12Months() {
        HashMap<String, Object> retornoHashmap = new HashMap<>();
        Date data = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
        for (int i = 0; i < 12; i++) {
            String dataFormatada = sdf.format(data);
            retornoHashmap.put(dataFormatada, exameRepository.countAllByDataRegistroStartsWith(dataFormatada));
        }
        return ResponseEntity.ok(retornoHashmap);
    }

    @GetMapping("/countGenero")
    public ResponseEntity getCountByGender() {
        HashMap<String, Object> retornoHashmap = new HashMap<>();
        Date data = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
        List<Exame> examesMes = exameRepository.findAllByDataRegistroStartsWith(sdf.format(data));
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
        Date data = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
        for (int i = 0; i < 12; i++) {
            String dataFormatada = sdf.format(data);
            List<Exame> examesMes = exameRepository.findAllByDataRegistroStartsWith(sdf.format(data));

            int feedbacks = examesMes.stream().map(Exame::getFeedbacks)
                                            .filter(Objects::nonNull)
                                            .mapToInt(List::size)
                                            .sum();
            retornoHashmap.put(dataFormatada, feedbacks);
        }
        return ResponseEntity.ok(retornoHashmap);
    }

    @GetMapping("/countRightFeedbacks")
    public ResponseEntity getRightFeedbackLast12Months() {
        //TODO só puxar do banco quando feedback não nulo
        HashMap<String, Object> retornoHashmap = new HashMap<>();
        Date data = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
        for (int i = 0; i < 12; i++) {
            String dataFormatada = sdf.format(data);
            List<Exame> examesMes = exameRepository.findAllByDataRegistroStartsWith(sdf.format(data));

            int rightFeedbacks = examesMes.stream()
                    .filter(Objects::nonNull)
                    .mapToInt(Exame::countRightFeedbacks).sum();

            retornoHashmap.put(dataFormatada, rightFeedbacks);
        }
        return ResponseEntity.ok(retornoHashmap);
    }

    @GetMapping("/countDoenca")
    public ResponseEntity getCountDoenca() {
        List<String> doencas = new ArrayList<String>();
        List<Exame> exames = exameRepository.getDistinctByResultado();
//                .stream()
//                .map(exame -> exame.getResultado())
//                .forEach(s -> doencas.add(s));

        return ResponseEntity.ok(exames);
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
}