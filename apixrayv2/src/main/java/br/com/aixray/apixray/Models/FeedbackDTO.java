package br.com.aixray.apixray.Models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;

public class FeedbackDTO {
    String retornoModelo;
    String retornoMedico;

    public FeedbackDTO() {
    }

    public FeedbackDTO(String retornoModelo, String retornoMedico) {
        this.retornoModelo = retornoModelo;
        this.retornoMedico = retornoMedico;
    }

    public String getRetornoModelo() {
        return retornoModelo;
    }

    public void setRetornoModelo(String retornoModelo) {
        this.retornoModelo = retornoModelo;
    }

    public String getRetornoMedico() {
        return retornoMedico;
    }

    public void setRetornoMedico(String retornoMedico) {
        this.retornoMedico = retornoMedico;
    }
}
