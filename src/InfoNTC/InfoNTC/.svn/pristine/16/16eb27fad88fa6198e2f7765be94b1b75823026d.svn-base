package lk.informatics.ntc.view.beans;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import lk.informatics.ntc.model.dto.QueueNumberDTO;
import lk.informatics.ntc.model.dto.TransactionTypeDTO;
import lk.informatics.ntc.model.exception.ApplicationException;
import lk.informatics.ntc.model.service.CommonService;
import lk.informatics.ntc.model.service.QueueManagementService;
import lk.informatics.ntc.view.util.PropertyReader;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "queueNoGenerateBackingBean")
@ViewScoped
public class QueueNoGenerateBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	private QueueNumberDTO queueNumberDTO;
	private boolean permitEnable;
	private boolean vehicleEnable;
	private boolean appNoEnabe;
	private boolean appNoStarEnabe;
	private boolean rendrRefNoEnabe;
	private List<TransactionTypeDTO> transTypeList;// transaction Type LOV
	private QueueManagementService queueManagementService;
	private CommonService commonService;
	private StreamedContent files;

	private boolean downloadStart = false;

	private static final String TENDER = getPropertyFileVale("queue.transaction.type.code.tender");// 01
	private static final String INSPECTION = "112";
	private static final String PERMIT = getPropertyFileVale("queue.transaction.type.code.new.permit");// 03
	private static final String RENEWAL = getPropertyFileVale("queue.transaction.type.code.renewal");// 04
	private static final String AMMENDMENT = "111";// getPropertyFileVale("queue.transaction.type.code.ammendment");//05
	private static final String PAYMENT = getPropertyFileVale("queue.transaction.type.code.payment");// 06
	private static final String CANCEL = getPropertyFileVale("queue.transaction.type.code.cancel");// 07
	private static final String SURVEY = getPropertyFileVale("queue.transaction.type.code.survey");// 08
	private static final String INQUIRY = getPropertyFileVale("queue.transaction.type.code.inquiry");// 09
	private static final String AMEND_BUS = getPropertyFileVale("queue.transaction.type.code.ammendment.bus");// 13
	private static final String AMEND_SERVICE = getPropertyFileVale("queue.transaction.type.code.ammendment.service");// 05
	private static final String AMEND_OWNER = getPropertyFileVale("queue.transaction.type.code.ammendment.bus.owner");// 14
	private static final String AMEND_OWNER_BUS = getPropertyFileVale(
			"queue.transaction.type.code.ammendment.owner.bus.owner");// 15
	private static final String AMEND_SERVICE_BUS = getPropertyFileVale(
			"queue.transaction.type.code.ammendment.service.bus.owner");// 16
	private static final String INSPECTION_NORMAL = getPropertyFileVale("queue.transaction.type.code.inspection");// 02
	private static final String INSPECTION_AMEND = getPropertyFileVale(
			"queue.transaction.type.code.inspection.ammendment");// 17
	private static final String AMMENDMENT_SERVICE = getPropertyFileVale(
			"queue.transaction.type.code.amendment.service");// 21
	private static final String AMMENDMENT_ROUTE = getPropertyFileVale("queue.transaction.type.code.amendment.route");// 22
	private static final String AMMENDMENT_END = getPropertyFileVale("queue.transaction.type.code.amendment.end");// 23

	private static final String OTHER_INSPECTION = getPropertyFileVale("queue.transaction.type.code.other.inspection");// OI
	private static final String OTHER_INSPECTION_COMPLAIN = getPropertyFileVale(
			"queue.transaction.type.code.other.inspection.complain");// 115
	private static final String OTHER_INSPECTION_INQUIRY = getPropertyFileVale(
			"queue.transaction.type.code.other.inspection.inquiry");// 116
	private static final String OTHER_INSPECTION_SITE_VISIT = getPropertyFileVale(
			"queue.transaction.type.code.other.inspection.site.visit");// 117

	private String tenderStyle;
	private String inspection;
	private String newPermit;
	private String renewal;
	private String ammendment;
	private String payment;
	private String cancel;
	private String inquiry;
	private String normal;
	private String priority;
	private boolean queueServeiceVisible;
	private boolean genQueueClear;
	private boolean ammendmentVisible;
	private boolean genTokenVisible;
	private String queueNumber;
	private String title;
	private boolean ammendBus;
	private boolean inspectionVisible;
	private boolean ammendmentServiceVisible;
	private String otherInspection;
	private boolean otherInspectionVisible;
	private boolean displayPriorityBtn;

	private List<QueueNumberDTO> queueList;
	private boolean queueEnable;

	@PostConstruct
	public void init() {
		queueNumberDTO = new QueueNumberDTO();
		queueManagementService = (QueueManagementService) SpringApplicationContex.getBean("queueManagementService");
		commonService = (CommonService) SpringApplicationContex.getBean("commonService");

		loadData();
	}

	public void loadData() {

		transTypeList = new ArrayList<TransactionTypeDTO>();
		transTypeList = queueManagementService.retrieveTransactionTypeList();// get
																				// data
																				// to
																				// transaction
																				// type
																				// LOV
		permitEnable = false;
		vehicleEnable = false;
		rendrRefNoEnabe = false;
		appNoEnabe = false;// always enable except LOV value=INSPECTION,INQUIRY
		appNoStarEnabe = false;
		queueServeiceVisible = false;
		genQueueClear = false;
		ammendmentVisible = false;
		ammendmentServiceVisible = false;
		otherInspectionVisible = false;
		displayPriorityBtn = true;

		queueNumber = null;
		title = null;
		ammendBus = false;

		buttonStyleRemove();
		queueTypeStyleRemove();

		RequestContext.getCurrentInstance().update("@form");

	}

	/** GET TRANSATION TYPE START **/
	public void onTransactionTypeChange() {

		if (queueNumberDTO.getTransTypeCode() != null && !queueNumberDTO.getTransTypeCode().isEmpty()
				&& !queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase("")) {

			String transTypeCode = queueNumberDTO.getTransTypeCode();
			queueNumberDTO = new QueueNumberDTO();
			queueNumberDTO.setTransTypeCode(transTypeCode);

			if (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(PERMIT)) {
				// 03 - Permit, 04 - Renewal
				buttonStyleRemove();
				newPermit = "true";
				title = "New Permit";
				permitEnable = false;
				vehicleEnable = true;
				rendrRefNoEnabe = false;
				appNoEnabe = false;
				appNoStarEnabe = false;
				queueEnable = true;
				queueList = queueManagementService.getQueuesForTransactionType("OP");

			} else if (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(RENEWAL)) {
				renewal = "true";
				title = "Renewal";
				permitEnable = true;
				vehicleEnable = false;
				rendrRefNoEnabe = false;
				appNoEnabe = false;
				appNoStarEnabe = false;
				queueEnable = true;
				queueList = queueManagementService.getQueuesForTransactionType("OP");

			} else if (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(INSPECTION_NORMAL)) {
				// 02 - Inspection - in inspection only needed to enter vehicle
				// number
				buttonStyleRemove();
				inspection = "true";
				vehicleEnable = true;
				permitEnable = false;
				rendrRefNoEnabe = false;// only enable when LOV value=TENDER
				appNoEnabe = false;// always enable except LOV
									// value=INSPECTION,INQUIRY
				appNoStarEnabe = false;
				inspectionVisible = false;
				title = "Inspection";
			} else if (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(TENDER)) {
				buttonStyleRemove();
				tenderStyle = "true";
				permitEnable = false;
				vehicleEnable = false;
				appNoEnabe = false;// always enable except LOV
									// value=INSPECTION,INQUIRY
				appNoStarEnabe = false;
				inspectionVisible = false;
				rendrRefNoEnabe = true;// only enable when LOV value=TENDER
				title = "Tender";
			} else if (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(INQUIRY)) {
				buttonStyleRemove();
				inquiry = "true";
				permitEnable = false;
				vehicleEnable = true;
				rendrRefNoEnabe = false;// only enable when LOV value=TENDER
				appNoEnabe = true;
				inspectionVisible = false;
				appNoStarEnabe = false;
				title = "Inquiry";
			} else if (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMEND_BUS)
					|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMEND_SERVICE)
					|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMEND_OWNER)
					|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMEND_OWNER_BUS)
					|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMEND_SERVICE_BUS)) {

				if (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMEND_BUS)) {
					ammendBus = true;
				}
				ammendment = "true";
				permitEnable = true;
				vehicleEnable = false;
				rendrRefNoEnabe = false;// only enable when LOV value=TENDER
				appNoEnabe = false;
				appNoStarEnabe = false;
				title = "Ammendment";
				genTokenVisible = true;
				inspectionVisible = false;
				ammendmentVisible = false;
				queueEnable = true;
				queueList = queueManagementService.getQueuesForTransactionType("OP");

			} else if (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(INSPECTION_AMEND)) {
				buttonStyleRemove();
				inspection = "false";
				vehicleEnable = false;// false;//changed on 22/10/2019 because
										// in kiosk this is taking from vehicle
				permitEnable = true;
				rendrRefNoEnabe = false;// only enable when LOV value=TENDER
				appNoEnabe = true;// true;//changed on 22/10/2019 because in
									// kiosk this is taking from vehicle num
									// not app num
				appNoStarEnabe = true;
				inspectionVisible = false;
				title = "Inspection for Amendment";
				queueEnable = true;
				queueList = queueManagementService.getQueuesForTransactionType("OP");

			} else {
				// ammendments, payments, cancel
				buttonStyleRemove();
				if (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMMENDMENT)) {
					ammendment = "true";
					permitEnable = true;
					vehicleEnable = true;
					rendrRefNoEnabe = false;// only enable when LOV value=TENDER
					appNoEnabe = false;
					inspectionVisible = false;
					appNoStarEnabe = false;
					title = "Ammendment";
				} else if (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(PAYMENT)) {
					payment = "true";
					title = "Payment";
					permitEnable = false;
					vehicleEnable = false;
					rendrRefNoEnabe = false;// only enable when LOV value=TENDER
					appNoEnabe = true;// always enable except LOV
										// value=INSPECTION,INQUIRY
					inspectionVisible = false;
					appNoStarEnabe = true;
				} else if (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(CANCEL)) {
					cancel = "true";
					title = "Cancel";
					permitEnable = false;
					vehicleEnable = false;
					rendrRefNoEnabe = false;// only enable when LOV value=TENDER
					appNoEnabe = true;// always enable except LOV
										// value=INSPECTION,INQUIRY
					inspectionVisible = false;
					appNoStarEnabe = true;
				}

			}
		} else {
			buttonStyleRemove();
			permitEnable = false;
			vehicleEnable = false;
			rendrRefNoEnabe = false;// only enable when LOV value=TENDER
			appNoEnabe = true;// always enable except LOV
								// value=INSPECTION,INQUIRY
			appNoStarEnabe = true;
			inspectionVisible = false;
		}

	}

	/** GET TRANSATION TYPE END **/

	/**
	 * GENERATE TOKEN START
	 * 
	 * @throws JRException
	 **/
	public StreamedContent generateTokenListener() throws JRException {
		files = null;
		List<QueueNumberDTO> dtoList = new ArrayList<QueueNumberDTO>();
		boolean renewal = false;

		if (queueNumberDTO.getTransTypeCode() != null && !queueNumberDTO.getTransTypeCode().isEmpty()
				&& !queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase("")) {

			/** VALIDATE APPLICATION NO **/
			if (validateApplicationNumber(queueNumberDTO)) {

				/** VALIDATE PERMIT NO **/
				if (validatePermitNo(queueNumberDTO)) {

					/** VALIDATE VEHICLE NO **/
					if (validateVehicleNumber(queueNumberDTO)) {

						/** VALIDATE TOKEN NO **/
						if (validateTokenNo(queueNumberDTO)) {

							/** VALIDATE TENDER REFERENCE NO **/
							boolean validateTenderRefNo = true;
							if (validateTenderRefNo) {

								// validate queue service priority and normal
								if (queueNumberDTO.getQueueService() != null
										&& !queueNumberDTO.getQueueService().isEmpty()
										&& !queueNumberDTO.getQueueService().trim().equalsIgnoreCase("")) {

									/*
									 * Adding new CR for queue no generate
									 * method
									 */
									if (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(PERMIT)) {

										if (queueManagementService
												.isVehicleDetailsFoundInTaskDetails(queueNumberDTO.getVehicleNo())) {
											queueNumberDTO.setTransTypeCode(PERMIT);
										} else {
											queueNumberDTO.setTransTypeCode(INSPECTION_NORMAL);
										}

									} else if (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(RENEWAL)) {

										QueueNumberDTO dto = queueManagementService
												.getPermitInfo(queueNumberDTO.getPermitNo());

										if (dto.isPermitInfoFound() == true) {

											boolean isFound = queueManagementService
													.isApplicatioinDetailsFoundInTaskDetails(dto.getApplicationNo());

											queueNumberDTO.setVehicleNo(dto.getVehicleNo());

											if (isFound) {
												queueNumberDTO.setApplicationNo(dto.getApplicationNo());
												QueueNumberDTO proceedDTO = queueManagementService
														.getInspectionProceedStatus(queueNumberDTO.getVehicleNo(),
																"PI");

												if (proceedDTO.getInspectionStatus().equals("I")
														&& proceedDTO.getProceedStatus().equals("N")) {

													sessionBackingBean.showMessage("Warning",
															"Vehicle Inspection Incomplete. Please Refer to High Authorized Officer.",
															"WARNING_DIALOG");
													return null;

												} else {
													queueNumberDTO.setTransTypeCode(RENEWAL);
												}

											} else {
												queueNumberDTO.setTransTypeCode(INSPECTION_NORMAL);
											}

										} else {
											sessionBackingBean.showMessage("Warning",
													"Cannot find Application No. for " + queueNumberDTO.getPermitNo(),
													"WARNING_DIALOG");
											return null;
										}

									} else if (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMEND_BUS)
											|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMEND_SERVICE)
											|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMEND_OWNER)
											|| queueNumberDTO.getTransTypeCode().trim()
													.equalsIgnoreCase(AMEND_OWNER_BUS)
											|| queueNumberDTO.getTransTypeCode().trim()
													.equalsIgnoreCase(AMEND_SERVICE_BUS)
											|| queueNumberDTO.getTransTypeCode().trim()
													.equalsIgnoreCase(AMMENDMENT_SERVICE)
											|| queueNumberDTO.getTransTypeCode().trim()
													.equalsIgnoreCase(AMMENDMENT_ROUTE)
											|| queueNumberDTO.getTransTypeCode().trim()
													.equalsIgnoreCase(AMMENDMENT_END)) {

										QueueNumberDTO dto = queueManagementService
												.getPermitInfo(queueNumberDTO.getPermitNo());

										if (dto.isPermitInfoFound() == true) {

											queueNumberDTO.setVehicleNo(dto.getVehicleNo());

										} else {
											sessionBackingBean.showMessage("Warning",
													"Cannot find data for " + queueNumberDTO.getPermitNo(),
													"WARNING_DIALOG");
											return null;
										}

									} else if (queueNumberDTO.getTransTypeCode().trim()
											.equalsIgnoreCase(INSPECTION_AMEND)) {

										QueueNumberDTO dto = queueManagementService.getAmendmentVehicleNo(
												queueNumberDTO.getPermitNo(), queueNumberDTO.getApplicationNo());

										if (dto.isPermitInfoFound()) {

											queueNumberDTO.setVehicleNo(dto.getVehicleNo());
											queueNumberDTO.setApplicationNo(dto.getApplicationNo());

										} else {
											sessionBackingBean.showMessage("Warning", "Cannot find data.",
													"WARNING_DIALOG");
											return null;
										}

									}

									/**
									 * If transaction type is inquiry no need to
									 * check anything just give a token number
									 * start
									 **/
									if (queueNumberDTO.getTransTypeCode().equalsIgnoreCase(INQUIRY)) {
										boolean valid = validateVehicleNoAndAppNoForInquiry(
												queueNumberDTO.getVehicleNo(), queueNumberDTO.getApplicationNo());
										if (!valid) {
											sessionBackingBean.showMessage("Warning",
													"Please enter valid Vehicle No. or Application No. ",
													"WARNING_DIALOG");
											return null;
										}
										if (queueNumberDTO.getApplicationNo() != null
												&& !queueNumberDTO.getApplicationNo().isEmpty()
												&& !queueNumberDTO.getApplicationNo().trim().equalsIgnoreCase("")) {
											boolean isExist = commonService
													.checkApplicationNoAvailable(queueNumberDTO.getApplicationNo());
											if (!isExist) {
												sessionBackingBean.showMessage("Warning", "Invalid Application No.",
														"WARNING_DIALOG");
												return null;
											}
										}

										tokenGenerateForInquiry(queueNumberDTO);
										clearListener();
										return files;
									}
									/**
									 * If transaction type is inquiry no need to
									 * check anything just give a token number
									 * end
									 **/

									/**
									 * If transaction type is other
									 * inspection(complain/inquiry/site visit)
									 * no need to check anything just give a
									 * token number start
									 **/
									if (queueNumberDTO.getTransTypeCode().equalsIgnoreCase(OTHER_INSPECTION_COMPLAIN)
											|| queueNumberDTO.getTransTypeCode()
													.equalsIgnoreCase(OTHER_INSPECTION_INQUIRY)
											|| queueNumberDTO.getTransTypeCode()
													.equalsIgnoreCase(OTHER_INSPECTION_SITE_VISIT)) {
										// validate the permit number
										QueueNumberDTO dto = queueManagementService
												.getPermitInfo(queueNumberDTO.getPermitNo());

										if (dto.isPermitInfoFound() == false) {
											sessionBackingBean.showMessage("Warning", "Invalid Permit No.",
													"WARNING_DIALOG");
											return null;
										} else {
											// set vehicle number to
											// queueNumberDTO
											queueNumberDTO.setVehicleNo(dto.getVehicleNo());
										}
										
										/* Code commented on 07/06/2024 dhananjika.d */
										
//										boolean isTokenAvailable = queueManagementService.checkTokenNumberAvailableForQueueNoGenerate(queueNumberDTO.getTokenNo(), "O");
//										if(isTokenAvailable == false) {
//											sessionBackingBean.showMessage("Warning", "Token has already in progress or used.", "WARNING_DIALOG");
//											return null;
//										}

										tokenGenerateForInquiry(queueNumberDTO);
										clearListener();
										return files;
									}
									/**
									 * If transaction type is other
									 * inspection(complain/inquiry/site visit)
									 * no need to check anything just give a
									 * token number end
									 **/

									/**
									 * If transaction type is inspection check
									 * if vehicle no is in the system start
									 **/
									if (queueNumberDTO.getTransTypeCode().equalsIgnoreCase(INSPECTION_NORMAL)) {

										/***/
										// should not allow to generate multiple
										// IN token for same vehicle no in
										// current
										// day
										// added by Dinushi Ranasinghe on
										// 04-03-2020
										boolean isExistSameDay = queueManagementService
												.validateTokenforSameDay(queueNumberDTO.getVehicleNo());
										if (isExistSameDay) {
											sessionBackingBean.showMessage("Warning",
													"Renewal process is ongoing / completed for Today.",
													"WARNING_DIALOG");
											return null;
										}
										/***/

										// 27/10/2019 check AM 100 C or AM106 C
										// or AM102 C or AM101 C or AM103 C ->
										// if
										// so dont let take queue numbers
										boolean taskCompleteInsAm = queueManagementService
												.checkTaskHistoryForInspectionAmmendment(queueNumberDTO.getVehicleNo());
										if (taskCompleteInsAm) {
											sessionBackingBean.showMessage("Warning",
													"Inspection token cannot be generated", "WARNING_DIALOG");
											return null;
										}
										// 27/10/2019 check AM 100 C or AM106 C
										// or AM102 C or AM101 C or AM103 C ->
										// if
										// so dont let take queue numbers

										// 15/10/2019 check PM100 C task
										boolean taskComplete = queueManagementService
												.checkTaskHistoryForInspectionComplete(queueNumberDTO.getVehicleNo());
										if (taskComplete) {
											sessionBackingBean.showMessage("Warning", "Inspection process is ongoing",
													"WARNING_DIALOG");
											return null;
										}
										// 15/10/2019 check PM100 C task

										// 15/10/2019 check PM101 C task
										boolean exist = queueManagementService
												.checkTaskHistoryForInspectionUploadPhotosComplete(
														queueNumberDTO.getVehicleNo());
										if (exist) {
											sessionBackingBean.showMessage("Warning",
													"Inspection comepleted. Please take a renewal token",
													"WARNING_DIALOG");
											return null;
										}
										// 15/10/2019 check PM101 C task

										// 30/5/2019 check PR200 O task
										String mssage = queueManagementService
												.checkTaskHistoryForInspection(queueNumberDTO.getVehicleNo());
										if (mssage != null && !mssage.isEmpty()
												&& !mssage.trim().equalsIgnoreCase("")) {
											sessionBackingBean.showMessage("Warning", mssage, "WARNING_DIALOG");
											return null;
										}
										// 30/5/2019 check PR200 O task

										// 15/10/2019 check inspection token is
										// taken today
										boolean inspectionOngoing = queueManagementService
												.checkInspectionTokeGenerated(queueNumberDTO.getVehicleNo());
										if (inspectionOngoing) {
											sessionBackingBean.showMessage("Warning",
													"Token has already generated for today.", "WARNING_DIALOG");
											return null;
										}
										// 15/10/2019 check inspection token is
										// taken today

										/*
										 * check data available in
										 * nt_t_pm_application
										 */
										QueueNumberDTO dto = queueManagementService.validateUserInputValues(queueNumberDTO);
										boolean isAvailable = queueManagementService.checkTokenNumberAvailableForQueueNoGenerate(queueNumberDTO.getTokenNo(), "O");
										if (dto != null || isAvailable == false) {
											sessionBackingBean.showMessage("Warning", "Token has already in progress or generated.", "WARNING_DIALOG");
											return null;
										} else {
											tokenGenerateForInquiry(queueNumberDTO);
											clearListener();
											return files;
										}

									}
									/**
									 * If transaction type is inspection check
									 * if vehicle no is in the system end
									 **/

									/**
									 * If transaction type inspection amendment
									 * start
									 **/
									if (queueNumberDTO.getTransTypeCode().equalsIgnoreCase(INSPECTION_AMEND)) {

										QueueNumberDTO dto = queueManagementService
												.validateUserInputValues(queueNumberDTO);
										if (dto != null && dto.getPermitNo() != null && !dto.getPermitNo().isEmpty()) {
											queueNumberDTO.setPermitNo(dto.getPermitNo());
										}

										if (dto != null && dto.getApplicationNo() != null
												&& !dto.getApplicationNo().isEmpty()) {
											queueNumberDTO.setApplicationNo(dto.getApplicationNo());
											queueNumberDTO.setVehicleNo(queueNumberDTO.getVehicleNo());
										}

										if (dto != null && dto.getApplicationNo() != null
												&& !dto.getApplicationNo().isEmpty()
												&& !dto.getApplicationNo().trim().equalsIgnoreCase("")) {
											boolean taskValid = queueManagementService
													.validateTasksOfApplicatinNum(dto.getApplicationNo());
											if (!taskValid) {
												sessionBackingBean.showMessage("Warning",
														"Application number is not eligible", "WARNING_DIALOG");
												return null;
											}
										}

										/* Check data available for */
										QueueNumberDTO queueDto = queueManagementService
												.validateUserInputValueForInspectionAmmendment(queueNumberDTO);

										if (queueDto != null) {
											queueDto.setTransTypeCode(queueNumberDTO.getTransTypeCode());
											// **Validate same details generated
											// queue token for same queue type
											// again
											// start**//
											boolean generateAgain = queueManagementService.validateDuplicateQueueNoGenerate(queueDto);
											boolean isTokenAvailable = queueManagementService.checkTokenNumberAvailableForQueueNoGenerate(queueNumberDTO.getTokenNo(), "O");
											if (!generateAgain || isTokenAvailable == false) {
												sessionBackingBean.showMessage("Warning", "Token has already in progress or generated.","WARNING_DIALOG");
												return null;
											}
											// **Validate same details generated
											// queue token for same queue type
											// again
											// end**//*

											tokenGenerateForInquiry(queueNumberDTO);
											clearListener();
											return files;
										}
									}
									/**
									 * If transaction type inspection amendment
									 * end
									 **/

									/**
									 * If transation type is New Permit check if
									 * it's pm_is_back_log not equal to 'Y'
									 * start
									 **/
									if (queueNumberDTO.getTransTypeCode().equalsIgnoreCase(PERMIT)) {
										// TODO ask whether select needs to be
										// checked with pm_created_date today?
										boolean isBackLog = queueManagementService.validateApplicationNoIsBackLog(queueNumberDTO.getApplicationNo());

										if (isBackLog) {
											// if it is backlog application
											// should not let proceed with
											// application
											// number
											sessionBackingBean.showMessage("Warning",
													"Entered Application No. is from backlog app. Please enter another Application No.",
													"WARNING_DIALOG");
											return null;
										}
									}
									/**
									 * If transaction type is New Permit check
									 * if it's pm_is_back_log not equal to 'Y'
									 * end
									 **/

									/**
									 * if transaction type is RENEWAL should
									 * check is_new_permit='N' or
									 * is_back_log='Y' start
									 **/
									if (queueNumberDTO.getTransTypeCode().equalsIgnoreCase(RENEWAL)
											|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(PERMIT)) {
										renewal = true;
									}
									/**
									 * if transaction type is RENEWAL should
									 * check is_new_permit='N' or
									 * is_back_log='Y' end
									 **/

									/**
									 * check amendments validation whether to
									 * check if the entered data is active start
									 **/
									if (queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_BUS)
											|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_SERVICE)
											|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_OWNER)
											|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_OWNER_BUS)
											|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_SERVICE_BUS)
											|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMMENDMENT_SERVICE)
											|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMMENDMENT_ROUTE)
											|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMMENDMENT_END)) {

										// 25/10/2019 start
										/**
										 * check new validation -> if PM400 C
										 * let the user take the token or else
										 * give the application number to
										 * continue the process
										 **/
										String appNum = queueManagementService
												.checkProcessIsDoneForAmmendment(queueNumberDTO);
										if (appNum != null && !appNum.isEmpty()
												&& !appNum.trim().equalsIgnoreCase("")) {
											sessionBackingBean.showMessage("Warning",
													"Please complete ammendment process. Your application no is "
															+ appNum,
													"WARNING_DIALOG");
											return null;
										}

										/**
										 * check new validation -> if PM400 C
										 * let the user take the token or else
										 * give the application number to
										 * continue the process
										 **/
										// 25/10/2019 end

										boolean valid = queueManagementService.checkAmmenmentValidation(queueNumberDTO);
										if (!valid) {
											sessionBackingBean.showMessage("Warning",
													"Entered details are not Active stage", "WARNING_DIALOG");
											return null;
										}

										boolean ongoing = queueManagementService
												.checkAmmenmentValidationForOngoingQueueNumbers(queueNumberDTO);
										if (ongoing) {
											sessionBackingBean.showMessage("Warning",
													"Queue Number has already genetated for entered details",
													"WARNING_DIALOG");
											return null;
										}
									}
									/**
									 * check amendments validation whether to
									 * check if the entered data is active end
									 **/

									/** Check entered inputs are valid start **/
									String returnVal = validateBeforeGenerate(queueNumberDTO, renewal);

									if (returnVal != null && !returnVal.isEmpty()
											&& !returnVal.trim().equalsIgnoreCase("")) {
										sessionBackingBean.showMessage("Warning", returnVal, "WARNING_DIALOG");
										return null;
									}
									/** Check entered inputs are valid end **/

									/**
									 * Check the status of queue number start
									 **/
									// if it's amendments no need to check
									// transaction type
									if (!queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_BUS)
											&& !queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_SERVICE)
											&& !queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_OWNER)
											&& !queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_SERVICE_BUS)
											&& !queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_OWNER_BUS)
											&& !queueNumberDTO.getTransTypeCode().equalsIgnoreCase(TENDER)
											&& !queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMMENDMENT_SERVICE)
											&& !queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMMENDMENT_ROUTE)
											&& !queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMMENDMENT_END)) {// TODO
																														// check
																														// tender
										boolean queueNoValidForTransType = queueManagementService
												.checkSelectedTransactionTypeValidForQueueNumber(queueNumberDTO,
														queueNumberDTO.getTransTypeCode());
										if (!queueNoValidForTransType) {
											if (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(CANCEL)) {
												// cancel duplicate will be
												// checked here
												sessionBackingBean.showMessage("Warning", "Token has already generated",
														"WARNING_DIALOG");
											} else {
												// except cancel
												// queueNoValidForTransType
												// checks whether transaction
												// type is
												// valid or not
												sessionBackingBean.showMessage("Warning",
														"Application is not eligible for selected transaction type",
														"WARNING_DIALOG");
											}

											return null;
										}
									}
									/** Check the status of queue number end **/

									/**
									 * Validate same details generated queue
									 * token for same queue type again start
									 **/
									boolean generateAgain = queueManagementService.validateDuplicateQueueNoGenerate(queueNumberDTO);
									boolean isTokenAvailable = queueManagementService.checkTokenNumberAvailableForQueueNoGenerate(queueNumberDTO.getTokenNo(), "O");
									if (!generateAgain || isTokenAvailable == false) {
										sessionBackingBean.showMessage("Warning", "Token has already in progress or generated.", "WARNING_DIALOG");
										return null;
									}
									/**
									 * Validate same details generated queue
									 * token for same queue type again start
									 **/

									/**
									 * if trans type permit holder change /
									 * permit holder & bus change get former app
									 * num and insert to queue_mastart start
									 **/

									/**
									 * if trans type permit holder change /
									 * permit holder & bus change get former app
									 * num and insert to queue_mastart end
									 **/

									String queueNo = queueManagementService.insertQueueDataIntoQueueMasterTable(
											queueNumberDTO, sessionBackingBean.getLoginUser());

									/**
									 * update queue no in application table by
									 * tharushi.e
									 **/
									queueManagementService.updateQueueNoInApplication(queueNo,
											queueNumberDTO.getApplicationNo());
									/**
									 * update queue no in application table by
									 * tharushi.e
									 **/
									String ownerName = queueManagementService.retrieveOwnerName(queueNumberDTO);

									/** create queue call order start **/
									queueManagementService.createQueuNumberOrder(queueNumberDTO.getTransTypeCode(),
											sessionBackingBean.getLoginUser());
									/** create queue call order end **/

									// To avoid displaying NULL for owner name
									// in queue ticket. In case it's null
									if (ownerName != null && !ownerName.isEmpty()
											&& !ownerName.trim().equalsIgnoreCase("")) {
									} else {
										ownerName = " ";
									}

									// TODO this is just to test. This Report
									// will be removed once token printer
									// comes
									/** TO BE REMOVED START **/

									String sourceFileName = "..//reports//temp_token.jrxml";

									java.util.Date date = new java.util.Date();
									DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

									QueueNumberDTO queueNoDTO = new QueueNumberDTO();
									/** null check start **/
									if (queueNumberDTO.getPermitNo() != null && !queueNumberDTO.getPermitNo().isEmpty()
											&& !queueNumberDTO.getPermitNo().trim().equalsIgnoreCase("")) {
									} else {
										queueNumberDTO.setPermitNo(" ");
									}

									if (queueNumberDTO.getApplicationNo() != null
											&& !queueNumberDTO.getApplicationNo().isEmpty()
											&& !queueNumberDTO.getApplicationNo().trim().equalsIgnoreCase("")) {
										queueNoDTO.setApplicationNo(
												"Application No. : " + queueNumberDTO.getApplicationNo());
									} else {
										queueNumberDTO.setApplicationNo(" ");
									}

									String pdfName = queueNo;

									if (queueNumberDTO.getVehicleNo() != null
											&& !queueNumberDTO.getVehicleNo().isEmpty()
											&& !queueNumberDTO.getVehicleNo().trim().equalsIgnoreCase("")) {
										queueNoDTO.setVehicleNo("Vehicle No. : " + queueNumberDTO.getVehicleNo());
										pdfName = pdfName + "_" + queueNumberDTO.getVehicleNo();
									} else {
										queueNumberDTO.setVehicleNo(" ");
									}
									/** null check end **/

									dtoList.add(queueNoDTO);

									// Parameters for report
									Map<String, Object> parameters = new HashMap<String, Object>();
									parameters.put("DATE", df.format(date));
									parameters.put("TOKEN_NO", queueNo);
									parameters.put("TRANS_TYPE", queueManagementService
											.retrieveTransDescFromTransCode(queueNumberDTO.getTransTypeCode().trim()));
									parameters.put("PERMIT_NO", queueNumberDTO.getPermitNo());
									parameters.put("OWNER_NAME", ownerName);

									JasperDesign jasperDesign = JRXmlLoader
											.load(this.getClass().getResourceAsStream(sourceFileName));

									JasperReport report = JasperCompileManager.compileReport(jasperDesign);
									JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(
											dtoList, false);

									JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters,
											beanCollectionDataSource);

									byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
									InputStream stream = new ByteArrayInputStream(pdfByteArray);
									files = new DefaultStreamedContent(stream, "Application/pdf", pdfName + ".pdf");

									/*
									 * Adding for updating status of selected
									 * queue no.
									 */
									queueManagementService.updateQueueMasterTable(queueNumberDTO, queueNo);

									/** TO BE REMOVED END **/
									queueNumber = queueNo;
									clearListener();

								} else {
									sessionBackingBean.showMessage("Warning", "Please select Queue Service",
											"WARNING_DIALOG");
								}
							} else {
								sessionBackingBean.showMessage("Warning", "Please enter valid Tender Reference No. ",
										"WARNING_DIALOG");
							}
						} else {
							sessionBackingBean.showMessage("Warning", "Please select Queue No.", "WARNING_DIALOG");
						}
					} else {
						sessionBackingBean.showMessage("Warning", "Please enter Vehicle No.", "WARNING_DIALOG");
					}
				} else {
					sessionBackingBean.showMessage("Warning", "Please enter Permit No.", "WARNING_DIALOG");
				}
			} else {
				sessionBackingBean.showMessage("Warning", "Please enter Application No.", "WARNING_DIALOG");
			}
		} else {
			sessionBackingBean.showMessage("Warning", "Please select Transaction Type", "WARNING_DIALOG");
		}

		return files;
	}

	/** GENERATE TOKEN END **/

	/** VALIDATE APPLICATION NO BEFORE TOKEN GENERATE STAR **/
	public boolean validateApplicationNumber(QueueNumberDTO queueNumberDTO) {
		boolean check = false;
		// 01 - TENDER, 02 - INSPECTION_NORMAL
		if (!queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(INSPECTION_NORMAL)
				&& !queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(TENDER)
				&& !queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(INQUIRY)
				&& !queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMEND_BUS)
				&& !queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMEND_SERVICE)
				&& !queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMEND_OWNER)
				&& !queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMEND_OWNER_BUS)
				&& !queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMEND_SERVICE_BUS)
				&& !queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMMENDMENT_SERVICE)
				&& !queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMMENDMENT_ROUTE)
				&& !queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMMENDMENT_END)
				&& !queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(PERMIT)
				&& !queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(RENEWAL)
				&& !queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(OTHER_INSPECTION_COMPLAIN)
				&& !queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(OTHER_INSPECTION_INQUIRY)
				&& !queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(OTHER_INSPECTION_SITE_VISIT)) {
			// transaction type = 03,04,05...
			if (queueNumberDTO.getApplicationNo() != null && !queueNumberDTO.getApplicationNo().isEmpty()
					&& !queueNumberDTO.getApplicationNo().trim().equalsIgnoreCase("")) {
				// valid
				check = true;
			} else {
				// invalid
				check = false;
			}
		} else {
			// valid - does not need to check whether application no is exists
			// or not
			check = true;
		}

		return check;

	}

	/** VALIDATE APPLICATION NO BEFORE TOKEN GENERATE END **/

	/** VALIDATE VEHICLE NO BEFORE TOKEN GENERATE START **/
	public boolean validateVehicleNumber(QueueNumberDTO queueNumberDTO) {
		boolean valid = false;

		if (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(INSPECTION_NORMAL)
				|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(PERMIT)) {

			if (queueNumberDTO.getVehicleNo() != null && !queueNumberDTO.getVehicleNo().isEmpty()
					&& !queueNumberDTO.getVehicleNo().trim().equalsIgnoreCase("")) {
				valid = true;
			} else {
				valid = false;
			}
		} else {
			valid = true;
		}

		return valid;
	}

	/** VALIDATE VEHICLE NO BEFORE TOKEN GENERATE END **/

	/** VALIDATE TOKEN NO BEFORE TOKEN GENERATE START **/
	public boolean validateTokenNo(QueueNumberDTO queueNumberDTO) {
		boolean valid = false;

		if (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(RENEWAL)
				|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(PERMIT)
				|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(OTHER_INSPECTION_COMPLAIN)
				|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(OTHER_INSPECTION_INQUIRY)
				|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMEND_BUS)
				|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMEND_SERVICE)
				|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMEND_OWNER)
				|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMEND_OWNER_BUS)
				|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMEND_SERVICE_BUS)
				|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMMENDMENT_SERVICE)
				|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMMENDMENT_ROUTE)
				|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMMENDMENT_END)
				|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(INSPECTION_AMEND)) {

			if (queueNumberDTO.getTokenNo() != null && !queueNumberDTO.getTokenNo().isEmpty()
					&& !queueNumberDTO.getTokenNo().trim().equalsIgnoreCase("")) {
				valid = true;
			} else {
				valid = false;
			}
		} else {
			valid = true;
		}

		return valid;
	}

	/** VALIDATE TOKEN NO BEFORE TOKEN GENERATE END **/

	/** VALIDATE PERMIT NO BEFORE TOKEN GENERATE START **/
	public boolean validatePermitNo(QueueNumberDTO queueNumberDTO) {
		boolean valid = false;

		if (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(RENEWAL)
				|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(OTHER_INSPECTION_COMPLAIN)
				|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(OTHER_INSPECTION_INQUIRY)
				|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(OTHER_INSPECTION_SITE_VISIT)
				|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMEND_BUS)
				|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMEND_SERVICE)
				|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMEND_OWNER)
				|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMEND_OWNER_BUS)
				|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMEND_SERVICE_BUS)
				|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMMENDMENT_SERVICE)
				|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMMENDMENT_ROUTE)
				|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMMENDMENT_END)
				|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(INSPECTION_AMEND)) {

			if (queueNumberDTO.getPermitNo() != null && !queueNumberDTO.getPermitNo().isEmpty()
					&& !queueNumberDTO.getPermitNo().trim().equalsIgnoreCase("")) {
				valid = true;
			} else {
				valid = false;
			}
		} else {
			valid = true;
		}

		return valid;
	}

	/** VALIDATE PERMIT NO BEFORE TOKEN GENERATE END **/

	/** VALIDATE TENDER REFERENCE NUMBER START **/
	public boolean validateTenderReferenceNumber(QueueNumberDTO queueNumberDTO) {
		boolean valid = false;

		if (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(TENDER)) {
			if (queueNumberDTO.getTenderRefNo() != null && !queueNumberDTO.getTenderRefNo().isEmpty()
					&& !queueNumberDTO.getTenderRefNo().trim().equalsIgnoreCase("")) {
				valid = true;
			} else {
				valid = false;
			}
		} else {
			valid = true;
		}

		return valid;
	}

	/** VALIDATE TENDER REFERENCE NUMBER END **/

	/** VALIDATE BEFORE TOKEN GENERATE START **/
	public String validateBeforeGenerate(QueueNumberDTO queueNumberDTO, boolean renewal) {
		String returnVal = null;
		QueueNumberDTO dto = new QueueNumberDTO();

		if (renewal) {
			dto = queueManagementService.validateUserInputValuesForRNAndNP(queueNumberDTO);// checks
																							// data
																							// is
																							// available
																							// for
																							// application
																							// number
		} else {
			if (queueNumberDTO.getTransTypeCode() != null && !queueNumberDTO.getTransTypeCode().isEmpty()
					&& !queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase("")
					&& (queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_BUS)
							|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_SERVICE)
							|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_OWNER)
							|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_OWNER_BUS)
							|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMEND_SERVICE_BUS)
							|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(TENDER)
							|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMMENDMENT_SERVICE)
							|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMMENDMENT_ROUTE)
							|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(AMMENDMENT_END))) {// TODO
																										// check
																										// tender
																										// thing

			} else {
				dto = queueManagementService.validateUserInputValues(queueNumberDTO);// checks
																						// data
																						// is
																						// available
																						// for
																						// application
																						// number
			}

		}

		if (dto != null) {
			/** Check input data is correct start **/
			if (dto.getApplicationPmStatus() != null && !dto.getApplicationPmStatus().isEmpty()
					&& !dto.getApplicationPmStatus().trim().equalsIgnoreCase("")) {
				if (dto.getApplicationPmStatus().equalsIgnoreCase("I")
						|| dto.getApplicationPmStatus().equalsIgnoreCase("R")) {
					returnVal = "Invalid input. Entered value/s in Inactive stage";
					return returnVal;
				}
			}

			if (queueNumberDTO.getPermitNo() != null && !queueNumberDTO.getPermitNo().isEmpty()
					&& !queueNumberDTO.getPermitNo().trim().equalsIgnoreCase("") && dto.getPermitNo() != null
					&& !dto.getPermitNo().isEmpty() && !dto.getPermitNo().trim().equalsIgnoreCase("")) {
				if (!queueNumberDTO.getPermitNo().trim().equalsIgnoreCase(dto.getPermitNo().trim())) {
					returnVal = "Invalid Permit No.";
					return returnVal;
				}
			}
			if (queueNumberDTO.getVehicleNo() != null && !queueNumberDTO.getVehicleNo().isEmpty()
					&& !queueNumberDTO.getVehicleNo().trim().equalsIgnoreCase("") && dto.getVehicleNo() != null
					&& !dto.getVehicleNo().isEmpty() && !dto.getVehicleNo().trim().equalsIgnoreCase("")) {
				if (!queueNumberDTO.getVehicleNo().trim().equalsIgnoreCase(dto.getVehicleNo().trim())) {
					returnVal = "Invalid Vehicle No.";
					return returnVal;
				}
			}
			/** Check input data is correct end **/
		} else {
			returnVal = "Invalid input. Please verify input and try again";
		}

		return returnVal;
	}

	/** VALIDATE BEFORE TOKEN GENERATE END **/

	/** on transaction button click START **/
	public void onTransTypeButtonClick(String transactionType) {

		queueTypeStyleRemove();
		String transType = null;
		int lettercount = transactionType.length();
		if (lettercount == 1) {
			transType = "0" + transactionType;
		} else {
			transType = transactionType;
		}

		queueNumberDTO.setTransTypeCode(transType);
		onTransactionTypeChange();

		if (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMMENDMENT)) {
			ammendmentServiceVisible = false;
			queueServeiceVisible = true;
			genTokenVisible = false;
			ammendmentVisible = true;
			inspectionVisible = false;
		} else if (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(INSPECTION)) {
			queueServeiceVisible = true;
			genTokenVisible = false;
			ammendmentVisible = false;
			inspectionVisible = true;
		} else if (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase("05")) {

			queueServeiceVisible = true;
			genTokenVisible = false;
			ammendmentVisible = false;
			inspectionVisible = false;
			ammendmentServiceVisible = true;

		} else if (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMMENDMENT_SERVICE)
				|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMMENDMENT_ROUTE)
				|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(AMMENDMENT_END)) {

			ammendmentServiceVisible = false;
			queueServeiceVisible = true;
			genTokenVisible = true;

		} else if (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(OTHER_INSPECTION)) {
			buttonStyleRemove();
			otherInspection = "true";
			otherInspectionVisible = true;
			queueServeiceVisible = true;// does not display when 'true'
			genTokenVisible = false;
			ammendmentVisible = false;
			inspectionVisible = false;
			ammendmentServiceVisible = false;
			displayPriorityBtn = false;

		} else if (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(OTHER_INSPECTION_COMPLAIN)
				|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(OTHER_INSPECTION_INQUIRY)
				|| queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(OTHER_INSPECTION_SITE_VISIT)) {

			title = "Other Inspection";
			otherInspectionVisible = false;
			genTokenVisible = true;
			permitEnable = true;
			queueEnable = true;
			String inspectionType = null;
			if (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(OTHER_INSPECTION_COMPLAIN)) {
				title = "Complain";
				inspectionType = "CI";
				queueList = queueManagementService.getQueuesForTransactionType("OP");
			} else if (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(OTHER_INSPECTION_INQUIRY)) {
				title = "Inquiry";
				inspectionType = "II";
				queueList = queueManagementService.getQueuesForTransactionType("OP");
			} else if (queueNumberDTO.getTransTypeCode().trim().equalsIgnoreCase(OTHER_INSPECTION_SITE_VISIT)) {
				title = "Site Visit";
				inspectionType = "SI";
				queueEnable = false;
			}
			queueNumberDTO.setInspectionType(inspectionType);

		} else {
			queueServeiceVisible = true;
			genTokenVisible = true;
		}
		genQueueClear = false;

	}

	/** on transaction button click END **/

	/** on queue service type button click START **/
	public void onQueueServiceTypeButtonClick(String queueServiceType) {
		genQueueClear = true;

		if (queueServiceType != null && !queueServiceType.isEmpty() && !queueServiceType.trim().equalsIgnoreCase("")) {

			if (queueServiceType.trim().equalsIgnoreCase("1")) {
				queueNumberDTO.setQueueService("normal");
				queueTypeStyleRemove();
				normal = "true";
			} else if (queueServiceType.trim().equalsIgnoreCase("2")) {
				queueNumberDTO.setQueueService("priority");
				queueTypeStyleRemove();
				priority = "true";
			}

			try {

				generateTokenListener();
				if (queueNumber != null && !queueNumber.isEmpty() && !queueNumber.trim().equalsIgnoreCase("")) {
					RequestContext.getCurrentInstance().execute("PF('queueNumberDialog').show()");
				}

			} catch (JRException e) {
				e.printStackTrace();
			}
		}

	}

	/** on queue service type button click END **/

	/** generate token if it is a inquiry start **/
	public String tokenGenerateForInquiry(QueueNumberDTO queueNumberDTO) {

		List<QueueNumberDTO> dtoList = new ArrayList<QueueNumberDTO>();
		String ownerName = null;
		String queueNo = queueManagementService.insertQueueDataIntoQueueMasterTable(queueNumberDTO,
				sessionBackingBean.getLoginUser());
		// set vehicle number to null for other inspections
		if (queueNumberDTO.getTransTypeCode().equalsIgnoreCase(OTHER_INSPECTION_COMPLAIN)
				|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(OTHER_INSPECTION_INQUIRY)
				|| queueNumberDTO.getTransTypeCode().equalsIgnoreCase(OTHER_INSPECTION_SITE_VISIT)) {
			queueNumberDTO.setVehicleNo(null);
		}

		if (!queueNumberDTO.getTransTypeCode().equalsIgnoreCase(INQUIRY)) {
			ownerName = queueManagementService.retrieveOwnerName(queueNumberDTO);
		}

		/** create queue call order start **/
		queueManagementService.createQueuNumberOrder(queueNumberDTO.getTransTypeCode(),
				sessionBackingBean.getLoginUser());
		/** create queue call order end **/

		// To avoid displaying NULL for owner name in queue ticket. In case it's
		// null
		if (ownerName != null && !ownerName.isEmpty() && !ownerName.trim().equalsIgnoreCase("")) {
			ownerName = "Owner Name: " + ownerName;
		} else {
			ownerName = " ";
		}

		// TODO this is just to test. This Report will be removed once token
		// printer
		// comes
		/** TO BE REMOVED START **/

		String sourceFileName = "..//reports//temp_token.jrxml";

		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");

		QueueNumberDTO queueNoDTO = new QueueNumberDTO();
		/** null check start **/
		if (queueNumberDTO.getPermitNo() != null && !queueNumberDTO.getPermitNo().isEmpty()
				&& !queueNumberDTO.getPermitNo().trim().equalsIgnoreCase("")) {
			queueNoDTO.setPermitNo("Permit No. : " + queueNumberDTO.getPermitNo());
		} else {
			queueNumberDTO.setPermitNo(" ");
		}

		if (queueNumberDTO.getApplicationNo() != null && !queueNumberDTO.getApplicationNo().isEmpty()
				&& !queueNumberDTO.getApplicationNo().trim().equalsIgnoreCase("")) {
			queueNoDTO.setApplicationNo("Application No. : " + queueNumberDTO.getApplicationNo());
		} else {
			queueNumberDTO.setApplicationNo(" ");
		}

		String pdfName = queueNo;
		if (queueNumberDTO.getVehicleNo() != null && !queueNumberDTO.getVehicleNo().isEmpty()
				&& !queueNumberDTO.getVehicleNo().trim().equalsIgnoreCase("")) {
			queueNoDTO.setVehicleNo("Vehicle No. : " + queueNumberDTO.getVehicleNo());
			pdfName = pdfName + "_" + queueNumberDTO.getVehicleNo();
		} else {
			queueNumberDTO.setVehicleNo(" ");
		}
		/** null check end **/

		dtoList.add(queueNoDTO);

		// Parameters for report
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("DATE", df.format(date));
		parameters.put("TOKEN_NO", queueNo);
		parameters.put("TRANS_TYPE",
				queueManagementService.retrieveTransDescFromTransCode(queueNumberDTO.getTransTypeCode().trim()));

		parameters.put("OWNER_NAME", ownerName);

		JasperDesign jasperDesign;
		try {
			jasperDesign = JRXmlLoader.load(this.getClass().getResourceAsStream(sourceFileName));

			JasperReport report = JasperCompileManager.compileReport(jasperDesign);
			JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(dtoList, false);

			JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, beanCollectionDataSource);

			byte[] pdfByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
			InputStream stream = new ByteArrayInputStream(pdfByteArray);
			files = new DefaultStreamedContent(stream, "Application/pdf", pdfName + ".pdf");

			/* Adding for updating status of selected queue no. */
			queueManagementService.updateQueueMasterTable(queueNumberDTO, queueNo);

		} catch (JRException e) {
			e.printStackTrace();
		}

		/** TO BE REMOVED END **/
		// downloadStart = true;
		return queueNo;
	}

	/** generate token if it is a inquiry end **/

	public void clearListener() {
		queueNumberDTO = new QueueNumberDTO();
		permitEnable = false;
		vehicleEnable = false;
		queueEnable = false;
		appNoEnabe = false;
		appNoStarEnabe = false;
		downloadStart = false;
		rendrRefNoEnabe = false;
		queueServeiceVisible = false;
		genQueueClear = false;
		queueNumber = null;
		title = null;
		ammendmentVisible = false;
		ammendBus = false;
		inspectionVisible = false;
		ammendmentServiceVisible = false;
		otherInspectionVisible = false;
		genTokenVisible = false;
		displayPriorityBtn = true;
		buttonStyleRemove();
		queueTypeStyleRemove();
		RequestContext.getCurrentInstance().update(":frmQueueMananagement");

	}

	public static String getPropertyFileVale(String key) {
		String value = null;
		try {
			value = PropertyReader.getPropertyValue(key);
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		return value;
	}

	public void buttonStyleRemove() {
		tenderStyle = null;
		inspection = null;
		newPermit = null;
		renewal = null;
		ammendment = null;
		payment = null;
		cancel = null;
		inquiry = null;
		otherInspection = null;
	}

	public void queueTypeStyleRemove() {
		normal = null;
		priority = null;
	}

	private boolean validateVehicleNoAndAppNoForInquiry(String vehicleNo, String applicationNo) {

		if ((vehicleNo == null || vehicleNo.isEmpty() || vehicleNo.trim().equalsIgnoreCase(""))
				&& (applicationNo == null || applicationNo.isEmpty() || applicationNo.trim().equalsIgnoreCase(""))) {
			return false;
		}

		return true;
	}

	public void ammendmentBudProcceed() {
		ammendment = "true";
		permitEnable = true;
		vehicleEnable = true;
		rendrRefNoEnabe = false;// only enable when LOV value=TENDER
		appNoEnabe = false;
		appNoStarEnabe = false;
		title = "Ammendment";
		ammendmentVisible = false;
		queueServeiceVisible = true;
		genTokenVisible = true;
		genQueueClear = false;
		RequestContext.getCurrentInstance().update("@all");

		onTransTypeButtonClick("13");
	}

	public QueueNumberDTO getQueueNumberDTO() {
		return queueNumberDTO;
	}

	public void setQueueNumberDTO(QueueNumberDTO queueNumberDTO) {
		this.queueNumberDTO = queueNumberDTO;
	}

	public QueueManagementService getQueueManagementService() {
		return queueManagementService;
	}

	public void setQueueManagementService(QueueManagementService queueManagementService) {
		this.queueManagementService = queueManagementService;
	}

	public List<TransactionTypeDTO> getTransTypeList() {
		return transTypeList;
	}

	public void setTransTypeList(List<TransactionTypeDTO> transTypeList) {
		this.transTypeList = transTypeList;
	}

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public StreamedContent getFiles() {
		return files;
	}

	public void setFiles(StreamedContent files) {
		this.files = files;
	}

	public boolean isPermitEnable() {
		return permitEnable;
	}

	public void setPermitEnable(boolean permitEnable) {
		this.permitEnable = permitEnable;
	}

	public boolean isVehicleEnable() {
		return vehicleEnable;
	}

	public void setVehicleEnable(boolean vehicleEnable) {
		this.vehicleEnable = vehicleEnable;
	}

	public boolean isAppNoEnabe() {
		return appNoEnabe;
	}

	public void setAppNoEnabe(boolean appNoEnabe) {
		this.appNoEnabe = appNoEnabe;
	}

	public boolean isDownloadStart() {
		return downloadStart;
	}

	public void setDownloadStart(boolean downloadStart) {
		this.downloadStart = downloadStart;
	}

	public boolean isRendrRefNoEnabe() {
		return rendrRefNoEnabe;
	}

	public void setRendrRefNoEnabe(boolean rendrRefNoEnabe) {
		this.rendrRefNoEnabe = rendrRefNoEnabe;
	}

	public String getTenderStyle() {
		return tenderStyle;
	}

	public void setTenderStyle(String tenderStyle) {
		this.tenderStyle = tenderStyle;
	}

	public String getInspection() {
		return inspection;
	}

	public void setInspection(String inspection) {
		this.inspection = inspection;
	}

	public String getNewPermit() {
		return newPermit;
	}

	public void setNewPermit(String newPermit) {
		this.newPermit = newPermit;
	}

	public String getRenewal() {
		return renewal;
	}

	public void setRenewal(String renewal) {
		this.renewal = renewal;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getCancel() {
		return cancel;
	}

	public void setCancel(String cancel) {
		this.cancel = cancel;
	}

	public String getInquiry() {
		return inquiry;
	}

	public void setInquiry(String inquiry) {
		this.inquiry = inquiry;
	}

	public String getAmmendment() {
		return ammendment;
	}

	public void setAmmendment(String ammendment) {
		this.ammendment = ammendment;
	}

	public String getNormal() {
		return normal;
	}

	public void setNormal(String normal) {
		this.normal = normal;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public boolean isQueueServeiceVisible() {
		return queueServeiceVisible;
	}

	public void setQueueServeiceVisible(boolean queueServeiceVisible) {
		this.queueServeiceVisible = queueServeiceVisible;
	}

	public boolean isGenQueueClear() {
		return genQueueClear;
	}

	public void setGenQueueClear(boolean genQueueClear) {
		this.genQueueClear = genQueueClear;
	}

	public String getQueueNumber() {
		return queueNumber;
	}

	public void setQueueNumber(String queueNumber) {
		this.queueNumber = queueNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isAppNoStarEnabe() {
		return appNoStarEnabe;
	}

	public void setAppNoStarEnabe(boolean appNoStarEnabe) {
		this.appNoStarEnabe = appNoStarEnabe;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public boolean isAmmendmentVisible() {
		return ammendmentVisible;
	}

	public void setAmmendmentVisible(boolean ammendmentVisible) {
		this.ammendmentVisible = ammendmentVisible;
	}

	public boolean isGenTokenVisible() {
		return genTokenVisible;
	}

	public void setGenTokenVisible(boolean genTokenVisible) {
		this.genTokenVisible = genTokenVisible;
	}

	public boolean isAmmendBus() {
		return ammendBus;
	}

	public void setAmmendBus(boolean ammendBus) {
		this.ammendBus = ammendBus;
	}

	public boolean isInspectionVisible() {
		return inspectionVisible;
	}

	public void setInspectionVisible(boolean inspectionVisible) {
		this.inspectionVisible = inspectionVisible;
	}

	public boolean isAmmendmentServiceVisible() {
		return ammendmentServiceVisible;
	}

	public void setAmmendmentServiceVisible(boolean ammendmentServiceVisible) {
		this.ammendmentServiceVisible = ammendmentServiceVisible;
	}

	public List<QueueNumberDTO> getQueueList() {
		return queueList;
	}

	public void setQueueList(List<QueueNumberDTO> queueList) {
		this.queueList = queueList;
	}

	public boolean isQueueEnable() {
		return queueEnable;
	}

	public void setQueueEnable(boolean queueEnable) {
		this.queueEnable = queueEnable;
	}

	public String getOtherInspection() {
		return otherInspection;
	}

	public void setOtherInspection(String otherInspection) {
		this.otherInspection = otherInspection;
	}

	public boolean isOtherInspectionVisible() {
		return otherInspectionVisible;
	}

	public void setOtherInspectionVisible(boolean otherInspectionVisible) {
		this.otherInspectionVisible = otherInspectionVisible;
	}

	public boolean isDisplayPriorityBtn() {
		return displayPriorityBtn;
	}

	public void setDisplayPriorityBtn(boolean displayPriorityBtn) {
		this.displayPriorityBtn = displayPriorityBtn;
	}

}
