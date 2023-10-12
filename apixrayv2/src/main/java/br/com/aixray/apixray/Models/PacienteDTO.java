package br.com.aixray.apixray.Models;

public class PacienteDTO {
    private String nome;
    private Integer idade;
    private String genero;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public PacienteDTO(String nome, Integer idade, String genero) {
        this.nome = nome;
        this.idade = idade;
        this.genero = genero;
    }

    public PacienteDTO() {
    }
}
