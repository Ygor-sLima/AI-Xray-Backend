package br.com.aixray.apixray.Models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;

public class Feedback {
    @DynamoDBAttribute(attributeName = "retorno_modelo")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    String retornoModelo;
    @DynamoDBAttribute(attributeName = "retorno_medico")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    String retornoMedico;
    @DynamoDBAttribute(attributeName = "timestamp_inclusao")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.N)
    private Integer timestampInclusao;
    @DynamoDBAttribute(attributeName = "data_registro")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    private String dataRegistro;

    public Feedback() {
    }

    public Feedback(String retornoModelo, String retornoMedico, Integer timestampInclusao, String dataRegistro) {
        this.retornoModelo = retornoModelo;
        this.retornoMedico = retornoMedico;
        this.timestampInclusao = timestampInclusao;
        this.dataRegistro = dataRegistro;
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

    public Integer getTimestampInclusao() {
        return timestampInclusao;
    }

    public void setTimestampInclusao(Integer timestampInclusao) {
        this.timestampInclusao = timestampInclusao;
    }

    public String getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(String dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
}
