package com.shopme.admin.user.export;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

public class UserPdfExporter extends AbstractExporter {

	public void export(List<User> listallusers, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "application/pdf", ".pdf");

		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());

		document.open();

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.BLUE);

		Paragraph paragraph = new Paragraph("List Of Users", font);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(paragraph);

		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100f);
		table.setSpacingBefore(10);
		table.setWidths(new float[] { 1.2f, 3.5f, 3.0f, 3.0f, 3.0f, 1.7f});
 
		WriteTableHeader(table);
		WrietTableData(table, listallusers);

		document.add(table);
		document.close();
	}

	private void WrietTableData(PdfPTable table, List<User> listallusers) {

		for (User user : listallusers) {
			table.addCell(String.valueOf(user.getId()));
			table.addCell(String.valueOf(user.getEmail()));
			table.addCell(String.valueOf(user.getFirstname()));
			table.addCell(String.valueOf(user.getLastname()));
			table.addCell(String.valueOf(user.getRoles()));
			table.addCell(String.valueOf(user.isEnabled()));
		}

	}

	private void WriteTableHeader(PdfPTable table) {

		PdfPCell cell = new PdfPCell();
		cell.setPadding(5);

		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.BLACK);

		cell.setPhrase(new Phrase("ID", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("E-Mail", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("FirstName", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("LastName", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Roles", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Enabled", font));
		table.addCell(cell);

	}

}
