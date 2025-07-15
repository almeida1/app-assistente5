package com.fatec.assistentej.model;

public class RagRespostaAvaliacaoRequest {
    private String pergunta;
    private String resposta;
    private String contextoRag; // opcional: inclui o trecho dos chunks utilizados na resposta

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public String getContextoRag() {
        return contextoRag;
    }

    public void setContextoRag(String contextoRag) {
        this.contextoRag = contextoRag;
    }
}

