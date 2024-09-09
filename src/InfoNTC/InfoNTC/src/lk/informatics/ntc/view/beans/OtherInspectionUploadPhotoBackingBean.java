package lk.informatics.ntc.view.beans;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Base64;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import lk.informatics.ntc.model.dto.ParamerDTO;
import lk.informatics.ntc.model.dto.UploadImageDTO;
import lk.informatics.ntc.model.dto.VehicleInspectionDTO;
import lk.informatics.ntc.model.exception.ApplicationException;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.InspectionActionPointService;
import lk.informatics.ntc.model.service.MigratedService;
import lk.informatics.ntc.model.service.PaymentVoucherService;
import lk.informatics.ntc.model.service.VehicleInspectionService;
import lk.informatics.ntc.view.util.PropertyReader;
import lk.informatics.ntc.view.util.SpringApplicationContex;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@ManagedBean(name = "otherUploadPhotosBackingBean")
@ViewScoped
public class OtherInspectionUploadPhotoBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Logger logger = Logger.getLogger("UploadPhotosBackingBean");

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private StreamedContent permitOwner;
	private DefaultStreamedContent permtOwner;
	private DefaultStreamedContent firstVehicleImage;
	private DefaultStreamedContent secondVehicleImage;
	private DefaultStreamedContent thirdVehicleImage;
	private DefaultStreamedContent fourthVehicleImage;
	private DefaultStreamedContent fifthVehicleImage;
	private DefaultStreamedContent sixthVehicleImage;
	private byte[] permitOwnerFaceImage;
	private byte[] firstVehicleImg;
	private byte[] secondVehicleImg;
	private byte[] thirdVehicleImg;
	private byte[] fourthVehicleImg;
	private byte[] fifthVehicleImg;
	private byte[] sixthVehicleImg;
	private String ownerPhotoName;
	private InputStream ownerPhotoStream;
	private String ownerPhotoPath;
	private String vehiclePhotoName;
	private InputStream vehiclePhotoStream;
	private UploadedFile uploadedFile;
	private UploadImageDTO uploadImageDTO;
	private UploadImageDTO deleteUploadImagesDTO;
	private boolean ownerImageUpload, backBtn;
	private String vehicleNo;
	private String applicationNo;
	private String vehicleOwnerName;
	private String vehicleInspection;
	private VehicleInspectionService vehicleInspectionService;
	private CommonService commonService;
	private MigratedService migratedService;
	private boolean photosSaveSuccess;
	private String tempQueueNo;
	private boolean mainInsCancel;
	private boolean viewInsCancel;
	private PaymentVoucherService paymentVoucherService;
	private VehicleInspectionDTO taskDetWithAppDetDTO;
	private InspectionActionPointService inspectionActionPointService;
	private boolean disableSave = false;
	private UploadImageDTO uploadTempDTO = new UploadImageDTO();

	@PostConstruct
	public void init() {

		loadData();

		if (sessionBackingBean.isClicked == true) {
			backBtn = true;
		}
	}

	public void clearSessionData() {
		FacesContext fcontext = FacesContext.getCurrentInstance();
		fcontext.getExternalContext().getSessionMap().remove("VEHICLE_NO");
		fcontext.getExternalContext().getSessionMap().remove("APPLICATION_NO");
		fcontext.getExternalContext().getSessionMap().remove("OWNER_NAME");
		fcontext.getExternalContext().getSessionMap().remove("QUEUE_NO");
		fcontext.getExternalContext().getSessionMap().remove("OTHER_VEHICLE_INSPECTION");
		fcontext.getExternalContext().getSessionMap().remove("UPLOAD_VIEW");
		fcontext.getExternalContext().getSessionMap().remove("INSPECTION_TYPE");
		fcontext.getExternalContext().getSessionMap().put("UPLOAD_PHOTO_REDIRECT", uploadTempDTO);

	}

	public void loadData() {
		vehicleInspectionService = (VehicleInspectionService) SpringApplicationContex
				.getBean("vehicleInspectionService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");
		migratedService = (MigratedService) SpringApplicationContex.getBean("migratedService");
		paymentVoucherService = (PaymentVoucherService) SpringApplicationContex.getBean("paymentVoucherService");
		inspectionActionPointService = (InspectionActionPointService) SpringApplicationContex
				.getBean("inspectionActionPointService");
		uploadImageDTO = new UploadImageDTO();
		deleteUploadImagesDTO = new UploadImageDTO();
		ownerImageUpload = false;
		photosSaveSuccess = false;
		tempQueueNo = null;

		mainInsCancel = false;
		viewInsCancel = false;

		taskDetWithAppDetDTO = new VehicleInspectionDTO();

		FacesContext fcontext = FacesContext.getCurrentInstance();
		Object objCallerVehicleNo = fcontext.getExternalContext().getSessionMap().get("VEHICLE_NO");
		if (objCallerVehicleNo != null) {
			vehicleNo = (String) objCallerVehicleNo;
		}

		Object objCallerApplicationNo = fcontext.getExternalContext().getSessionMap().get("APPLICATION_NO");
		if (objCallerApplicationNo != null) {
			applicationNo = (String) objCallerApplicationNo;
			uploadTempDTO.setApplicationNo(applicationNo);
		}

		Object objCallerOwnerName = fcontext.getExternalContext().getSessionMap().get("OWNER_NAME");
		if (objCallerOwnerName != null) {
			vehicleOwnerName = (String) objCallerOwnerName;
		}

		Object objCallerQueueNo = fcontext.getExternalContext().getSessionMap().get("QUEUE_NO");
		if (objCallerQueueNo != null) {
			tempQueueNo = (String) objCallerQueueNo;
			uploadTempDTO.setQueueNo(tempQueueNo);

		}

		Object objCallerVehicleIns = fcontext.getExternalContext().getSessionMap().get("OTHER_VEHICLE_INSPECTION");
		if (objCallerVehicleIns != null) {
			vehicleInspection = (String) objCallerVehicleIns;

			if (vehicleInspection != null && !vehicleInspection.isEmpty()
					&& !vehicleInspection.trim().equalsIgnoreCase("")) {

				if (vehicleInspection.equalsIgnoreCase("main_other")) {
					viewInsCancel = true;
				}
				if (vehicleInspection.equalsIgnoreCase("view_other")) {
					viewInsCancel = true;

					Object modeObject = fcontext.getExternalContext().getSessionMap().get("UPLOAD_VIEW");

					if (modeObject != null) {
						String value = (String) modeObject;

						if (value.equals("true")) {
							disableSave = true;
						}

					}

				}
			}
		}

		uploadImageDTO.setVehicleNo(vehicleNo);
		uploadImageDTO.setVehicleOwnerName(vehicleOwnerName);
		uploadImageDTO.setApplicationNo(applicationNo);

		loadDefaultImages();

		if (vehicleNo != null && !vehicleNo.isEmpty() && !vehicleNo.trim().equalsIgnoreCase("") && applicationNo != null
				&& !applicationNo.isEmpty() && !applicationNo.trim().equalsIgnoreCase("")) {

			uploadImageDTO = vehicleInspectionService
					.findVehicleOwnerByFormerApplicationNo(uploadImageDTO.getVehicleNo());

			BufferedImage personImage;
			UploadImageDTO renewalUploadedDTO = new UploadImageDTO();
			try {
				if (!applicationNo.equalsIgnoreCase(uploadImageDTO.getApplicationNo())) {
					if (uploadImageDTO != null && uploadImageDTO.getVehicleOwnerPhotoPath() != null
							&& !uploadImageDTO.getVehicleOwnerPhotoPath().isEmpty()
							&& !uploadImageDTO.getVehicleOwnerPhotoPath().trim().equalsIgnoreCase("")) {

						personImage = ImageIO.read(new File(uploadImageDTO.getVehicleOwnerPhotoPath()));
						BufferedImage resized = resize(personImage, 500, 500);
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						ImageIO.write(resized, "png", bos);
						permitOwnerFaceImage = bos.toByteArray();

						if (uploadImageDTO != null && uploadImageDTO.getFirstVehiImagePath() != null
								&& !uploadImageDTO.getFirstVehiImagePath().isEmpty()
								&& !uploadImageDTO.getFirstVehiImagePath().trim().equalsIgnoreCase("")) {

							BufferedImage firstVehicleImg1 = ImageIO
									.read(new File(uploadImageDTO.getFirstVehiImagePath()));
							BufferedImage resized1 = resize(firstVehicleImg1, 500, 500);
							ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
							ImageIO.write(resized1, "png", bos1);
							firstVehicleImg = bos1.toByteArray();
						}

						if (uploadImageDTO != null && uploadImageDTO.getSecondVehiImagePath() != null
								&& !uploadImageDTO.getSecondVehiImagePath().isEmpty()
								&& !uploadImageDTO.getSecondVehiImagePath().trim().equalsIgnoreCase("")) {

							BufferedImage vehicleImg2 = ImageIO.read(new File(uploadImageDTO.getSecondVehiImagePath()));
							BufferedImage resized2 = resize(vehicleImg2, 500, 500);
							ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
							ImageIO.write(resized2, "png", bos2);
							secondVehicleImg = bos2.toByteArray();
						}

						if (uploadImageDTO != null && uploadImageDTO.getThirdVehiImagePath() != null
								&& !uploadImageDTO.getThirdVehiImagePath().isEmpty()
								&& !uploadImageDTO.getThirdVehiImagePath().trim().equalsIgnoreCase("")) {

							BufferedImage vehicleImg3 = ImageIO.read(new File(uploadImageDTO.getThirdVehiImagePath()));
							BufferedImage resized3 = resize(vehicleImg3, 500, 500);
							ByteArrayOutputStream bos3 = new ByteArrayOutputStream();
							ImageIO.write(resized3, "png", bos3);
							thirdVehicleImg = bos3.toByteArray();
						}

						if (uploadImageDTO != null && uploadImageDTO.getForthVehiImagePath() != null
								&& !uploadImageDTO.getForthVehiImagePath().isEmpty()
								&& !uploadImageDTO.getForthVehiImagePath().trim().equalsIgnoreCase("")) {

							BufferedImage vehicleImg4 = ImageIO.read(new File(uploadImageDTO.getForthVehiImagePath()));
							BufferedImage resized4 = resize(vehicleImg4, 500, 500);
							ByteArrayOutputStream bos4 = new ByteArrayOutputStream();
							ImageIO.write(resized4, "png", bos4);
							fourthVehicleImg = bos4.toByteArray();
						}

						if (uploadImageDTO != null && uploadImageDTO.getFifthVehiImagePath() != null
								&& !uploadImageDTO.getFifthVehiImagePath().isEmpty()
								&& !uploadImageDTO.getFifthVehiImagePath().trim().equalsIgnoreCase("")) {

							BufferedImage vehicleImg5 = ImageIO.read(new File(uploadImageDTO.getFifthVehiImagePath()));
							BufferedImage resized5 = resize(vehicleImg5, 500, 500);
							ByteArrayOutputStream bos5 = new ByteArrayOutputStream();
							ImageIO.write(resized5, "png", bos5);
							fifthVehicleImg = bos5.toByteArray();
						}

						if (uploadImageDTO != null && uploadImageDTO.getSixthVehiImagePath() != null
								&& !uploadImageDTO.getSixthVehiImagePath().isEmpty()
								&& !uploadImageDTO.getSixthVehiImagePath().trim().equalsIgnoreCase("")) {

							BufferedImage vehicleImg6 = ImageIO.read(new File(uploadImageDTO.getSixthVehiImagePath()));
							BufferedImage resized6 = resize(vehicleImg6, 500, 500);
							ByteArrayOutputStream bos6 = new ByteArrayOutputStream();
							ImageIO.write(resized6, "png", bos6);
							sixthVehicleImg = bos6.toByteArray();
						}

						renewalUploadedDTO = renewalVehicleOwnerDataSave(uploadImageDTO.getVehicleOwnerPhotoPath(),
								applicationNo);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			String status = vehicleInspectionService.checkStatusFromApplicationNumber(applicationNo);

			if (status != null && !status.isEmpty() && !status.trim().equalsIgnoreCase("")
					&& status.trim().equalsIgnoreCase("N")) {
				if (renewalUploadedDTO != null) {
					vehicleInspectionService.insertVehicleUploadPhotosToNT_T_OWNER_VEHI_IMAGES(renewalUploadedDTO,
							sessionBackingBean.getLoginUser(), "N");
				} else {
					vehicleInspectionService.insertVehicleUploadPhotosToNT_T_OWNER_VEHI_IMAGES(uploadImageDTO,
							sessionBackingBean.getLoginUser(), "N");
				}
			} else {
				searchDataByVehicleNumber();
			}
			/** check whether status is "N" end **/

		}
		RequestContext.getCurrentInstance().update("form");

	}

	public void uploadAction() {
		try {

			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/InfoNTC/pages/vehicleInspectionSet/uploadPhotos.xhtml");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void backToRenewals() {

		try {

			sessionBackingBean.setApproveURLStatus(false);
			sessionBackingBean.isClicked = true;

			if ((vehicleInspection != null && !vehicleInspection.isEmpty()
					&& !vehicleInspection.trim().equalsIgnoreCase("")
					&& vehicleInspection.trim().equalsIgnoreCase("main_other"))) {

				clearSessionData();
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/InfoNTC/pages/vehicleInspectionSet/otherVehicleInspection.xhtml");

			} else if ((vehicleInspection != null && !vehicleInspection.isEmpty()
					&& !vehicleInspection.trim().equalsIgnoreCase("")
					&& vehicleInspection.trim().equalsIgnoreCase("view_other"))) {

				clearSessionData();
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/InfoNTC/pages/vehicleInspectionSet/viewOtherInspection.xhtml");

			}

		} catch (IOException e) {

			e.printStackTrace();
		}

		setBackBtn(false);
		RequestContext.getCurrentInstance().update("btnBack");
	}

	public void loadDefaultImages() {

		try {
			Properties props = PropertyReader.loadPropertyFile();
			String ownerPath = props.getProperty("vehicle.inspection.upload.photo.owner.default.path");
			String busPath = props.getProperty("vehicle.inspection.upload.photo.bus.default.path");

			/** owner photo default start **/
			BufferedImage personImage = ImageIO.read(new File(ownerPath));
			BufferedImage resized = resize(personImage, 500, 500);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(resized, "png", bos);
			permitOwnerFaceImage = bos.toByteArray();

			File file = new File(ownerPath);
			String mimeType;
			if (file.getName().endsWith("png")) {
				mimeType = "image/png";
			} else if (file.getName().endsWith("jpg") || file.getName().endsWith("jpeg")) {
				mimeType = "image/jpeg";
			} else if (file.getName().endsWith("gif")) {
				mimeType = "image/gif";
			} else {
				mimeType = "application/octet-stream";
			}

			permtOwner = new DefaultStreamedContent(new FileInputStream(file), mimeType, file.getName());
			/** owner photo default end **/

			/** vehicle default images start **/
			BufferedImage vehicleImage = ImageIO.read(new File(busPath));
			BufferedImage resizedVehicleImg = resize(vehicleImage, 500, 500);
			ByteArrayOutputStream vehiclebos = new ByteArrayOutputStream();
			ImageIO.write(resizedVehicleImg, "png", vehiclebos);
			firstVehicleImg = vehiclebos.toByteArray();
			secondVehicleImg = vehiclebos.toByteArray();
			thirdVehicleImg = vehiclebos.toByteArray();
			fourthVehicleImg = vehiclebos.toByteArray();
			fifthVehicleImg = vehiclebos.toByteArray();
			sixthVehicleImg = vehiclebos.toByteArray();

			File file2 = new File(busPath);
			String mimeType2;
			if (file2.getName().endsWith("png")) {
				mimeType2 = "image/png";
			} else if (file2.getName().endsWith("jpg") || file2.getName().endsWith("jpeg")) {
				mimeType2 = "image/jpeg";
			} else if (file2.getName().endsWith("gif")) {
				mimeType2 = "image/gif";
			} else {
				mimeType2 = "application/octet-stream";
			}

			firstVehicleImage = new DefaultStreamedContent(new FileInputStream(file2), mimeType2, file2.getName());
			secondVehicleImage = new DefaultStreamedContent(new FileInputStream(file2), mimeType2, file2.getName());
			thirdVehicleImage = new DefaultStreamedContent(new FileInputStream(file2), mimeType2, file2.getName());
			fourthVehicleImage = new DefaultStreamedContent(new FileInputStream(file2), mimeType2, file2.getName());
			fifthVehicleImage = new DefaultStreamedContent(new FileInputStream(file2), mimeType2, file2.getName());
			sixthVehicleImage = new DefaultStreamedContent(new FileInputStream(file2), mimeType2, file2.getName());
			/** vehicle default images end **/

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

	}

	/** upload vehicle owner photo start **/
	public void handleImageUpload(FileUploadEvent event) {

		String path = null;
		String photoName = null;
		try {

			photoName = event.getFile().getFileName();

			String extension = "png";

			Properties props = PropertyReader.loadPropertyFile();
			String imagePath = props.getProperty("vehicle.inspection.upload.photo.path");

			ownerPhotoStream = event.getFile().getInputstream();
			BufferedImage imBuff = ImageIO.read(ownerPhotoStream);

			String tempPath = imagePath + uploadImageDTO.getApplicationNo() + File.separator;
			path = tempPath + "vehicleOwner." + extension;
			uploadImageDTO.setVehicleOwnerPhotoPath(path);
			ownerImageUpload = true;

			File theDir = new File(tempPath);

			if (!theDir.mkdirs()) {
				theDir.mkdir();
				File outputfile = new File(tempPath + "vehicleOwner." + "png");
				ImageIO.write(imBuff, "png", outputfile);
			} else {
				theDir.mkdir();
				File outputfile = new File(tempPath + "vehicleOwner." + "png");
				ImageIO.write(imBuff, "png", outputfile);
			}

			// Get uploaded file from the FileUploadEvent
			this.uploadedFile = event.getFile();
			permitOwnerFaceImage = uploadedFile.getContents();
			BufferedImage bi = ImageIO.read(new ByteArrayInputStream(permitOwnerFaceImage));
			BufferedImage resized = resize(bi, 500, 500);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(resized, "png", bos);
			permitOwnerFaceImage = bos.toByteArray();

			/** save data in database temporary start **/
			vehicleInspectionService.insertVehicleUploadPhotosToNT_T_OWNER_VEHI_IMAGES(uploadImageDTO,
					sessionBackingBean.getLoginUser(), "N");// update is doing by this method as well
			/** save data in database temporary end **/

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		RequestContext.getCurrentInstance().update(":frmUploadPhotos");

	}

	/** upload vehicle owner photo end **/

	/** upload vehicle vehicle images start **/
	public void handleVehicleImagesUpload(FileUploadEvent event) {
		String imageId = (String) event.getComponent().getAttributes().get("imageId");
		String path = null;

		String status = "Y";
		try {

			if (viewInsCancel) {
				status = "Y";
			}

			vehiclePhotoName = event.getFile().getFileName();
			vehiclePhotoStream = event.getFile().getInputstream();

			Properties props = PropertyReader.loadPropertyFile();
			String imagePath = props.getProperty("vehicle.inspection.upload.photo.path");

			BufferedImage imBuff = ImageIO.read(vehiclePhotoStream);

			uploadImageDTO.setApplicationNo(applicationNo);
			String tempPath = imagePath + uploadImageDTO.getApplicationNo() + File.separator;
			path = tempPath + "vehiclePhoto" + imageId + "." + "png";

			File theDir = new File(tempPath);

			if (!theDir.mkdirs()) {
				theDir.mkdir();
				File outputfile = new File(tempPath + "vehiclePhoto" + imageId + "." + "png");

				ImageIO.write(imBuff, "png", outputfile);

			} else {
				theDir.mkdir();
				File outputfile = new File(tempPath + "vehiclePhoto" + imageId + "." + "png");
				ImageIO.write(imBuff, "png", outputfile);
			}

			/** get image and display in page start **/

			this.uploadedFile = event.getFile();

			if (imageId != null && !imageId.isEmpty() && !imageId.trim().equalsIgnoreCase("")) {

				if (imageId.trim().equalsIgnoreCase("1")) {

					firstVehicleImg = uploadedFile.getContents();
					BufferedImage bi = ImageIO.read(new ByteArrayInputStream(firstVehicleImg));
					BufferedImage resized = resize(bi, 500, 500);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ImageIO.write(resized, "png", bos);
					firstVehicleImg = bos.toByteArray();

					uploadImageDTO.setFirstVehiImagePath(path);

					/** insert path into database temporary start **/
					vehicleInspectionService.insertVehicleUploadPhotosToNT_T_OWNER_VEHI_IMAGES(uploadImageDTO,
							sessionBackingBean.getLoginUser(), status);// update is doing by this method as well
					/** insert path into database temporary end **/

				} else if (imageId.trim().equalsIgnoreCase("2")) {

					secondVehicleImg = uploadedFile.getContents();
					BufferedImage bi = ImageIO.read(new ByteArrayInputStream(secondVehicleImg));
					BufferedImage resized = resize(bi, 500, 500);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ImageIO.write(resized, "png", bos);
					secondVehicleImg = bos.toByteArray();

					uploadImageDTO.setSecondVehiImagePath(path);

					/** insert path into database temporary start **/
					vehicleInspectionService.insertVehicleUploadPhotosToNT_T_OWNER_VEHI_IMAGES(uploadImageDTO,
							sessionBackingBean.getLoginUser(), status);// update is doing by this method as well
					/** insert path into database temporary end **/

				} else if (imageId.trim().equalsIgnoreCase("3")) {

					thirdVehicleImg = uploadedFile.getContents();
					BufferedImage bi = ImageIO.read(new ByteArrayInputStream(thirdVehicleImg));
					BufferedImage resized = resize(bi, 500, 500);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					logger.info("ImageIO.write start thrid image start");
					ImageIO.write(resized, "png", bos);
					logger.info("ImageIO.write start thrid image start");
					thirdVehicleImg = bos.toByteArray();

					uploadImageDTO.setThirdVehiImagePath(path);

					/** insert path into database temporary start **/
					vehicleInspectionService.insertVehicleUploadPhotosToNT_T_OWNER_VEHI_IMAGES(uploadImageDTO,
							sessionBackingBean.getLoginUser(), status);// update is doing by this method as well
					/** insert path into database temporary end **/

				} else if (imageId.trim().equalsIgnoreCase("4")) {

					fourthVehicleImg = uploadedFile.getContents();
					BufferedImage bi = ImageIO.read(new ByteArrayInputStream(fourthVehicleImg));
					BufferedImage resized = resize(bi, 500, 500);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ImageIO.write(resized, "png", bos);
					fourthVehicleImg = bos.toByteArray();

					uploadImageDTO.setForthVehiImagePath(path);

					/** insert path into database temporary start **/
					vehicleInspectionService.insertVehicleUploadPhotosToNT_T_OWNER_VEHI_IMAGES(uploadImageDTO,
							sessionBackingBean.getLoginUser(), status);// update is doing by this method as well
					/** insert path into database temporary end **/

				} else if (imageId.trim().equalsIgnoreCase("5")) {

					fifthVehicleImg = uploadedFile.getContents();
					BufferedImage bi = ImageIO.read(new ByteArrayInputStream(fifthVehicleImg));
					BufferedImage resized = resize(bi, 500, 500);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ImageIO.write(resized, "png", bos);
					fifthVehicleImg = bos.toByteArray();

					uploadImageDTO.setFifthVehiImagePath(path);

					/** insert path into database temporary start **/
					vehicleInspectionService.insertVehicleUploadPhotosToNT_T_OWNER_VEHI_IMAGES(uploadImageDTO,
							sessionBackingBean.getLoginUser(), status);// update is doing by this method as well
					/** insert path into database temporary end **/

				} else if (imageId.trim().equalsIgnoreCase("6")) {

					sixthVehicleImg = uploadedFile.getContents();
					BufferedImage bi = ImageIO.read(new ByteArrayInputStream(sixthVehicleImg));
					BufferedImage resized = resize(bi, 500, 500);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ImageIO.write(resized, "png", bos);
					sixthVehicleImg = bos.toByteArray();

					uploadImageDTO.setSixthVehiImagePath(path);

					/** insert path into database temporary start **/
					vehicleInspectionService.insertVehicleUploadPhotosToNT_T_OWNER_VEHI_IMAGES(uploadImageDTO,
							sessionBackingBean.getLoginUser(), status);// update is doing by this method as well
					/** insert path into database temporary end **/
				}

			}
			/** get image and display in page end **/

			RequestContext.getCurrentInstance().update(":frmUploadPhotos");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

	}

	/** upload vehicle vehicle images end **/

	/** save image paths to DB start **/
	public void savePhotos() {

		String message = validateImageCount();
		if (message != null && !message.isEmpty() && !message.trim().equalsIgnoreCase("")) {
			sessionBackingBean.showMessage("Warning", message, "WARNING_DIALOG");
			return;
		}

		vehicleInspectionService.insertVehicleUploadPhotosToNT_T_OWNER_VEHI_IMAGES(uploadImageDTO,
				sessionBackingBean.getLoginUser(), "Y");

		VehicleInspectionDTO vDto = new VehicleInspectionDTO();
		vDto.setApplicationNo(uploadImageDTO.getApplicationNo());

		migratedService.updateStatusOfQueueNumberAfterCallNext(tempQueueNo, "C");

		FacesContext fcontext = FacesContext.getCurrentInstance();

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('saveSuccessWv').show();");

		if (vehicleInspection.equalsIgnoreCase("main_other")) {

			Object objCallerQueueNo = fcontext.getExternalContext().getSessionMap().get("INSPECTION_TYPE");
			if (objCallerQueueNo != null) {
				String inspectionType = (String) objCallerQueueNo;
				VehicleInspectionDTO vehicleDTO = new VehicleInspectionDTO();
				vehicleDTO.setApplicationNo(applicationNo);

				if (inspectionType.equals("CI")) {
					commonService.otherInspectionTasksUpdate(uploadImageDTO.getApplicationNo(), "CI101", "C",
							sessionBackingBean.getLoginUser(), sessionBackingBean.getCounterId(),
							uploadImageDTO.getVehicleNo());

					vehicleInspectionService.updateQueueMasterStatusUsingAppNo(tempQueueNo, vehicleDTO, "C", "CI101",
							"C", sessionBackingBean.getLoginUser());

				} else if (inspectionType.equals("II")) {
					commonService.otherInspectionTasksUpdate(uploadImageDTO.getApplicationNo(), "II101", "C",
							sessionBackingBean.getLoginUser(), sessionBackingBean.getCounterId(),
							uploadImageDTO.getVehicleNo());

					vehicleInspectionService.updateQueueMasterStatusUsingAppNo(tempQueueNo, vehicleDTO, "C", "II101",
							"C", sessionBackingBean.getLoginUser());

				} else if (inspectionType.equals("SI")) {
					commonService.otherInspectionTasksUpdate(uploadImageDTO.getApplicationNo(), "SI101", "C",
							sessionBackingBean.getLoginUser(), sessionBackingBean.getCounterId(),
							uploadImageDTO.getVehicleNo());

					vehicleInspectionService.updateQueueMasterStatusUsingAppNo(tempQueueNo, vehicleDTO, "C", "SI101",
							"C", sessionBackingBean.getLoginUser());
				}

			}

		} else if (vehicleInspection.equalsIgnoreCase("view_other")) {

			Object objCallerQueueNo = fcontext.getExternalContext().getSessionMap().get("INSPECTION_TYPE");
			if (objCallerQueueNo != null) {
				String inspectionType = (String) objCallerQueueNo;
				VehicleInspectionDTO vehicleDTO = new VehicleInspectionDTO();
				vehicleDTO.setApplicationNo(applicationNo);

				if (inspectionType.equals("CI")) {
					commonService.otherInspectionTasksUpdate(uploadImageDTO.getApplicationNo(), "CI103", "C",
							sessionBackingBean.getLoginUser(), sessionBackingBean.getCounterId(),
							uploadImageDTO.getVehicleNo());

					vehicleInspectionService.updateQueueMasterStatusUsingAppNo(tempQueueNo, vehicleDTO, "C", "CI103",
							"C", sessionBackingBean.getLoginUser());

				} else if (inspectionType.equals("II")) {
					commonService.otherInspectionTasksUpdate(uploadImageDTO.getApplicationNo(), "II103", "C",
							sessionBackingBean.getLoginUser(), sessionBackingBean.getCounterId(),
							uploadImageDTO.getVehicleNo());

					vehicleInspectionService.updateQueueMasterStatusUsingAppNo(tempQueueNo, vehicleDTO, "C", "II103",
							"C", sessionBackingBean.getLoginUser());

				} else if (inspectionType.equals("SI")) {
					commonService.otherInspectionTasksUpdate(uploadImageDTO.getApplicationNo(), "SI103", "C",
							sessionBackingBean.getLoginUser(), sessionBackingBean.getCounterId(),
							uploadImageDTO.getVehicleNo());

					vehicleInspectionService.updateQueueMasterStatusUsingAppNo(tempQueueNo, vehicleDTO, "C", "SI103",
							"C", sessionBackingBean.getLoginUser());
				}

			}

		}

		uploadImageDTO = new UploadImageDTO();
		loadDefaultImages();
		photosSaveSuccess = true;
		RequestContext.getCurrentInstance().update(":frmUploadPhotos");

	}

	/** save image paths to DB end **/

	/****/
	public String validateImageCount() {
		int count = 0;

		ParamerDTO dto = new ParamerDTO();
		int minCount = 0;

		dto = migratedService.retrieveParameterValuesForParamName("PHOTO_UPLOAD_MIN_COUNT");
		minCount = dto.getIntValue();

		if (uploadImageDTO.getFirstVehiImagePath() != null && !uploadImageDTO.getFirstVehiImagePath().isEmpty()
				&& !uploadImageDTO.getFirstVehiImagePath().equalsIgnoreCase("")) {
			count++;
		}
		if (uploadImageDTO.getSecondVehiImagePath() != null && !uploadImageDTO.getSecondVehiImagePath().isEmpty()
				&& !uploadImageDTO.getSecondVehiImagePath().equalsIgnoreCase("")) {
			count++;
		}
		if (uploadImageDTO.getThirdVehiImagePath() != null && !uploadImageDTO.getThirdVehiImagePath().isEmpty()
				&& !uploadImageDTO.getThirdVehiImagePath().equalsIgnoreCase("")) {
			count++;
		}
		if (uploadImageDTO.getForthVehiImagePath() != null && !uploadImageDTO.getForthVehiImagePath().isEmpty()
				&& !uploadImageDTO.getForthVehiImagePath().equalsIgnoreCase("")) {
			count++;
		}
		if (uploadImageDTO.getFifthVehiImagePath() != null && !uploadImageDTO.getFifthVehiImagePath().isEmpty()
				&& !uploadImageDTO.getFifthVehiImagePath().equalsIgnoreCase("")) {
			count++;
		}
		if (uploadImageDTO.getSixthVehiImagePath() != null && !uploadImageDTO.getSixthVehiImagePath().isEmpty()
				&& !uploadImageDTO.getSixthVehiImagePath().equalsIgnoreCase("")) {
			count++;
		}

		if (count < minCount) {
			return "Please upload at least " + minCount + " vehicle photo/s";
		}
		if (!ownerImageUpload) {
			return "Please upload Permit Owner photo";
		}

		return null;
	}

	/****/

	/** search data by vehicle no start **/
	public void searchDataByVehicleNumber() {
		UploadImageDTO tempUploadImageDTO = new UploadImageDTO();

		try {

			if (uploadImageDTO == null || uploadImageDTO.getVehicleOwnerPhotoPath() == null
					|| uploadImageDTO.getVehicleOwnerPhotoPath().isEmpty() || uploadImageDTO.getApplicationNo() == null
					|| uploadImageDTO.getApplicationNo().isEmpty()) {

				tempUploadImageDTO = vehicleInspectionService.retrieveVehicleImageDataForVehicleNo(applicationNo);
			}

			if (tempUploadImageDTO != null && ((tempUploadImageDTO.getVehicleOwnerPhotoPath() != null
					&& !tempUploadImageDTO.getVehicleOwnerPhotoPath().isEmpty()
					&& !tempUploadImageDTO.getVehicleOwnerPhotoPath().trim().equalsIgnoreCase(""))
					|| (tempUploadImageDTO.getFirstVehiImagePath() != null
							&& !tempUploadImageDTO.getFirstVehiImagePath().isEmpty()
							&& !tempUploadImageDTO.getFirstVehiImagePath().trim().equalsIgnoreCase(""))
					|| (tempUploadImageDTO.getSecondVehiImagePath() != null
							&& !tempUploadImageDTO.getSecondVehiImagePath().isEmpty()
							&& !tempUploadImageDTO.getSecondVehiImagePath().trim().equalsIgnoreCase(""))
					|| (tempUploadImageDTO.getThirdVehiImagePath() != null
							&& !tempUploadImageDTO.getThirdVehiImagePath().isEmpty()
							&& !tempUploadImageDTO.getThirdVehiImagePath().trim().equalsIgnoreCase(""))
					|| (tempUploadImageDTO.getForthVehiImagePath() != null
							&& !tempUploadImageDTO.getForthVehiImagePath().isEmpty()
							&& !tempUploadImageDTO.getForthVehiImagePath().trim().equalsIgnoreCase(""))
					|| (tempUploadImageDTO.getFifthVehiImagePath() != null
							&& !tempUploadImageDTO.getFifthVehiImagePath().isEmpty()
							&& !tempUploadImageDTO.getFifthVehiImagePath().trim().equalsIgnoreCase(""))
					|| (tempUploadImageDTO.getSixthVehiImagePath() != null
							&& !tempUploadImageDTO.getSixthVehiImagePath().isEmpty()
							&& !tempUploadImageDTO.getSixthVehiImagePath().trim().equalsIgnoreCase("")))) {
				/** set owner image start **/
				if (tempUploadImageDTO.getVehicleOwnerPhotoPath() != null
						&& !tempUploadImageDTO.getVehicleOwnerPhotoPath().isEmpty()
						&& !tempUploadImageDTO.getVehicleOwnerPhotoPath().trim().equalsIgnoreCase("")) {
					BufferedImage ownerImage = ImageIO.read(new File(tempUploadImageDTO.getVehicleOwnerPhotoPath()));
					BufferedImage resized = resize(ownerImage, 600, 600);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ImageIO.write(resized, "png", bos);
					permitOwnerFaceImage = bos.toByteArray();

					ownerImageUpload = true;
					uploadImageDTO.setVehicleOwnerPhotoPath(tempUploadImageDTO.getVehicleOwnerPhotoPath());
				}
				/** set owner image end **/

				/** set vehicle images start **/
				if (tempUploadImageDTO.getFirstVehiImagePath() != null
						&& !tempUploadImageDTO.getFirstVehiImagePath().isEmpty()
						&& !tempUploadImageDTO.getFirstVehiImagePath().trim().equalsIgnoreCase("")) {
					BufferedImage vehicleImage = ImageIO.read(new File(tempUploadImageDTO.getFirstVehiImagePath()));
					BufferedImage resized = resize(vehicleImage, 500, 500);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ImageIO.write(resized, "png", bos);
					firstVehicleImg = bos.toByteArray();
					uploadImageDTO.setFirstVehiImagePath(tempUploadImageDTO.getFirstVehiImagePath());
				}
				if (tempUploadImageDTO.getSecondVehiImagePath() != null
						&& !tempUploadImageDTO.getSecondVehiImagePath().isEmpty()
						&& !tempUploadImageDTO.getSecondVehiImagePath().trim().equalsIgnoreCase("")) {
					BufferedImage vehicleImage = ImageIO.read(new File(tempUploadImageDTO.getSecondVehiImagePath()));
					BufferedImage resized = resize(vehicleImage, 500, 500);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ImageIO.write(resized, "png", bos);
					secondVehicleImg = bos.toByteArray();
					uploadImageDTO.setSecondVehiImagePath(tempUploadImageDTO.getSecondVehiImagePath());
				}
				if (tempUploadImageDTO.getThirdVehiImagePath() != null
						&& !tempUploadImageDTO.getThirdVehiImagePath().isEmpty()
						&& !tempUploadImageDTO.getThirdVehiImagePath().trim().equalsIgnoreCase("")) {
					BufferedImage vehicleImage = ImageIO.read(new File(tempUploadImageDTO.getThirdVehiImagePath()));
					BufferedImage resized = resize(vehicleImage, 500, 500);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ImageIO.write(resized, "png", bos);
					thirdVehicleImg = bos.toByteArray();
					uploadImageDTO.setThirdVehiImagePath(tempUploadImageDTO.getThirdVehiImagePath());
				}
				if (tempUploadImageDTO.getForthVehiImagePath() != null
						&& !tempUploadImageDTO.getForthVehiImagePath().isEmpty()
						&& !tempUploadImageDTO.getForthVehiImagePath().trim().equalsIgnoreCase("")) {
					BufferedImage vehicleImage = ImageIO.read(new File(tempUploadImageDTO.getForthVehiImagePath()));
					BufferedImage resized = resize(vehicleImage, 500, 500);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ImageIO.write(resized, "png", bos);
					fourthVehicleImg = bos.toByteArray();
					uploadImageDTO.setForthVehiImagePath(tempUploadImageDTO.getForthVehiImagePath());
				}
				if (tempUploadImageDTO.getFifthVehiImagePath() != null
						&& !tempUploadImageDTO.getFifthVehiImagePath().isEmpty()
						&& !tempUploadImageDTO.getFifthVehiImagePath().trim().equalsIgnoreCase("")) {
					BufferedImage vehicleImage = ImageIO.read(new File(tempUploadImageDTO.getFifthVehiImagePath()));
					BufferedImage resized = resize(vehicleImage, 500, 500);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ImageIO.write(resized, "png", bos);
					fifthVehicleImg = bos.toByteArray();
					uploadImageDTO.setFifthVehiImagePath(tempUploadImageDTO.getFifthVehiImagePath());
				}
				if (tempUploadImageDTO.getSixthVehiImagePath() != null
						&& !tempUploadImageDTO.getSixthVehiImagePath().isEmpty()
						&& !tempUploadImageDTO.getSixthVehiImagePath().trim().equalsIgnoreCase("")) {
					BufferedImage vehicleImage = ImageIO.read(new File(tempUploadImageDTO.getSixthVehiImagePath()));
					BufferedImage resized = resize(vehicleImage, 500, 500);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					ImageIO.write(resized, "png", bos);
					sixthVehicleImg = bos.toByteArray();
					uploadImageDTO.setSixthVehiImagePath(tempUploadImageDTO.getSixthVehiImagePath());
				}

				/** set vehicle images end **/

			} else {
				UploadImageDTO uploadImageDTOTemp = new UploadImageDTO();
				uploadImageDTOTemp = vehicleInspectionService.retrieveVehicleImageDataForVehicleNo(applicationNo);
				boolean canread = false;
				boolean imageCorrupt = false;
				if (uploadImageDTOTemp != null) {
					/** set owner image start **/
					if (uploadImageDTOTemp.getVehicleOwnerPhotoPath() != null
							&& !uploadImageDTOTemp.getVehicleOwnerPhotoPath().isEmpty()
							&& !uploadImageDTOTemp.getVehicleOwnerPhotoPath().trim().equalsIgnoreCase("")) {
						/** added by tharushi.e for live issue **/
						File f = new File(uploadImageDTOTemp.getVehicleOwnerPhotoPath());
						if (f.canRead()) {
							canread = true;

							BufferedImage ownerImage = ImageIO
									.read(new File(uploadImageDTOTemp.getVehicleOwnerPhotoPath()));
							if (ownerImage != null) {
								BufferedImage resized = resize(ownerImage, 600, 600);
								ByteArrayOutputStream bos = new ByteArrayOutputStream();
								ImageIO.write(resized, "png", bos);
								permitOwnerFaceImage = bos.toByteArray();

								ownerImageUpload = true;
								uploadImageDTO.setVehicleOwnerPhotoPath(uploadImageDTOTemp.getVehicleOwnerPhotoPath());
							} else {

								imageCorrupt = true;
								ownerImageUpload = false;

								try {
									/** owner photo default start **/
									Properties props = PropertyReader.loadPropertyFile();
									String ownerPath = props
											.getProperty("vehicle.inspection.upload.photo.owner.default.path");

									BufferedImage personImage = ImageIO.read(new File(ownerPath));
									BufferedImage resized = resize(personImage, 500, 500);
									ByteArrayOutputStream bos = new ByteArrayOutputStream();
									ImageIO.write(resized, "png", bos);
									permitOwnerFaceImage = bos.toByteArray();
									/** owner photo default end **/

								} catch (Exception e) {
									e.printStackTrace();
								}

							}
						}

					}
					/** set owner image end **/

					/** set vehicle images start **/

					if (canread && !imageCorrupt) {
						if (uploadImageDTOTemp.getFirstVehiImagePath() != null
								&& !uploadImageDTOTemp.getFirstVehiImagePath().isEmpty()
								&& !uploadImageDTOTemp.getFirstVehiImagePath().trim().equalsIgnoreCase("")) {
							BufferedImage vehicleImage = ImageIO
									.read(new File(uploadImageDTOTemp.getFirstVehiImagePath()));
							BufferedImage resized = resize(vehicleImage, 500, 500);
							ByteArrayOutputStream bos = new ByteArrayOutputStream();
							ImageIO.write(resized, "png", bos);
							firstVehicleImg = bos.toByteArray();

							uploadImageDTO.setFirstVehiImagePath(uploadImageDTOTemp.getFirstVehiImagePath());

						}
						if (uploadImageDTOTemp.getSecondVehiImagePath() != null
								&& !uploadImageDTOTemp.getSecondVehiImagePath().isEmpty()
								&& !uploadImageDTOTemp.getSecondVehiImagePath().trim().equalsIgnoreCase("")) {
							BufferedImage vehicleImage = ImageIO
									.read(new File(uploadImageDTOTemp.getSecondVehiImagePath()));
							BufferedImage resized = resize(vehicleImage, 500, 500);
							ByteArrayOutputStream bos = new ByteArrayOutputStream();
							ImageIO.write(resized, "png", bos);
							secondVehicleImg = bos.toByteArray();

							uploadImageDTO.setSecondVehiImagePath(uploadImageDTOTemp.getSecondVehiImagePath());

						}
						if (uploadImageDTOTemp.getThirdVehiImagePath() != null
								&& !uploadImageDTOTemp.getThirdVehiImagePath().isEmpty()
								&& !uploadImageDTOTemp.getThirdVehiImagePath().trim().equalsIgnoreCase("")) {
							BufferedImage vehicleImage = ImageIO
									.read(new File(uploadImageDTOTemp.getThirdVehiImagePath()));
							BufferedImage resized = resize(vehicleImage, 500, 500);
							ByteArrayOutputStream bos = new ByteArrayOutputStream();
							ImageIO.write(resized, "png", bos);
							thirdVehicleImg = bos.toByteArray();
							uploadImageDTO.setThirdVehiImagePath(uploadImageDTOTemp.getThirdVehiImagePath());
						}
						if (uploadImageDTOTemp.getForthVehiImagePath() != null
								&& !uploadImageDTOTemp.getForthVehiImagePath().isEmpty()
								&& !uploadImageDTOTemp.getForthVehiImagePath().trim().equalsIgnoreCase("")) {
							BufferedImage vehicleImage = ImageIO
									.read(new File(uploadImageDTOTemp.getForthVehiImagePath()));
							BufferedImage resized = resize(vehicleImage, 500, 500);
							ByteArrayOutputStream bos = new ByteArrayOutputStream();
							ImageIO.write(resized, "png", bos);
							fourthVehicleImg = bos.toByteArray();
							uploadImageDTO.setForthVehiImagePath(uploadImageDTOTemp.getForthVehiImagePath());
						}
						if (uploadImageDTOTemp.getFifthVehiImagePath() != null
								&& !uploadImageDTOTemp.getFifthVehiImagePath().isEmpty()
								&& !uploadImageDTOTemp.getFifthVehiImagePath().trim().equalsIgnoreCase("")) {
							BufferedImage vehicleImage = ImageIO
									.read(new File(uploadImageDTOTemp.getFifthVehiImagePath()));
							BufferedImage resized = resize(vehicleImage, 500, 500);
							ByteArrayOutputStream bos = new ByteArrayOutputStream();
							ImageIO.write(resized, "png", bos);
							fifthVehicleImg = bos.toByteArray();
							uploadImageDTO.setFifthVehiImagePath(uploadImageDTOTemp.getFifthVehiImagePath());
						}
						if (uploadImageDTOTemp.getSixthVehiImagePath() != null
								&& !uploadImageDTOTemp.getSixthVehiImagePath().isEmpty()
								&& !uploadImageDTOTemp.getSixthVehiImagePath().trim().equalsIgnoreCase("")) {
							BufferedImage vehicleImage = ImageIO
									.read(new File(uploadImageDTOTemp.getSixthVehiImagePath()));
							BufferedImage resized = resize(vehicleImage, 500, 500);
							ByteArrayOutputStream bos = new ByteArrayOutputStream();
							ImageIO.write(resized, "png", bos);
							sixthVehicleImg = bos.toByteArray();
							uploadImageDTO.setSixthVehiImagePath(uploadImageDTOTemp.getSixthVehiImagePath());
						}
					}

					else {
						/** added error message for prevent Live issue in photo upload **/
						if (!canread) {

							RequestContext context = RequestContext.getCurrentInstance();
							context.execute("PF('saveSuccessWv1').show();");
						}
						if (imageCorrupt) {

							RequestContext context = RequestContext.getCurrentInstance();
							context.execute("PF('saveSuccessWv2').show();");

						}

					}

				}

				/** set vehicle images end **/

				uploadImageDTO.setVehicleNo(vehicleNo);
				uploadImageDTO.setApplicationNo(applicationNo);
				uploadImageDTO.setVehicleOwnerName(vehicleOwnerName);
			}

		} catch (IOException e) {
			e.printStackTrace();

		} finally {

		}

		RequestContext.getCurrentInstance().update(":frmUploadPhotos");
	}

	/** search data by vehicle no end **/
	public void closeWarningMsg() {
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('saveSuccessWv1').hide();");

	}

	public void closeWarningMsg2() {
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('saveSuccessWv2').hide();");

	}

	/** delete vehicle owner image start **/
	public void deleteVehicleOwnerImage() {

		try {

			/** Check renewal and get application number start **/
			UploadImageDTO renewalUploadedDTO = new UploadImageDTO();
			if (!applicationNo.equalsIgnoreCase(uploadImageDTO.getApplicationNo())) {
				renewalUploadedDTO = vehicleInspectionService.findVehicleOwnerByFormerApplicationNo(vehicleNo);
			}

			if (renewalUploadedDTO != null && renewalUploadedDTO.getVehicleOwnerPhotoPath() != null
					&& !renewalUploadedDTO.getVehicleOwnerPhotoPath().isEmpty()
					&& !renewalUploadedDTO.getVehicleOwnerPhotoPath().trim().equalsIgnoreCase("")) {
				uploadImageDTO = renewalUploadedDTO;
			}
			/** Check renewal and get application number end **/

			deleteUploadImagesDTO.setVehicleOwnerPhotoPath(uploadImageDTO.getVehicleOwnerPhotoPath());
			uploadImageDTO.setVehicleOwnerPhotoPath(null);
			ownerImageUpload = false;

			/** delete from physical path start **/
			File file = new File(deleteUploadImagesDTO.getVehicleOwnerPhotoPath());

			if (file.delete()) {
				logger.info(file.getName() + " is deleted for application no: " + uploadImageDTO.getApplicationNo());
			} else {
				logger.info(file.getName() + " Delete operation is failed application no: "
						+ uploadImageDTO.getApplicationNo());
			}
			/** delete from physical path end **/

			/** owner photo default start **/
			Properties props = PropertyReader.loadPropertyFile();
			String ownerPath = props.getProperty("vehicle.inspection.upload.photo.owner.default.path");

			BufferedImage personImage = ImageIO.read(new File(ownerPath));
			BufferedImage resized = resize(personImage, 500, 500);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(resized, "png", bos);
			permitOwnerFaceImage = bos.toByteArray();
			/** owner photo default end **/

			/** delete data from database table start **/
			vehicleInspectionService.insertVehicleUploadPhotosToNT_T_OWNER_VEHI_IMAGES(uploadImageDTO,
					sessionBackingBean.getLoginUser(), "N");// update is doing by this method as well
			/** delete data from database table end **/

			RequestContext.getCurrentInstance().update(":frmUploadPhotos");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** delete vehicle owner image end **/

	/** delete vehicle images start **/
	public void deleteVehicleImages(String imageId) {

		try {

			/** Check renewal and get application number start **/
			UploadImageDTO renewalUploadedDTO = new UploadImageDTO();
			if (!applicationNo.equalsIgnoreCase(uploadImageDTO.getApplicationNo())) {
				renewalUploadedDTO = vehicleInspectionService
						.findVehicleOwnerByFormerApplicationNo(uploadImageDTO.getVehicleNo());
			}

			if (renewalUploadedDTO != null) {
				uploadImageDTO = renewalUploadedDTO;
			}
			/** Check renewal and get application number end **/

			File file = null;
			Properties props = PropertyReader.loadPropertyFile();
			String vehiclesPath = props.getProperty("vehicle.inspection.upload.photo.bus.default.path");

			BufferedImage vehicleImage = ImageIO.read(new File(vehiclesPath));
			BufferedImage resized = resize(vehicleImage, 500, 500);
			ByteArrayOutputStream vehiclebos = new ByteArrayOutputStream();
			ImageIO.write(resized, "png", vehiclebos);

			if (imageId.equalsIgnoreCase("1")) {

				deleteUploadImagesDTO.setFirstVehiImagePath(uploadImageDTO.getFirstVehiImagePath());

				/** vehicle default photo start **/
				firstVehicleImg = vehiclebos.toByteArray();
				/** vehicle default photo end **/

				/** delete from physicalPath start **/
				if (deleteUploadImagesDTO.getFirstVehiImagePath() != null
						&& !deleteUploadImagesDTO.getFirstVehiImagePath().isEmpty()
						&& !deleteUploadImagesDTO.getFirstVehiImagePath().trim().equalsIgnoreCase("")) {
					file = new File(deleteUploadImagesDTO.getFirstVehiImagePath());

					if (file.delete()) {
						logger.info(file.getName() + " is deleted for application no: "
								+ uploadImageDTO.getApplicationNo());
					} else {
						logger.info(file.getName() + " Delete operation is failed application no: "
								+ uploadImageDTO.getApplicationNo());
					}
				}
				/** delete from physicalPath end **/

				/** delete from database start **/
				uploadImageDTO.setFirstVehiImagePath(null);
				vehicleInspectionService.insertVehicleUploadPhotosToNT_T_OWNER_VEHI_IMAGES(uploadImageDTO,
						sessionBackingBean.getLoginUser(), "Y");// update is doing by this method as well
				/** delete from database end **/

			}
			if (imageId.equalsIgnoreCase("2")) {

				deleteUploadImagesDTO.setSecondVehiImagePath(uploadImageDTO.getSecondVehiImagePath());

				/** vehicle default photo start **/
				secondVehicleImg = vehiclebos.toByteArray();
				/** vehicle default photo end **/

				/** delete from physicalPath start **/
				if (deleteUploadImagesDTO.getSecondVehiImagePath() != null
						&& !deleteUploadImagesDTO.getSecondVehiImagePath().isEmpty()
						&& !deleteUploadImagesDTO.getSecondVehiImagePath().trim().equalsIgnoreCase("")) {
					file = new File(deleteUploadImagesDTO.getSecondVehiImagePath());

					if (file.delete()) {
						logger.info(file.getName() + " is deleted for application no: "
								+ uploadImageDTO.getApplicationNo());
					} else {
						logger.info(file.getName() + " Delete operation is failed application no: "
								+ uploadImageDTO.getApplicationNo());
					}
				}
				/** delete from physicalPath end **/

				/** delete from database start **/
				uploadImageDTO.setSecondVehiImagePath(null);
				vehicleInspectionService.insertVehicleUploadPhotosToNT_T_OWNER_VEHI_IMAGES(uploadImageDTO,
						sessionBackingBean.getLoginUser(), "Y");// update is doing by this method as well
				/** delete from database end **/
			}
			if (imageId.equalsIgnoreCase("3")) {

				deleteUploadImagesDTO.setThirdVehiImagePath(uploadImageDTO.getThirdVehiImagePath());

				/** vehicle default photo start **/
				thirdVehicleImg = vehiclebos.toByteArray();
				/** vehicle default photo end **/

				/** delete from physicalPath start **/
				if (deleteUploadImagesDTO.getThirdVehiImagePath() != null
						&& !deleteUploadImagesDTO.getThirdVehiImagePath().isEmpty()
						&& !deleteUploadImagesDTO.getThirdVehiImagePath().trim().equalsIgnoreCase("")) {
					file = new File(deleteUploadImagesDTO.getThirdVehiImagePath());

					if (file.delete()) {
						logger.info(file.getName() + " is deleted for application no: "
								+ uploadImageDTO.getApplicationNo());
					} else {
						logger.info(file.getName() + " Delete operation is failed application no: "
								+ uploadImageDTO.getApplicationNo());
					}
				}
				/** delete from physicalPath end **/

				/** delete from database start **/
				uploadImageDTO.setThirdVehiImagePath(null);
				vehicleInspectionService.insertVehicleUploadPhotosToNT_T_OWNER_VEHI_IMAGES(uploadImageDTO,
						sessionBackingBean.getLoginUser(), "Y");// update is doing by this method as well
				/** delete from database end **/

			}
			if (imageId.equalsIgnoreCase("4")) {

				deleteUploadImagesDTO.setForthVehiImagePath(uploadImageDTO.getForthVehiImagePath());

				/** vehicle default photo start **/
				fourthVehicleImg = vehiclebos.toByteArray();
				/** vehicle default photo end **/

				/** delete from physicalPath start **/
				if (deleteUploadImagesDTO.getForthVehiImagePath() != null
						&& !deleteUploadImagesDTO.getForthVehiImagePath().isEmpty()
						&& !deleteUploadImagesDTO.getForthVehiImagePath().trim().equalsIgnoreCase("")) {
					file = new File(deleteUploadImagesDTO.getForthVehiImagePath());

					if (file.delete()) {
						logger.info(file.getName() + " is deleted for application no: "
								+ uploadImageDTO.getApplicationNo());
					} else {
						logger.info(file.getName() + " Delete operation is failed application no: "
								+ uploadImageDTO.getApplicationNo());
					}
				}
				/** delete from physicalPath end **/

				/** delete from database start **/
				uploadImageDTO.setForthVehiImagePath(null);
				vehicleInspectionService.insertVehicleUploadPhotosToNT_T_OWNER_VEHI_IMAGES(uploadImageDTO,
						sessionBackingBean.getLoginUser(), "Y");// update is doing by this method as well
				/** delete from database end **/
			}
			if (imageId.equalsIgnoreCase("5")) {

				deleteUploadImagesDTO.setFifthVehiImagePath(uploadImageDTO.getFifthVehiImagePath());

				/** vehicle default photo start **/
				fifthVehicleImg = vehiclebos.toByteArray();
				/** vehicle default photo end **/

				/** delete from physicalPath start **/
				if (deleteUploadImagesDTO.getFifthVehiImagePath() != null
						&& !deleteUploadImagesDTO.getFifthVehiImagePath().isEmpty()
						&& !deleteUploadImagesDTO.getFifthVehiImagePath().trim().equalsIgnoreCase("")) {
					file = new File(deleteUploadImagesDTO.getFifthVehiImagePath());

					if (file.delete()) {

						logger.info(file.getName() + " is deleted for application no: "
								+ uploadImageDTO.getApplicationNo());
					} else {
						logger.info(file.getName() + " Delete operation is failed application no: "
								+ uploadImageDTO.getApplicationNo());
					}
				}
				/** delete from physicalPath end **/

				/** delete from database start **/
				uploadImageDTO.setFifthVehiImagePath(null);
				vehicleInspectionService.insertVehicleUploadPhotosToNT_T_OWNER_VEHI_IMAGES(uploadImageDTO,
						sessionBackingBean.getLoginUser(), "Y");// update is doing by this method as well
				/** delete from database end **/
			}
			if (imageId.equalsIgnoreCase("6")) {

				deleteUploadImagesDTO.setSixthVehiImagePath(uploadImageDTO.getSixthVehiImagePath());

				/** vehicle default photo start **/
				sixthVehicleImg = vehiclebos.toByteArray();
				/** vehicle default photo end **/

				/** delete from physicalPath start **/
				if (deleteUploadImagesDTO.getSixthVehiImagePath() != null
						&& !deleteUploadImagesDTO.getSixthVehiImagePath().isEmpty()
						&& !deleteUploadImagesDTO.getSixthVehiImagePath().trim().equalsIgnoreCase("")) {
					file = new File(deleteUploadImagesDTO.getSixthVehiImagePath());

					if (file.delete()) {

						logger.info(file.getName() + " is deleted for application no: "
								+ uploadImageDTO.getApplicationNo());
					} else {

						logger.info(file.getName() + " Delete operation is failed application no: "
								+ uploadImageDTO.getApplicationNo());
					}
				}
				/** delete from physicalPath end **/

				/** delete from database start **/
				uploadImageDTO.setSixthVehiImagePath(null);
				vehicleInspectionService.insertVehicleUploadPhotosToNT_T_OWNER_VEHI_IMAGES(uploadImageDTO,
						sessionBackingBean.getLoginUser(), "Y");
				/** delete from database end **/
			}

			RequestContext.getCurrentInstance().update(":frmUploadPhotos");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** delete vehicle images end **/

	// permit owner image
	public String getImageOfPermitOwner() {
		if (permitOwnerFaceImage != null) {
			return Base64.getEncoder().encodeToString(permitOwnerFaceImage);
		}
		return null;

	}

	// vehicle image 6
	public String getFirstImageOfVehicle() {
		if (firstVehicleImg != null) {
			return Base64.getEncoder().encodeToString(firstVehicleImg);
		}
		return null;

	}

	// vehicle image 2
	public String getSecondImageOfVehicle() {
		if (secondVehicleImg != null) {
			return Base64.getEncoder().encodeToString(secondVehicleImg);
		}
		return null;

	}

	// vehicle image 3
	public String getThirdImageOfVehicle() {
		if (thirdVehicleImg != null) {
			return Base64.getEncoder().encodeToString(thirdVehicleImg);
		}
		return null;

	}

	// vehicle image 4
	public String getFourthImageOfVehicle() {
		if (fourthVehicleImg != null) {
			return Base64.getEncoder().encodeToString(fourthVehicleImg);
		}
		return null;

	}

	// vehicle image 5
	public String getFifthImageOfVehicle() {
		if (fifthVehicleImg != null) {
			return Base64.getEncoder().encodeToString(fifthVehicleImg);
		}
		return null;

	}

	// vehicle image 6
	public String getSixthImageOfVehicle() {
		if (sixthVehicleImg != null) {
			return Base64.getEncoder().encodeToString(sixthVehicleImg);
		}
		return null;

	}

	private static BufferedImage resize(BufferedImage img, int height, int width) {
		logger.info("BufferedImage resize(" + height + "," + width + ") start");

		try {

			Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			logger.info("Image tmp done");

			BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			logger.info("BufferedImage resized: " + width + "," + height);

			Graphics2D g2d = resized.createGraphics();
			logger.info("Graphics2D g2d done");

			g2d.drawImage(tmp, 0, 0, null);
			logger.info("g2d.drawImage done");

			g2d.dispose();
			logger.info(" g2d.dispose done");

			return resized;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error upload photos: " + e.toString());
		}

		logger.info("BufferedImage resize(" + height + "," + width + ") end");
		return null;
	}

	public void backButtonAction() {
		try {

			if (!photosSaveSuccess) {

				String message = validateImageCount();
				if (message != null && !message.isEmpty() && !message.trim().equalsIgnoreCase("")) {
					sessionBackingBean.showMessage("Warning", message, "WARNING_DIALOG");
					return;
				} else {
					sessionBackingBean.showMessage("Warning", "Please save photos before moving back",
							"WARNING_DIALOG");
					return;
				}
			}

			FacesContext fcontext = FacesContext.getCurrentInstance();
			fcontext.getExternalContext().getSessionMap().put("QUEUE_NO", tempQueueNo);
			fcontext.getExternalContext().getSessionMap().put("APPLICATION_NO", applicationNo);

			if (vehicleInspection != null && !vehicleInspection.isEmpty()
					&& !vehicleInspection.trim().equalsIgnoreCase("")
					&& vehicleInspection.trim().equalsIgnoreCase("view")) {
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/InfoNTC/pages/vehicleInspectionSet/viewVehicleInspection.xhtml");
			} else {
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/InfoNTC/pages/vehicleInspectionSet/vehicleInspectionInfo.xhtml");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** photo upload success method start **/
	public void photoUploadSuccessAction() {
		FacesContext fcontext = FacesContext.getCurrentInstance();
		fcontext.getExternalContext().getSessionMap().put("QUEUE_NO", tempQueueNo);
		fcontext.getExternalContext().getSessionMap().put("APPLICATION_NO", applicationNo);

		if (vehicleInspection != null && !vehicleInspection.isEmpty() && !vehicleInspection.trim().equalsIgnoreCase("")
				&& vehicleInspection.trim().equalsIgnoreCase("main_other")) {
			vehicleInspection = null;

			try {
				clearSessionData();
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/InfoNTC/pages/vehicleInspectionSet/otherVehicleInspection.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (vehicleInspection != null && !vehicleInspection.isEmpty()
				&& !vehicleInspection.trim().equalsIgnoreCase("")
				&& vehicleInspection.trim().equalsIgnoreCase("view_other")) {
			vehicleInspection = null;

			try {
				clearSessionData();
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/InfoNTC/pages/vehicleInspectionSet/otherInspectionEditView.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('saveSuccessWv').hide();");
	}

	/** photo upload success method end **/

	/** cancel uploaded photos start **/
	public void cancelPhotos() {
		RequestContext.getCurrentInstance().execute("PF('calcelPhotoSccss').show()");
	}

	/** cancel uploaded photos end **/

	/** cancel uploaded photos for view edit start **/
	public void cancelPhotos2() {
		try {

			if ((vehicleInspection != null && !vehicleInspection.isEmpty()
					&& !vehicleInspection.trim().equalsIgnoreCase("")
					&& vehicleInspection.trim().equalsIgnoreCase("main_other"))) {

				clearSessionData();
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/InfoNTC/pages/vehicleInspectionSet/otherVehicleInspection.xhtml");

			} else if ((vehicleInspection != null && !vehicleInspection.isEmpty()
					&& !vehicleInspection.trim().equalsIgnoreCase("")
					&& vehicleInspection.trim().equalsIgnoreCase("view_other"))) {

				clearSessionData();
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/InfoNTC/pages/vehicleInspectionSet/viewOtherInspection.xhtml");

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** cancel uploaded photos for view edit end **/

	/** upload vehicle vehicle cancel button start **/
	public void photoUploadCancelAction() {
		deleteVehicleOwnerImage();
		deleteVehicleImages("1");
		deleteVehicleImages("2");
		deleteVehicleImages("3");
		deleteVehicleImages("4");
		deleteVehicleImages("5");
		deleteVehicleImages("6");

		FacesContext fcontext = FacesContext.getCurrentInstance();
		fcontext.getExternalContext().getSessionMap().put("VEHICLE_NO", uploadImageDTO.getVehicleNo());
		fcontext.getExternalContext().getSessionMap().put("APPLICATION_NO", uploadImageDTO.getApplicationNo());
		FacesContext fcontext2 = FacesContext.getCurrentInstance();
		fcontext2.getExternalContext().getSessionMap().put("QUEUE_NO", tempQueueNo);

		RequestContext.getCurrentInstance().execute("PF('calcelPhotoSccss').hide()");

		try {

			if ((vehicleInspection != null && !vehicleInspection.isEmpty()
					&& !vehicleInspection.trim().equalsIgnoreCase("")
					&& vehicleInspection.trim().equalsIgnoreCase("main_other"))) {

				clearSessionData();
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/InfoNTC/pages/vehicleInspectionSet/otherVehicleInspection.xhtml");

			} else if ((vehicleInspection != null && !vehicleInspection.isEmpty()
					&& !vehicleInspection.trim().equalsIgnoreCase("")
					&& vehicleInspection.trim().equalsIgnoreCase("view_other"))) {

				clearSessionData();
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/InfoNTC/pages/vehicleInspectionSet/viewOtherInspection.xhtml");

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** upload vehicle vehicle cancel button end **/

	/** permit renewal vehicle owner data details add to folder and DB start **/
	public UploadImageDTO renewalVehicleOwnerDataSave(String ownerImagePath, String currAppNo) {

		String path = null;

		UploadImageDTO uploadedImageDTO = new UploadImageDTO();
		try {

			InputStream is = new FileInputStream(ownerImagePath);
			String extension = "png";

			Properties props = PropertyReader.loadPropertyFile();
			String imagePath = props.getProperty("vehicle.inspection.upload.photo.path");

			ownerPhotoStream = is;
			BufferedImage imBuff = ImageIO.read(ownerPhotoStream);

			String tempPath = imagePath + currAppNo + File.separator;
			path = tempPath + "vehicleOwner." + extension;

			ownerImageUpload = true;

			File theDir = new File(tempPath);

			if (!theDir.mkdirs()) {
				theDir.mkdir();
				File outputfile = new File(tempPath + "vehicleOwner." + "png");
				ImageIO.write(imBuff, "png", outputfile);
			} else {
				theDir.mkdir();
				File outputfile = new File(tempPath + "vehicleOwner." + "png");
				ImageIO.write(imBuff, "png", outputfile);
			}

			byte[] bytes = IOUtils.toByteArray(is);

			BufferedImage personImage;
			personImage = ImageIO.read(new File(ownerImagePath));
			BufferedImage resized = resize(personImage, 500, 500);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(resized, "png", bos);
			permitOwnerFaceImage = bos.toByteArray();

			/** add images to physical path of renewal application number start **/
			uploadedImageDTO = saveImagesToPhysicalPath(uploadImageDTO, currAppNo);
			uploadedImageDTO.setApplicationNo(currAppNo);
			/** add images to physical path of renewal application number end **/

			/** save data in database temporary start **/
			vehicleInspectionService.insertVehicleUploadPhotosToNT_T_OWNER_VEHI_IMAGES(uploadedImageDTO,
					sessionBackingBean.getLoginUser(), "N");// update is doing by this method as well
			/** save data in database temporary end **/

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		return uploadedImageDTO;
	}

	/** permit renewal vehicle owner data details add to folder and DB end **/

	private UploadImageDTO saveImagesToPhysicalPath(UploadImageDTO dto, String generatesAppNum) {
		InputStream is = null;
		OutputStream os = null;
		String extension = "png";
		UploadImageDTO returnDTO = new UploadImageDTO();
		try {
			if (dto != null) {

				Properties props = PropertyReader.loadPropertyFile();
				String imagePath = props.getProperty("vehicle.inspection.upload.photo.path");
				String tempPath = imagePath + generatesAppNum + File.separator;

				File theDir = new File(tempPath);

				if (!theDir.mkdirs()) {
					theDir.mkdir();
				} else {
					theDir.mkdir();
				}

				/** owner image start */
				if (dto.getVehicleOwnerPhotoPath() != null && !dto.getVehicleOwnerPhotoPath().isEmpty()
						&& !dto.getVehicleOwnerPhotoPath().trim().equalsIgnoreCase("")) {
					String destPath = tempPath + "vehicleOwner." + extension;
					is = new FileInputStream(dto.getVehicleOwnerPhotoPath());
					os = new FileOutputStream(destPath);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = is.read(buffer)) > 0) {
						os.write(buffer, 0, length);
					}

					returnDTO.setVehicleOwnerPhotoPath(destPath);
				}
				/** owner image end */

				/** vehicle image one start **/
				if (dto.getFirstVehiImagePath() != null && !dto.getFirstVehiImagePath().isEmpty()
						&& !dto.getFirstVehiImagePath().trim().equalsIgnoreCase("")) {
					String destPath = tempPath + "vehiclePhoto1." + extension;
					is = new FileInputStream(dto.getFirstVehiImagePath());
					os = new FileOutputStream(destPath);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = is.read(buffer)) > 0) {
						os.write(buffer, 0, length);
					}

					returnDTO.setFirstVehiImagePath(destPath);
				}
				/** vehicle image one end **/

				/** vehicle image two start **/
				if (dto.getSecondVehiImagePath() != null && !dto.getSecondVehiImagePath().isEmpty()
						&& !dto.getSecondVehiImagePath().trim().equalsIgnoreCase("")) {
					String destPath = tempPath + "vehiclePhoto2." + extension;
					is = new FileInputStream(dto.getSecondVehiImagePath());
					os = new FileOutputStream(destPath);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = is.read(buffer)) > 0) {
						os.write(buffer, 0, length);
					}

					returnDTO.setSecondVehiImagePath(destPath);
				}
				/** vehicle image two end **/

				/** vehicle image three start **/
				if (dto.getThirdVehiImagePath() != null && !dto.getThirdVehiImagePath().isEmpty()
						&& !dto.getThirdVehiImagePath().trim().equalsIgnoreCase("")) {
					String destPath = tempPath + "vehiclePhoto3." + extension;
					is = new FileInputStream(dto.getThirdVehiImagePath());
					os = new FileOutputStream(destPath);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = is.read(buffer)) > 0) {
						os.write(buffer, 0, length);
					}

					returnDTO.setThirdVehiImagePath(destPath);
				}
				/** vehicle image three end **/

				/** vehicle image four start **/
				if (dto.getForthVehiImagePath() != null && !dto.getForthVehiImagePath().isEmpty()
						&& !dto.getForthVehiImagePath().trim().equalsIgnoreCase("")) {
					String destPath = tempPath + "vehiclePhoto4." + extension;
					is = new FileInputStream(dto.getForthVehiImagePath());
					os = new FileOutputStream(destPath);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = is.read(buffer)) > 0) {
						os.write(buffer, 0, length);
					}

					returnDTO.setForthVehiImagePath(destPath);
				}
				/** vehicle image four end **/

				/** vehicle image five start **/
				if (dto.getFifthVehiImagePath() != null && !dto.getFifthVehiImagePath().isEmpty()
						&& !dto.getFifthVehiImagePath().trim().equalsIgnoreCase("")) {
					String destPath = tempPath + "vehiclePhoto5." + extension;
					is = new FileInputStream(dto.getFifthVehiImagePath());
					os = new FileOutputStream(destPath);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = is.read(buffer)) > 0) {
						os.write(buffer, 0, length);
					}

					returnDTO.setFifthVehiImagePath(destPath);
				}
				/** vehicle image five end **/

				/** vehicle image six start **/
				if (dto.getSixthVehiImagePath() != null && !dto.getSixthVehiImagePath().isEmpty()
						&& !dto.getSixthVehiImagePath().trim().equalsIgnoreCase("")) {
					String destPath = tempPath + "vehiclePhoto6." + extension;
					is = new FileInputStream(dto.getSixthVehiImagePath());
					os = new FileOutputStream(destPath);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = is.read(buffer)) > 0) {
						os.write(buffer, 0, length);
					}

					returnDTO.setSixthVehiImagePath(destPath);
				}
				/** vehicle image six end **/

			}
		} catch (ApplicationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return returnDTO;
	}

	public byte[] getPermitOwnerFaceImage() {
		return permitOwnerFaceImage;
	}

	public void setPermitOwnerFaceImage(byte[] permitOwnerFaceImage) {
		this.permitOwnerFaceImage = permitOwnerFaceImage;
	}

	public StreamedContent getPermitOwner() {
		return permitOwner;
	}

	public void setPermitOwner(StreamedContent permitOwner) {
		this.permitOwner = permitOwner;
	}

	public String getOwnerPhotoName() {
		return ownerPhotoName;
	}

	public void setOwnerPhotoName(String ownerPhotoName) {
		this.ownerPhotoName = ownerPhotoName;
	}

	public InputStream getOwnerPhotoStream() {
		return ownerPhotoStream;
	}

	public void setOwnerPhotoStream(InputStream ownerPhotoStream) {
		this.ownerPhotoStream = ownerPhotoStream;
	}

	public String getOwnerPhotoPath() {
		return ownerPhotoPath;
	}

	public void setOwnerPhotoPath(String ownerPhotoPath) {
		this.ownerPhotoPath = ownerPhotoPath;
	}

	public String getVehiclePhotoName() {
		return vehiclePhotoName;
	}

	public void setVehiclePhotoName(String vehiclePhotoName) {
		this.vehiclePhotoName = vehiclePhotoName;
	}

	public InputStream getVehiclePhotoStream() {
		return vehiclePhotoStream;
	}

	public void setVehiclePhotoStream(InputStream vehiclePhotoStream) {
		this.vehiclePhotoStream = vehiclePhotoStream;
	}

	public DefaultStreamedContent getPermtOwner() {
		return permtOwner;
	}

	public void setPermtOwner(DefaultStreamedContent permtOwner) {
		this.permtOwner = permtOwner;
	}

	public DefaultStreamedContent getFirstVehicleImage() {
		return firstVehicleImage;
	}

	public void setFirstVehicleImage(DefaultStreamedContent firstVehicleImage) {
		this.firstVehicleImage = firstVehicleImage;
	}

	public DefaultStreamedContent getSecondVehicleImage() {
		return secondVehicleImage;
	}

	public void setSecondVehicleImage(DefaultStreamedContent secondVehicleImage) {
		this.secondVehicleImage = secondVehicleImage;
	}

	public DefaultStreamedContent getThirdVehicleImage() {
		return thirdVehicleImage;
	}

	public void setThirdVehicleImage(DefaultStreamedContent thirdVehicleImage) {
		this.thirdVehicleImage = thirdVehicleImage;
	}

	public DefaultStreamedContent getFourthVehicleImage() {
		return fourthVehicleImage;
	}

	public void setFourthVehicleImage(DefaultStreamedContent fourthVehicleImage) {
		this.fourthVehicleImage = fourthVehicleImage;
	}

	public boolean isBackBtn() {
		return backBtn;
	}

	public void setBackBtn(boolean backBtn) {
		this.backBtn = backBtn;
	}

	public DefaultStreamedContent getFifthVehicleImage() {
		return fifthVehicleImage;
	}

	public void setFifthVehicleImage(DefaultStreamedContent fifthVehicleImage) {
		this.fifthVehicleImage = fifthVehicleImage;
	}

	public DefaultStreamedContent getSixthVehicleImage() {
		return sixthVehicleImage;
	}

	public void setSixthVehicleImage(DefaultStreamedContent sixthVehicleImage) {
		this.sixthVehicleImage = sixthVehicleImage;
	}

	public byte[] getFirstVehicleImg() {
		return firstVehicleImg;
	}

	public void setFirstVehicleImg(byte[] firstVehicleImg) {
		this.firstVehicleImg = firstVehicleImg;
	}

	public byte[] getSecondVehicleImg() {
		return secondVehicleImg;
	}

	public void setSecondVehicleImg(byte[] secondVehicleImg) {
		this.secondVehicleImg = secondVehicleImg;
	}

	public byte[] getThirdVehicleImg() {
		return thirdVehicleImg;
	}

	public void setThirdVehicleImg(byte[] thirdVehicleImg) {
		this.thirdVehicleImg = thirdVehicleImg;
	}

	public byte[] getFourthVehicleImg() {
		return fourthVehicleImg;
	}

	public void setFourthVehicleImg(byte[] fourthVehicleImg) {
		this.fourthVehicleImg = fourthVehicleImg;
	}

	public byte[] getFifthVehicleImg() {
		return fifthVehicleImg;
	}

	public void setFifthVehicleImg(byte[] fifthVehicleImg) {
		this.fifthVehicleImg = fifthVehicleImg;
	}

	public byte[] getSixthVehicleImg() {
		return sixthVehicleImg;
	}

	public void setSixthVehicleImg(byte[] sixthVehicleImg) {
		this.sixthVehicleImg = sixthVehicleImg;
	}

	public UploadedFile getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public UploadImageDTO getUploadImageDTO() {
		return uploadImageDTO;
	}

	public void setUploadImageDTO(UploadImageDTO uploadImageDTO) {
		this.uploadImageDTO = uploadImageDTO;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public VehicleInspectionService getVehicleInspectionService() {
		return vehicleInspectionService;
	}

	public void setVehicleInspectionService(VehicleInspectionService vehicleInspectionService) {
		this.vehicleInspectionService = vehicleInspectionService;
	}

	public boolean isOwnerImageUpload() {
		return ownerImageUpload;
	}

	public void setOwnerImageUpload(boolean ownerImageUpload) {
		this.ownerImageUpload = ownerImageUpload;
	}

	public UploadImageDTO getDeleteUploadImagesDTO() {
		return deleteUploadImagesDTO;
	}

	public void setDeleteUploadImagesDTO(UploadImageDTO deleteUploadImagesDTO) {
		this.deleteUploadImagesDTO = deleteUploadImagesDTO;
	}

	/*
	 * public boolean isVehiImg1Selected() { return vehiImg1Selected; }
	 * 
	 * public void setVehiImg1Selected(boolean vehiImg1Selected) {
	 * this.vehiImg1Selected = vehiImg1Selected; }
	 * 
	 * public boolean isVehiImg2Selected() { return vehiImg2Selected; }
	 * 
	 * public void setVehiImg2Selected(boolean vehiImg2Selected) {
	 * this.vehiImg2Selected = vehiImg2Selected; }
	 * 
	 * public boolean isVehiImg3Selected() { return vehiImg3Selected; }
	 * 
	 * public void setVehiImg3Selected(boolean vehiImg3Selected) {
	 * this.vehiImg3Selected = vehiImg3Selected; }
	 * 
	 * public boolean isVehiImg4Selected() { return vehiImg4Selected; }
	 * 
	 * public void setVehiImg4Selected(boolean vehiImg4Selected) {
	 * this.vehiImg4Selected = vehiImg4Selected; }
	 * 
	 * public boolean isVehiImg5Selected() { return vehiImg5Selected; }
	 * 
	 * public void setVehiImg5Selected(boolean vehiImg5Selected) {
	 * this.vehiImg5Selected = vehiImg5Selected; }
	 * 
	 * public boolean isVehiImg6Selected() { return vehiImg6Selected; }
	 * 
	 * public void setVehiImg6Selected(boolean vehiImg6Selected) {
	 * this.vehiImg6Selected = vehiImg6Selected; }
	 */

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getVehicleOwnerName() {
		return vehicleOwnerName;
	}

	public void setVehicleOwnerName(String vehicleOwnerName) {
		this.vehicleOwnerName = vehicleOwnerName;
	}

	public String getVehicleInspection() {
		return vehicleInspection;
	}

	public void setVehicleInspection(String vehicleInspection) {
		this.vehicleInspection = vehicleInspection;
	}

	public boolean isPhotosSaveSuccess() {
		return photosSaveSuccess;
	}

	public void setPhotosSaveSuccess(boolean photosSaveSuccess) {
		this.photosSaveSuccess = photosSaveSuccess;
	}

	public String getTempQueueNo() {
		return tempQueueNo;
	}

	public void setTempQueueNo(String tempQueueNo) {
		this.tempQueueNo = tempQueueNo;
	}

	public PaymentVoucherService getPaymentVoucherService() {
		return paymentVoucherService;
	}

	public void setPaymentVoucherService(PaymentVoucherService paymentVoucherService) {
		this.paymentVoucherService = paymentVoucherService;
	}

	public boolean isMainInsCancel() {
		return mainInsCancel;
	}

	public void setMainInsCancel(boolean mainInsCancel) {
		this.mainInsCancel = mainInsCancel;
	}

	public boolean isViewInsCancel() {
		return viewInsCancel;
	}

	public void setViewInsCancel(boolean viewInsCancel) {
		this.viewInsCancel = viewInsCancel;
	}

	public VehicleInspectionDTO getTaskDetWithAppDetDTO() {
		return taskDetWithAppDetDTO;
	}

	public void setTaskDetWithAppDetDTO(VehicleInspectionDTO taskDetWithAppDetDTO) {
		this.taskDetWithAppDetDTO = taskDetWithAppDetDTO;
	}

	public InspectionActionPointService getInspectionActionPointService() {
		return inspectionActionPointService;
	}

	public void setInspectionActionPointService(InspectionActionPointService inspectionActionPointService) {
		this.inspectionActionPointService = inspectionActionPointService;
	}

	public boolean isDisableSave() {
		return disableSave;
	}

	public void setDisableSave(boolean disableSave) {
		this.disableSave = disableSave;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		OtherInspectionUploadPhotoBackingBean.logger = logger;
	}

	public MigratedService getMigratedService() {
		return migratedService;
	}

	public void setMigratedService(MigratedService migratedService) {
		this.migratedService = migratedService;
	}

	public UploadImageDTO getUploadTempDTO() {
		return uploadTempDTO;
	}

	public void setUploadTempDTO(UploadImageDTO uploadTempDTO) {
		this.uploadTempDTO = uploadTempDTO;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
