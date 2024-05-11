package io.whistler.docxwar;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.IOException;

@WebServlet(urlPatterns = "/generate")
public class GenerateDocument extends HttpServlet {
    @Override
    public void doGet(final HttpServletRequest req,
                      final HttpServletResponse res) throws ServletException, IOException {

        try {
            final var wordPackage = WordprocessingMLPackage.createPackage();
            final var mainDocumentPart = wordPackage.getMainDocumentPart();
            mainDocumentPart.addStyledParagraphOfText("Title", "Hello World!");
            mainDocumentPart.addParagraphOfText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse euismod.");

            res.setHeader("Content-Disposition", "attachment;filename=bogus.docx");
            res.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");

            try (final var outStream = res.getOutputStream()) {
                wordPackage.save(outStream);
            }
        } catch (final Docx4JException e) {
            throw new ServletException("Can't generate DOCX-File", e);
        }

    }
}
