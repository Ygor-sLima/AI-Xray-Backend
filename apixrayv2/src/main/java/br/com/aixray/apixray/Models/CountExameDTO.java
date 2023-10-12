package br.com.aixray.apixray.Models;

public class CountExameDTO {
    private String generoPaciente;
    private String dataRegistro;
    private Integer count;

    public CountExameDTO() {
    }

    public CountExameDTO(String generoPaciente, String dataRegistro, Integer count) {
        this.generoPaciente = generoPaciente;
        this.dataRegistro = dataRegistro;
        this.count = count;
    }

    public String getGeneroPaciente() {
        return generoPaciente;
    }

    public void setGeneroPaciente(String generoPaciente) {
        this.generoPaciente = generoPaciente;
    }

    public String getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(String dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
