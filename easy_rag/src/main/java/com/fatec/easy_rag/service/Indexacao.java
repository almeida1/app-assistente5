package com.fatec.easy_rag.service;

import java.util.List;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

public class Indexacao {
	
	/*
	 * A biblioteca Apache Tika, que suporta uma varidade de tipos de documentos, é usada para
	 * detectar o tipo de documento e analisa-lo (parse). Essa dependencia é carregada
	 * no starter project do spring boot langchain4j.
	 * É possivel tambem filtrar os documentos:
	 * PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:*.pdf");
     * List<Document> documents = FileSystemDocumentLoader.loadDocuments("/home/langchain4j/documentation", pathMatcher);
	 */
	
	 public List<Document> carregaDocumento(String path) {
		 //carrega todos os documentos de um diretorio especifico
		 List<Document> documents = FileSystemDocumentLoader.loadDocuments(path);
		 return documents;
	 }
	 /*
	  * pre-processa o arquivo para armazenar a informação em um banco de dados vetorial
	  * neste exemplo o banco de dados vetorial esta na memoria
	  */
	 
	 public void repositorioEmbedding(List<Document> documents) {
		 InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
		 EmbeddingStoreIngestor.ingest(documents, embeddingStore);
	 }

}
