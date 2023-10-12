package br.com.aixray.apixray.Models;


import br.com.aixray.apixray.Utils.FeedbackListConverter;
import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@DynamoDBTable(tableName = "exames")
public class Exame {

    @DynamoDBHashKey(attributeName = "id_exame")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    private String idExame;
    @DynamoDBAttribute(attributeName = "followUp")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.N)
    private Integer followUp;
    @DynamoDBAttribute(attributeName = "resultado")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    private String resultado;
    @DynamoDBAttribute(attributeName = "paciente")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.M)
    private Paciente paciente;
    @DynamoDBAttribute(attributeName = "imagem")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.M)
    private Imagem imagem;

    @DynamoDBAttribute(attributeName = "timestamp_inclusao")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.N)
    private Integer timestampInclusao;
    @DynamoDBAttribute(attributeName = "data_registro")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    private String dataRegistro;
    @DynamoDBAttribute(attributeName = "feedbacks")
    @DynamoDBTypeConverted(converter = FeedbackListConverter.class)
    private List<Feedback> feedbacks;

    public Exame() {
    }

    public Exame(String idExame, Integer followUp, String resultado, Paciente paciente, Imagem imagem, Integer timestampInclusao, String dataRegistro, ArrayList<Feedback> feedbacks) {
        this.idExame = idExame;
        this.followUp = followUp;
        this.resultado = resultado;
        this.paciente = paciente;
        this.imagem = imagem;
        this.timestampInclusao = timestampInclusao;
        this.dataRegistro = dataRegistro;
        this.feedbacks = feedbacks;
    }

    public Exame(String idExame, Integer followUp, String resultado, Paciente paciente, Imagem imagem) {
        this.idExame = idExame;
        this.followUp = followUp;
        this.resultado = resultado;
        this.paciente = paciente;
        this.imagem = imagem;
    }

    public String getIdExame() {
        return idExame;
    }

    public void setIdExame(String idExame) {
        this.idExame = idExame;
    }

    public Integer getFollowUp() {
        return followUp;
    }

    public void setFollowUp(Integer followUp) {
        this.followUp = followUp;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
    public Imagem getImagem() {
        return imagem;
    }

    public void setImagem(Imagem imagem) {
        this.imagem = imagem;
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

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public void novoFeedback(Feedback feedback) {
        if(this.feedbacks == null) {
            this.feedbacks = new ArrayList<Feedback>();
        }
        this.feedbacks.add(feedback);
    }

    public int countRightFeedbacks() {
        try {
            return (int) feedbacks.stream()
                    .filter(Objects::nonNull)
                    .filter(feedback -> feedback.getRetornoMedico().equals(feedback.getRetornoModelo()))
                    .count();
        }catch (NullPointerException exception) {
            return 0;
        }
    }
}