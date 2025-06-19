package com.senla.aggregator.service;

import com.senla.aggregator.service.minio.MinioService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class MarkdownService {

    private final MinioService minioService;

    @Value("${minio.external-server-url}")
    private String externalServerUrl;

    private Pattern tempImagePattern;

    private static final PolicyFactory HTML_SANITIZER = new HtmlPolicyBuilder()
            // Разрешаем базовые HTML-теги
            .allowElements("p", "br", "div", "span", "hr", "h1", "h2", "h3", "h4", "h5", "h6")

            // Разрешаем изображения, но только с безопасными URL
            .allowUrlProtocols("http", "https", "data")
            .allowElements("img")
            .allowAttributes("src", "alt", "title").onElements("img")

            // Разрешаем атрибуты class, id, style для всех тегов
            .allowAttributes("class", "id", "style").globally()

            // Разрешаем ссылки, но только с безопасными URL
            .allowElements("a")
            .allowAttributes("href").onElements("a")
            .requireRelNofollowOnLinks()

            // Дополнительные разрешения (если нужно)
            .allowElements("ul", "ol", "li", "strong", "em", "blockquote", "code", "pre")
            .toFactory();

    @PostConstruct
    public void init() {
        String pattern = "!\\[.*?]\\(" + externalServerUrl + "/images/reviews/temp/([^)]+)\\)";
        tempImagePattern = Pattern.compile(pattern);
    }

    private String processMarkdownImages(String markdown) {
        if (markdown == null || markdown.isBlank()) {
            return markdown;
        }

        Matcher matcher = tempImagePattern.matcher(markdown);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            // Полный путь к временному файлу
            String tempPath = externalServerUrl + "/images/reviews/temp/" + matcher.group(1);

            // Копируем в постоянное хранилище
            String newPath = minioService.copyImageFromTemp(tempPath);

            // Заменяем в Markdown
            String replacement = "![" + matcher.group(1) + "](" + newPath + ")";
            matcher.appendReplacement(result, replacement);
        }

        matcher.appendTail(result);
        return result.toString();
    }

    public String markdownToSafeHtml(String markdown) {
        if (markdown == null || markdown.isBlank()) {
            return "";
        }

        String processedMarkdown = processMarkdownImages(markdown);
        String rawHtml = parseMarkdownToHtml(processedMarkdown);

        return sanitizeHtml(rawHtml);
    }

    private String parseMarkdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    private String sanitizeHtml(String rawHtml) {
        return HTML_SANITIZER.sanitize(rawHtml);
    }
}
