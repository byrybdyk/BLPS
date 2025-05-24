package com.zarubovandlevchenko.lb1.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.zarubovandlevchenko.lb1.repository.dbcard.CardRepository;
import com.zarubovandlevchenko.lb1.repository.dbuser.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    public void generateReport(){
        long userCount = getRegisteredUserCount();
        long cardCounter = getIssuedCardCount();

        String filename = "reports/report_" + LocalDateTime.now().toString().replace(":", "-") + ".pdf";
        File pdfFile = new File(filename);
        pdfFile.getParentFile().mkdirs();

        try (PdfWriter writer = new PdfWriter(filename);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            document.add(new Paragraph("Report"));
            document.add(new Paragraph("Date " + LocalDateTime.now()));
            document.add(new Paragraph("Register User " + userCount));
            document.add(new Paragraph("Given card " + cardCounter));
            document.close();
            System.out.println("Отчёт сгенерирован: " + filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private long getRegisteredUserCount() {
        return 123;
    }

    private long getIssuedCardCount() {
        return 456;
    }
}
