package br.com.aixray.apixray.Utils;

import br.com.aixray.apixray.Models.PacienteDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class JSONStringConverterToPacienteDTO implements Converter<String, PacienteDTO> {

    private final ObjectMapper objectMapper;

    public JSONStringConverterToPacienteDTO(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public PacienteDTO convert(String s){
        try {
            return objectMapper.readValue(s, PacienteDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
