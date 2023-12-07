package br.com.aixray.apixray.Models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;

public class Paciente {
    @DynamoDBAttribute(attributeName = "idPaciente")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    private String idPaciente;
    @DynamoDBAttribute(attributeName = "nomePaciente")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    private String nomePaciente;
    @DynamoDBAttribute(attributeName = "emailPaciente")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    private String emailPaciente;
    @DynamoDBAttribute(attributeName = "idadePaciente")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.N)
    private Integer idadePaciente;
    @DynamoDBAttribute(attributeName = "generoPaciente")
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    private String generoPaciente;

    public Paciente() {
    }

    public Paciente(String idPaciente, String nomePaciente, String emailPaciente, String generoPaciente, Integer idadePaciente) {
        this.idPaciente = idPaciente;
        this.nomePaciente = nomePaciente;
        this.emailPaciente = emailPaciente;
        this.generoPaciente = generoPaciente;
        this.idadePaciente = idadePaciente;
    }

    public String getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(String idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public String getEmailPaciente() {
        return emailPaciente;
    }

    public void setEmailPaciente(String emailPaciente) {
        this.emailPaciente = emailPaciente;
    }

    public String getGeneroPaciente() {
        return generoPaciente;
    }

    public void setGeneroPaciente(String generoPaciente) {
        this.generoPaciente = generoPaciente;
    }

    public Integer getIdadePaciente() {
        return idadePaciente;
    }

    public void setIdadePaciente(Integer idadePaciente) {
        this.idadePaciente = idadePaciente;
    }

    @Override
    public String toString() {
        return "Paciente{" +
                "idPaciente=" + idPaciente +
                ", nomePaciente='" + nomePaciente + '\'' +
                ", emailPaciente='" + emailPaciente + '\'' +
                ", idadePaciente=" + idadePaciente +
                ", generoPaciente='" + generoPaciente + '\'' +
                '}';
    }

    public String faixaEtaria() {
        if (idadePaciente >= 0 && idadePaciente <= 12) {
            return "Crianças (0 a 12)";
        } else if (idadePaciente >= 13 && idadePaciente <= 18) {
            return "Adolescentes (13 a 18)";
        } else if (idadePaciente >= 19 && idadePaciente <= 35) {
            return "Adultos Jovens (19 a 35)";
        } else if (idadePaciente >= 36 && idadePaciente <= 60) {
            return "Adultos de Meia-Idade (36 a 60)";
        } else {
            return "Idosos (Acima de 60)";
        }
    }

}
