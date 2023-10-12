package br.com.aixray.apixray.Utils;

import br.com.aixray.apixray.Models.Paciente;
import br.com.aixray.apixray.Models.PacienteDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
/*
TODO: USAR ISSO PARA RETORNAR DE MANEIRA DECENTE PARA O FRONT
 */
@Component
public class JSONStringConverterToPaciente implements Converter<String, Paciente> {

    private final ObjectMapper objectMapper;

    public JSONStringConverterToPaciente(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Paciente convert(String s){
        try {
            return objectMapper.readValue(s, Paciente.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
