package com.senla.aggregator.util;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

public final class SanitizerUtil {
    private static final PolicyFactory HTML_SANITIZER = new HtmlPolicyBuilder()
            .allowElements("p", "br", "div", "span", "hr", "h1", "h2", "h3", "h4", "h5", "h6")

            .allowUrlProtocols("http", "https", "data")
            .allowElements("img")
            .allowAttributes("src", "alt", "title").onElements("img")

            .allowAttributes("class", "id", "style").globally()

            .allowElements("a")
            .allowAttributes("href").onElements("a")
            .requireRelNofollowOnLinks()

            .allowElements("ul", "ol", "li", "strong", "em", "blockquote", "code", "pre")
            .toFactory();

    public static String convertMarkdownToSafeHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String rawHtml = renderer.render(document);

        return HTML_SANITIZER.sanitize(rawHtml);
    }

    private SanitizerUtil() {}
}
