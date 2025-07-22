package cz.promtply.api.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import cz.promtply.shared.config.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmailRenderUtil {

    private static final Handlebars handlebars = new Handlebars();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String renderTemplate(String template, JsonNode model) throws Exception {
        Template renderedTemplate = handlebars.compileInline(template);

        Map<String, Object> context = mapper.convertValue(model, Map.class);

        return renderedTemplate.apply(context);
    }

    public static File renderHTMLtoPDF(String html) throws Exception {
        File tempFile = File.createTempFile("generated-", ".pdf");
        tempFile.deleteOnExit();

        try (OutputStream os = new FileOutputStream(tempFile)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFont(Constants.PDF_EXPORT_DEFAULT_FONT, "FallbackFont");
            builder.useDefaultPageSize(210, 297, PdfRendererBuilder.PageSizeUnits.MM); // A4 format
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();
        }

        return tempFile;
    }

}
