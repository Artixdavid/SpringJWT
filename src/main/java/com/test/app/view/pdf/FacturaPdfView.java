package com.test.app.view.pdf;

import java.awt.Color;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.filters.AddDefaultCharsetFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.test.app.models.entity.Factura;
import com.test.app.models.entity.ItemFactura;

/*
 * Debe implementar la interfaz VIEW y RENDER
 * para poder renderizar el contenido
 * */

@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		Factura factura = (Factura) model.get("factura");

		// numero de columnas que creara
		PdfPTable tabla = new PdfPTable(1);
		tabla.setSpacingAfter(20);

		PdfPCell titulo = new PdfPCell(new Phrase("Datos del cliente"));
		titulo.setBackgroundColor(new Color(186, 232, 238));
		titulo.setPadding(8f);
		tabla.addCell(titulo);

		tabla.addCell(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
		tabla.addCell(factura.getCliente().getEmail());

		PdfPTable tabla2 = new PdfPTable(1);
		tabla2.setSpacingAfter(20);

		PdfPCell tituloFactura = new PdfPCell(new Phrase("Datos de la Factura"));
		tituloFactura.setBackgroundColor(new Color(186, 238, 207));
		tituloFactura.setPadding(8f);
		tabla2.addCell(tituloFactura);

		tabla2.addCell("Folio: " + factura.getId());
		tabla2.addCell(
				"Descripcion: " + factura.getDescripcion() == "" || factura.getDescripcion() == "" ? "Sin descripcion"
						: factura.getDescripcion());
		tabla2.addCell("Fecha: " + factura.getCreateAt());

		document.add(tabla);
		document.add(tabla2);

		PdfPTable tabla3 = new PdfPTable(4);
		tabla3.setWidths(new float[] { 3.5f, 1, 1, 1 });
		tabla3.addCell("Producto");
		tabla3.addCell("Precio");
		tabla3.addCell("Cantidad");
		
		tabla3.addCell("Total");

		for (ItemFactura item : factura.getItems()) {
			tabla3.addCell(item.getProducto().getNombre());
			tabla3.addCell(item.getProducto().getPrecio().toString());
			//tabla3.addCell(item.getCantidad().toString());
			
			PdfPCell cantidad = new PdfPCell(new Phrase(item.getCantidad().toString()));
			cantidad.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			tabla3.addCell(cantidad);
			
			tabla3.addCell(item.calcularImporte().toString());
		}

		PdfPCell cell = new PdfPCell(new Phrase("Total: "));
		cell.setColspan(3);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		tabla3.addCell(cell);
		tabla3.addCell(factura.getTotal().toString());

		document.add(tabla3);

	}

}
