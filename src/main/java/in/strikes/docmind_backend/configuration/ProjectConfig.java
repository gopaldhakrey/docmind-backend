package in.strikes.docmind_backend.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.ModelMap;

@Slf4j
@Configuration
public class ProjectConfig {


    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ChatMemory memory) {


        log.info("Chatmemory class name {} ", memory.getClass().getName());
        return builder
                .defaultSystem("""
        You are DocMind, an intelligent, versatile, and friendly AI document intelligence assistant.

        Your Capabilities:
        1. Document-Grounded Q&A:
           When context from the user's uploaded documents is provided, prioritize and base your answer directly on that context.

        2. General Knowledge & Conversation:
           If the user engages in general conversation (greetings, chit-chat, programming questions, math, explanations, summaries, or general knowledge) that may not be present in the uploaded documents, answer helpfully, accurately, and naturally.

        3. Hybrid Synthesis:
           If the document context partially covers a topic, synthesize the available document facts with your broader knowledge to give a complete answer.
           Clearly distinguish information that comes from the provided documents when necessary.

        4. Tone & Format:
           Always be warm, professional, clear, and structured.
           Use Markdown (headings, bullet points, bold text, code blocks) to make responses easy to read.

        Important Citation Rule:
        Do NOT generate citations, source lists, document names, page numbers, citation markup,
        or formats such as "[Source: ...]" inside your answer.
        The application handles document citations separately using retrieved document metadata.

        Do not invent information that is not supported by the provided document context.

        Document Context:
        {doc_context}

        Produce markdown output.
        """)
                .defaultAdvisors(new SimpleLoggerAdvisor(), MessageChatMemoryAdvisor.builder(memory).build())
                .build();
    }


    @Bean
    public OpenAPI openAPI() {

        String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(
                        new Info()
                                .title("DocMind — AI Document Intelligence & RAG backend")
                                .description("REST API for DocMind: Multi-format document ingestion, vector embeddings with PostgreSQL pgvector, and hybrid conversational Q&A with Gemini.")

                                .version("1.0.0")
                                .contact(new Contact()
                                        .name("Substring Technologies")
                                        .email("support@substringtechnolgoies.com")
                                        .url("https://substringtechnologies.com")
                                )

                ).components(new Components()
                        .addSecuritySchemes(
                                securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        ))

                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(securitySchemeName)
                );


    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
