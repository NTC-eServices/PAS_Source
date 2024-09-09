package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@ManagedBean(name = "imageUploadSessionBean")
@SessionScoped 
public class ImageUploadSessionBean {

	
	private UploadedFile file;
	
	
	 
	
	 
	 
	public StreamedContent handleFileUpload() {
		 if (file == null) {
		        return new DefaultStreamedContent(); 
		 } else {
		        return new DefaultStreamedContent(new ByteArrayInputStream(file.getContents()), "image/png"); 
		 }
	}   
	 
	
	public StreamedContent getImage() {
	    if (file == null) {
	        return new DefaultStreamedContent(); 
	    } else {
	    	 
	        return new DefaultStreamedContent(new ByteArrayInputStream(file.getContents()), "image/png"); 
	    }
}


	public UploadedFile getFile() {
		return file;
	}


	public void setFile(UploadedFile file) {
		this.file = file;
	}   
	
	
	 
}
