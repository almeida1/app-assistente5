package com.fatec.easy_rag;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fatec.easy_rag.service.Indexacao;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
@SpringBootTest
class Req01Indexacao {
	@Autowired
	OpenAiChatModel chatModel;	
	@Autowired
	OpenAiEmbeddingModel embeddingModel;

	@Test
	void ct01_valida_a_carga_do_documento() {
		String path = "e:/documents";
		Indexacao indexacao = new Indexacao();
		List<Document> arquivoCarregado = indexacao.carregaDocumento(path);
		assertFalse(arquivoCarregado.isEmpty());
	}
	@Test
	void ct02_valida_configuracao() {
		System.out.println(chatModel.modelName());
		System.out.println(embeddingModel.modelName());
		
	}
	
}
