package com.fatec.easy_rag.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel; // Alterado de ChatLanguageModel para ChatModel
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import jakarta.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fatec.easy_rag.controller.RagController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class AIService {
	private static final Logger logger = LogManager.getLogger(AIService.class);
	private Assistente assistant; // A instância da sua interface Assistant
	private EmbeddingModel embeddingModel;
	private EmbeddingStore<TextSegment> embeddingStore;

	// O construtor injeta os componentes de baixo nível (ChatModel, EmbeddingModel,
	// EmbeddingStore)
	// que foram definidos como Beans na classe LangChain4jConfig.
	public AIService(ChatLanguageModel chatModel, // Alterado de ChatLanguageModel para ChatModel
			EmbeddingModel embeddingModel, // O EmbeddingModel injetado (OpenAiEmbeddingModel)
			EmbeddingStore<TextSegment> embeddingStore) {
		this.embeddingModel = embeddingModel;
		this.embeddingStore = embeddingStore;

		// *** AQUI É ONDE O AiServices É CONSTRUÍDO COM RAG E MEMÓRIA ***
		// Para RAG, você DEVE usar AiServices.builder() para configurar o
		// contentRetriever.
		this.assistant = AiServices.builder(Assistente.class).chatLanguageModel(chatModel) // Define o modelo de chat a
																							// ser usado
				.chatMemory(MessageWindowChatMemory.withMaxMessages(10)) // Adiciona memória de chat
				.contentRetriever(EmbeddingStoreContentRetriever.builder() // Configura o retriever para RAG
						.embeddingStore(embeddingStore) // Usa o EmbeddingStore para buscar
						.embeddingModel(embeddingModel) // Usa o EmbeddingModel para embeddar a pergunta
						.maxResults(2) // Busca os 2 chunks mais relevantes
						.minScore(0.7) // Score mínimo de similaridade
						.build())
				.build();
	}

	// Método executado após a injeção de dependências, para carregar e ingerir
	// documentos.
	@PostConstruct
	public void init() {
		try {
			logger.info(">>>>>> Carrega documento.");
			loadAndIngestDocuments();
		} catch (IOException e) {
			System.err.println("Erro ao carregar e ingerir documentos: " + e.getMessage());
		}
	}

	// Carrega documentos de uma pasta e os processa para RAG.
	private void loadAndIngestDocuments() throws IOException {
		Path documentsPath = Paths.get("e:/documents");
		logger.info(">>>>>> Obtem o path do arquivo => " + documentsPath.toString());
		if (!Files.exists(documentsPath)) {
			Files.createDirectories(documentsPath);
			logger.info(">>>>>> Diretório de documentos criado: " + documentsPath);
			Files.writeString(documentsPath.resolve("exemplo.txt"),
					"O céu é azul e o mar é profundo. O sol brilha forte. A Terra é um planeta maravilhoso. A capital do Brasil é Brasília.");
			System.out.println("Arquivo de exemplo 'exemplo.txt' criado.");
		}
		logger.info(">>>>>> O path existe.");
		List<Document> documents = FileSystemDocumentLoader.loadDocuments(documentsPath);
		var documentSplitter = DocumentSplitters.recursive(500, 0);
		List<TextSegment> segments = documentSplitter.splitAll(documents);

		List<Embedding> embeddings = embeddingModel.embedAll(segments).content();
		embeddingStore.addAll(embeddings, segments);
		logger.info(
				"Documentos carregados e embeddings criados/armazenados. Total de segmentos: " + segments.size());
	}

	// Método público para interagir com o assistente.
	public String chatWithAssistant(String userMessage) {
		return assistant.chat(userMessage);
	}
}