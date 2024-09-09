package lk.informatics.ntc.view.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/showUpload")
public class PdfReportServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	String getContentType(String fileName) {
		String extension[] = { // File Extensions
				"txt", // 0 - plain text
				"htm", // 1 - hypertext
				"jpg", // 2 - JPEG image
				"png", // 2 - JPEG image
				"gif", // 3 - gif image
				"pdf", // 4 - adobe pdf
				"doc", // 5 - Microsoft Word
				"docx", "xls", "xlsx", }; // you can add more
		String mimeType[] = { // mime types
				"text/plain", // 0 - plain text
				"text/html", // 1 - hypertext
				"image/jpg", // 2 - image
				"image/jpg", // 2 - image
				"image/gif", // 3 - image
				"application/pdf", // 4 - Adobe pdf
				"application/msword", // 5 - Microsoft Word
				"application/msword", // 5 - Microsoft Word
				"application/vnd.ms-excel", }, // you can add more
				contentType = "text/html"; // default type
		// dot + file extension
		int dotPosition = fileName.lastIndexOf('.');
		// get file extension
		String fileExtension = fileName.substring(dotPosition + 1);
		// match mime type to extension
		for (int index = 0; index < mimeType.length; index++) {
			if (fileExtension.equalsIgnoreCase(extension[index])) {
				contentType = mimeType[index];
				break;
			}
		}
		return contentType;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String reportName =request.getParameter("reportName");
		if(reportName==null) {
			reportName ="NTC-Report";
		}
		try {
			byte[] content = (byte[]) request.getSession().getAttribute("reportBytes");
			String docType = (String) request.getSession().getAttribute("docType");
			if (!docType.contains(".")) {
				docType = "." + docType;
			}
			
			response.setContentType( "application/pdf" );
			response.setHeader("Content-disposition","attachment; filename=" +reportName+".pdf" );

			if (docType != null && docType.equalsIgnoreCase(".doc") || docType.equalsIgnoreCase(".docx")) {
				response.setContentType("application/msword");
				response.setHeader("Content-Disposition", "attachment; filename=" + "document.docx");
			}

			if (docType != null && docType.equalsIgnoreCase(".xlsx")) {
				response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				response.setHeader("Content-Disposition", "attachment; filename=" + "document.xlsx");
			}

			if (docType != null && docType.equalsIgnoreCase(".xls")) {
				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition", "attachment; filename=" + "document.xlsx");
			}
			
			if (docType != null && docType.equalsIgnoreCase(".png")) {
				response.setContentType("image/png");
				response.setHeader("Content-Disposition", "attachment; filename=" + "document.png");
			}
			
			if (docType != null && docType.equalsIgnoreCase(".jpg")) {
				response.setContentType("image/jpg");
				response.setHeader("Content-Disposition", "attachment; filename=" + "document.jpg");
			}
			
			if (docType != null && docType.equalsIgnoreCase(".jpeg")) {
				response.setContentType("image/jpeg");
				response.setHeader("Content-Disposition", "attachment; filename=" + "document.jpeg");
			}

			response.setContentLength(content.length);
			response.getOutputStream().write(content);
			request.getSession().removeAttribute("reportBytes");
			request.getSession().removeAttribute("docType");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
