package com.example.paintlab.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class AIService {

    @Value("${huggingface.api.url:https://api-inference.huggingface.co/models/mistralai/Mistral-7B-Instruct-v0.2}")
    private String hfApiUrl;

    @Value("${huggingface.api.token}")
    private String hfApiToken;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public AIService() {
        this.webClient = WebClient.builder().build();
        this.objectMapper = new ObjectMapper();
    }

    public Map<String, Object> analisarCorComIA(String hexCode) {
        log.info("🎯 Iniciando análise IA para cor: {}", hexCode);

        // Validação do HEX
        if (!isValidHex(hexCode)) {
            throw new IllegalArgumentException("Código HEX inválido: " + hexCode);
        }

        List<String> pigmentosDisponiveis = Arrays.asList(
                "Vermelho", "Amarelo", "Azul", "Branco", "Preto",
                "Ocre", "Verde", "Laranja", "Violeta", "Marrom"
        );

        try {
            Map<String, Double> proporcoes = calcularProporcoesPigmentos(hexCode, pigmentosDisponiveis);

            Map<String, Object> resultado = new HashMap<>();
            resultado.put("cor_analisada", hexCode);
            resultado.put("pigmentos", converterParaListaPigmentos(proporcoes));
            resultado.put("fonte", "IA");
            resultado.put("timestamp", new Date());

            log.info("✅ Análise IA concluída: {}", resultado);
            return resultado;

        } catch (Exception e) {
            log.error("❌ Erro na análise IA: {}", e.getMessage());
            return criarRespostaFallback(hexCode);
        }
    }

    private boolean isValidHex(String hexCode) {
        return hexCode != null && hexCode.matches("^#[0-9A-Fa-f]{6}$");
    }

    private Map<String, Double> calcularProporcoesPigmentos(String hexCode, List<String> pigmentosDisponiveis) {
        String prompt = criarPrompt(hexCode, pigmentosDisponiveis);

        try {
            log.info("🔧 Enviando prompt para IA...");

            String resposta = webClient.post()
                    .uri(hfApiUrl)
                    .header("Authorization", "Bearer " + hfApiToken)
                    .header("Content-Type", "application/json")
                    .bodyValue(criarCorpoRequisicao(prompt))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("🔧 Resposta bruta da IA: {}", resposta);
            return extrairProporcoesDaResposta(resposta, pigmentosDisponiveis);

        } catch (Exception e) {
            log.warn("⚠️ IA falhou, usando fallback: {}", e.getMessage());
            return calcularProporcoesFallback(hexCode, pigmentosDisponiveis);
        }
    }

    private String criarPrompt(String hexCode, List<String> pigmentosDisponiveis) {
        return """
            Como especialista em mistura de tintas, calcule as proporções exatas para reproduzir a cor %s.
            
            Pigmentos disponíveis: %s
            
            REGRAS:
            1. Some TOTALMENTE 100%% entre todos os pigmentos
            2. Use apenas os pigmentos listados acima
            3. Retorne APENAS JSON sem explicações
            4. Formato: {"Vermelho": 45.0, "Amarelo": 30.0, "Azul": 0.0, "Branco": 20.0, "Preto": 5.0}
            
            Resposta para %s:
            """.formatted(hexCode, String.join(", ", pigmentosDisponiveis), hexCode);
    }

    private Map<String, Object> criarCorpoRequisicao(String prompt) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("max_new_tokens", 150);
        parameters.put("temperature", 0.1);
        parameters.put("return_full_text", false);

        Map<String, Object> body = new HashMap<>();
        body.put("inputs", prompt);
        body.put("parameters", parameters);

        return body;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Double> extrairProporcoesDaResposta(String resposta, List<String> pigmentosDisponiveis) {
        try {
            // A resposta do Hugging Face é uma LISTA
            List<Map<String, Object>> responseList = objectMapper.readValue(resposta, List.class);
            String generatedText = (String) responseList.get(0).get("generated_text");

            log.info("🔧 Texto gerado pela IA: {}", generatedText);

            Pattern pattern = Pattern.compile("\\{.*\\}");
            Matcher matcher = pattern.matcher(generatedText);

            if (matcher.find()) {
                String jsonStr = matcher.group();
                log.info("🔧 JSON extraído: {}", jsonStr);

                Map<String, Double> proporcoes = objectMapper.readValue(jsonStr,
                        new TypeReference<Map<String, Double>>() {});
                return normalizarProporcoes(proporcoes);
            }
        } catch (Exception e) {
            log.error("❌ Erro ao parsear resposta da IA: {}", e.getMessage());
        }
        throw new RuntimeException("Não foi possível calcular as proporções");
    }

    private Map<String, Double> normalizarProporcoes(Map<String, Double> proporcoes) {
        double soma = proporcoes.values().stream().mapToDouble(Double::doubleValue).sum();
        log.info("🔧 Soma das proporções: {}", soma);

        if (Math.abs(soma - 100.0) < 0.1) return proporcoes;

        double fator = 100.0 / soma;
        Map<String, Double> normalizado = new HashMap<>();
        proporcoes.forEach((k, v) -> {
            double valorNormalizado = Math.round(v * fator * 10.0) / 10.0;
            normalizado.put(k, valorNormalizado);
        });

        // Garante que a soma seja exatamente 100
        double somaFinal = normalizado.values().stream().mapToDouble(Double::doubleValue).sum();
        if (Math.abs(somaFinal - 100.0) > 0.1) {
            double ajuste = 100.0 - somaFinal;
            String maiorPigmento = normalizado.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .get()
                    .getKey();
            normalizado.put(maiorPigmento, normalizado.get(maiorPigmento) + ajuste);
        }

        log.info("🔧 Proporções normalizadas: {}", normalizado);
        return normalizado;
    }

    private Map<String, Double> calcularProporcoesFallback(String hexCode, List<String> pigmentosDisponiveis) {
        try {
            // Converte HEX para RGB manualmente
            int r = Integer.parseInt(hexCode.substring(1, 3), 16);
            int g = Integer.parseInt(hexCode.substring(3, 5), 16);
            int b = Integer.parseInt(hexCode.substring(5, 7), 16);

            Map<String, Double> proporcoes = new HashMap<>();

            // Mapeamento básico RGB para pigmentos
            if (pigmentosDisponiveis.contains("Vermelho")) {
                proporcoes.put("Vermelho", Math.round((r / 255.0) * 100 * 10.0) / 10.0);
            }
            if (pigmentosDisponiveis.contains("Amarelo")) {
                proporcoes.put("Amarelo", Math.round((g / 255.0) * 100 * 10.0) / 10.0);
            }
            if (pigmentosDisponiveis.contains("Azul")) {
                proporcoes.put("Azul", Math.round((b / 255.0) * 100 * 10.0) / 10.0);
            }
            if (pigmentosDisponiveis.contains("Branco")) {
                proporcoes.put("Branco", 10.0);
            }

            log.info("🔧 Fallback calculado: {}", proporcoes);
            return normalizarProporcoes(proporcoes);

        } catch (Exception e) {
            log.error("❌ Erro no fallback: {}", e.getMessage());
            // Fallback extremo - distribuição igual
            Map<String, Double> fallback = new HashMap<>();
            pigmentosDisponiveis.stream().limit(4).forEach(pig -> fallback.put(pig, 25.0));
            return fallback;
        }
    }

    private List<Map<String, Object>> converterParaListaPigmentos(Map<String, Double> proporcoes) {
        List<Map<String, Object>> pigmentosList = new ArrayList<>();

        proporcoes.forEach((nome, proporcao) -> {
            Map<String, Object> pigmento = new HashMap<>();
            pigmento.put("nome", nome);
            pigmento.put("proporcao", Math.round(proporcao * 10.0) / 10.0); // 1 casa decimal
            pigmento.put("hex", obterHexPigmento(nome));
            pigmentosList.add(pigmento);
        });

        // Ordena por proporção (maior primeiro)
        pigmentosList.sort((a, b) ->
                Double.compare((Double) b.get("proporcao"), (Double) a.get("proporcao")));

        return pigmentosList;
    }

    private String obterHexPigmento(String nome) {
        Map<String, String> pigmentosHex = Map.of(
                "Vermelho", "#FF0000",
                "Amarelo", "#FFFF00",
                "Azul", "#0000FF",
                "Branco", "#FFFFFF",
                "Preto", "#000000",
                "Ocre", "#CC7722",
                "Verde", "#008000",
                "Laranja", "#FFA500",
                "Violeta", "#8F00FF",
                "Marrom", "#A52A2A"
        );
        return pigmentosHex.getOrDefault(nome, "#CCCCCC");
    }

    private Map<String, Object> criarRespostaFallback(String hexCode) {
        Map<String, Object> fallback = new HashMap<>();
        fallback.put("cor_analisada", hexCode);
        fallback.put("pigmentos", Arrays.asList(
                Map.of("nome", "Vermelho", "proporcao", 33.3, "hex", "#FF0000"),
                Map.of("nome", "Amarelo", "proporcao", 33.3, "hex", "#FFFF00"),
                Map.of("nome", "Azul", "proporcao", 33.3, "hex", "#0000FF")
        ));
        fallback.put("fonte", "FALLBACK");
        fallback.put("timestamp", new Date());
        return fallback;
    }
}