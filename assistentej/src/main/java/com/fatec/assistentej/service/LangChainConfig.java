package com.fatec.assistentej.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ChatModelProperties.class, EmbeddingModelProperties.class})
public class LangChainConfig {

    private static final Logger logger = LogManager.getLogger(LangChainConfig.class);

    @Bean
    public ChatLanguageModel chatLanguageModel(ChatModelProperties props) {
        logger.info("🔧 Configurando modelo de chat OpenAI");
        logger.info("🔑 Modelo: {}, 🎯 Temperatura: {}", props.getModelName(), props.getTemperature());

        return OpenAiChatModel.builder()
                .apiKey(props.getApiKey())
                .modelName(props.getModelName())
                .temperature(props.getTemperature())
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean
    public EmbeddingModel embeddingModel(EmbeddingModelProperties props) {
        logger.info("🔧 Configurando modelo de embedding OpenAI");
        logger.info("🔑 Modelo: {}", props.getModelName());

        return OpenAiEmbeddingModel.builder()
                .apiKey(props.getApiKey())
                .modelName(props.getModelName())
                .build();
    }
}
