package br.com.aixray.apixray.Utils;

import br.com.aixray.apixray.Models.Imagem;
import br.com.aixray.apixray.Models.Paciente;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/*
TODO: USAR ISSO PARA RETORNAR DE MANEIRA DECENTE PARA O FRONT
 */
@Component
public class JSONStringConverterToImagem implements Converter<String, Imagem> {

    private final ObjectMapper objectMapper;

    public JSONStringConverterToImagem(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Imagem convert(String s){
        try {
            return objectMapper.readValue(s, Imagem.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}

