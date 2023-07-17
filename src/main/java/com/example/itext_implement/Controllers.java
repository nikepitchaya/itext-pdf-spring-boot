package com.example.itext_implement;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@CrossOrigin(origins = "http://localhost:3001")
@RestController
public class Controllers {
    // Pdf Generator for MCS007 (CIA)
    @PostMapping("/itext")
    public ResponseEntity<byte[]> Index(@RequestBody Model reqparams) {
        try {
            Document document = new Document();
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, output);
            CustomPageEvent event = new CustomPageEvent();
            document.setMargins(36, 36, 36, 36);
            writer.setPageEvent(event);

            document.open();

            BaseFont thaiFont = BaseFont.createFont("fonts/Helvethaica/DB Helvethaica X v3.2.ttf", BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED);
            Font font = new Font(thaiFont, 16, Font.NORMAL, BaseColor.BLACK);
            Paragraph title = new Paragraph("ใบเสร็จรับเงิน", font);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setHeaderRows(1); // Set the number of header rows
            addTableHeader(table, font);
            addRows(table, font, reqparams);
            document.add(table);

            Paragraph total = new Paragraph(
                    "รวมเป็นเงินทั้งสิ้น " + reqparams.getTotalPayamt() + " (" + reqparams.getTotalPayamtThai() + ")",
                    font);
            total.setAlignment(Element.ALIGN_LEFT);
            document.add(total);

            String dot = "............................................................................";
            Paragraph signatures = new Paragraph(
                    "จำนวนรายการ" + reqparams.getRowPayee().length + "เคลม" + "ลงชื่อผู้รับเอกสาร/ผู้นัด" + dot, font);
            signatures.setAlignment(Element.ALIGN_LEFT);
            document.add(signatures);

            document.newPage();
            Paragraph testP = new Paragraph("ปัน Pun ปั่น ปั้น ปุ้น" + "ปัน Pun ปั่น ปั้น ปุ้น", font);
            testP.setAlignment(Element.ALIGN_LEFT);
            document.add(testP);
            Paragraph testK = new Paragraph("ปัน Pun ปั่น ปั้น ปุ้น" + "ปัน Pun ปั่น ปั้น ปุ้น", font);
            testK.setAlignment(Element.ALIGN_LEFT);
            document.add(testK);

            document.close();

            byte[] pdfContent = output.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "receipt.pdf");
            headers.setCacheControl("must-revalidate, no-cache, no-store");

            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addTableHeader(PdfPTable table, Font thaiFont) {
        String[] headers = { "No.", "เลขที่เคลม", "จ่ายโดย", "จำนวนเงิน", "VAT", "จำนวนรวม",
                "ใบแจ้งหนี้/ใบกำกับ/หมายเหตุ" };
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, thaiFont));
            cell.setBackgroundColor(BaseColor.PINK);
            table.addCell(cell);
        }
    }

    private void addRows(PdfPTable table, Font thaiFont, Model reqparams) {
        for (String[] row : reqparams.getRowPayee()) {
            for (String cellData : row) {
                table.addCell(cellData);
            }
        }
        PdfPCell totalCell = new PdfPCell(new Phrase("รวมเป็นเงินทั้งสิ้น", thaiFont));
        totalCell.setColspan(4);
        table.addCell(totalCell);
        PdfPCell phaseTotal = new PdfPCell(new Phrase(reqparams.getTotalPayamt()));
        phaseTotal.setColspan(3);
        table.addCell(phaseTotal);
    }

    private class CustomPageEvent extends PdfPageEventHelper {
        private Image logo;
        private Font font;

        public CustomPageEvent() throws IOException, DocumentException {
            BaseFont thaiFont = BaseFont.createFont("fonts/Helvethaica/DB Helvethaica X v3.2.ttf", BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED);
            font = new Font(thaiFont, 16, Font.NORMAL, BaseColor.BLACK);
            // logo = Image.getInstance("E:\\Spring
            // boots\\itext_implement\\src\\main\\resources\\static\\Java_logo.png");
            // logo.scaleToFit(100, 100);
            // logo.setAlignment(Image.ALIGN_TOP);
        }

        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            int page = writer.getPageNumber();
            PdfContentByte canvas = writer.getDirectContent();
            int width = (int) document.getPageSize().getWidth();
            int height = (int) document.getPageSize().getHeight();
            ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, new Phrase(String.format("หน้า %d", page), font),
                    width - 36, height - 36, 0);
            if (page == 1)
                document.setMargins(36, 36, 130, 36);
            if (page > 1) {
                // float logoX = (document.getPageSize().getWidth() - 100) / 2;
                // float logoY = document.getPageSize().getHeight() - 100;
                // try {
                // canvas.addImage(logo, logo.getScaledWidth(), 0, 0, logo.getScaledHeight(),
                // logoX, logoY);
                // } catch (DocumentException e) {
                // throw new RuntimeException(e);
                // }
            }
        }
    }
}
