package com.fatec.assistentej.service;


import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fatec.assistentej.model.AvaliacaoResposta;
import com.fatec.assistentej.model.RagRespostaAvaliacaoRequest;

import dev.langchain4j.model.chat.ChatLanguageModel;

@Service
public class LLMJudgeService {

    private final ChatLanguageModel chatModel;
    private final ObjectMapper mapper = new ObjectMapper();

    public LLMJudgeService(ChatLanguageModel chatModel) {
        this.chatModel = chatModel;
    }

    public AvaliacaoResposta avaliarRagResposta(RagRespostaAvaliacaoRequest req) {
        String prompt = """
            Você é um avaliador especialista.

            Pergunta feita pelo usuário:
            %s

            Resposta gerada por um sistema RAG:
            %s

            Contexto que foi usado para gerar a resposta:
            %s

            Avalie a resposta segundo os critérios:
            {
                "responde": true ou false,
                "nota": número de 0 a 10,
                "correto": true ou false,
                "comentario": "explicação"
            }
            """.formatted(req.getPergunta(), req.getResposta(), req.getContextoRag());

        try {
            String respostaJson = chatModel.generate(prompt);
            return mapper.readValue(respostaJson, AvaliacaoResposta.class);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao avaliar resposta via LLM juiz", e);
        }
    }

}
