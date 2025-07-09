package com.fatec.easy_rag.controller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.easy_rag.service.AIService;
import com.fatec.easy_rag.service.PerguntaRequisicao;

@RestController
@RequestMapping("/rag")
public class RagController {

    private final AIService aiService; //injecao de dependencia
    private static final Logger logger = LogManager.getLogger(RagController.class);
    public RagController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/consultar")
    public String ask(@RequestBody PerguntaRequisicao request) {
    	 // --- Adicione este log para depuração ---
    	logger.info(">>>>>> Requisição recebida: " + request);
        if (request != null) {
            System.out.println("Pergunta extraída: " + request.getQuestion());
        } else {
            System.out.println("Objeto QuestionRequest é nulo!");
        }
        // --- Fim do log de depuração ---

        if (request == null || request.getQuestion() == null) {
            // Lidar com o caso de requisição ou pergunta nula
            return "Erro: A pergunta não foi fornecida corretamente no corpo da requisição JSON.";
        }

        return aiService.chatWithAssistant(request.getQuestion());
    }
}
