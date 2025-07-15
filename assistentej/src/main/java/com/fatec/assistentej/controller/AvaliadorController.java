package com.fatec.assistentej.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fatec.assistentej.model.AvaliacaoResposta;
import com.fatec.assistentej.model.RagRespostaAvaliacaoRequest;
import com.fatec.assistentej.service.LLMJudgeService;

@RestController
@RequestMapping("/api/julgamento")
public class AvaliadorController {

    private final LLMJudgeService judgeService;

    public AvaliadorController(LLMJudgeService judgeService) {
        this.judgeService = judgeService;
    }

    @PostMapping("/avaliar-rag")
    public AvaliacaoResposta avaliar(@RequestBody RagRespostaAvaliacaoRequest request) {
        return judgeService.avaliarRagResposta(request);
    }
}
