package lk.informatics.ntc.view.beans.references.templates;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.CodeDescriptionDTO;
import lk.informatics.ntc.model.service.ReferenceDataService;
import lk.informatics.ntc.view.beans.SessionBackingBean;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "codeDescriptionTemplate")
@ViewScoped
public abstract class CodeDescriptionTemplateBean implements Serializable {

	private static final long serialVersionUID = -2983817229956621158L;

	private CodeDescriptionDTO codeDescriptionDTO;
	public boolean codeMandatory;
	public boolean descriptionMandatory;
	private ReferenceDataService referenceDataService;
	private CodeDescriptionDTO deletecodeDescriptionDTO;

	public String functionHeader = null;
	public String TABLE_NAME = null;
	public String codeLabel = null;
	public String description_english_Label = null;
	public String description_sinhala_Label = null;
	public String description_tamil_Label = null;

	public String saveButtonLabel = null;
	public String clearButtonLabel = null;
	public String searchButtonLabel = null;

	private String prevDesEng;
	private String prevDesSin;
	private String prevDesTam;

	private Date todayMaxDate;

	public List<CodeDescriptionDTO> searchedData = new ArrayList<CodeDescriptionDTO>();

	@ManagedProperty("#{msg}")
	public ResourceBundle bundle;

	@ManagedProperty(value = "#{sessionBackingBean}")
	private SessionBackingBean sessionBackingBean;

	public SessionBackingBean getSessionBackingBean() {
		return sessionBackingBean;
	}

	public void setSessionBackingBean(SessionBackingBean sessionBackingBean) {
		this.sessionBackingBean = sessionBackingBean;
	}

	public ResourceBundle getBundle() {
		return bundle;
	}

	public void setBundle(ResourceBundle bundle) {
		this.bundle = bundle;
	}

	public void onPageLoad() {

		referenceDataService = (ReferenceDataService) SpringApplicationContex.getBean("referenceDataService");

		isCodeMandatory();
		isDescriptionMandatory();
		setTableName();
		setFunctionName();
		setCodeLable();
		setDescriptionLable();

		todayMaxDate = new Date();

		this.saveButtonLabel = bundle.getString("reference.page.save.button.label");
		this.clearButtonLabel = bundle.getString("reference.page.clear.button.label");
		this.searchButtonLabel = bundle.getString("reference.page.clear.button.search");

	}

	public CodeDescriptionTemplateBean() {
		codeDescriptionDTO = new CodeDescriptionDTO();

	}

	@SuppressWarnings("deprecation")
	public void checkDuplicate() {
		RequestContext context = RequestContext.getCurrentInstance();
		try {
			boolean duplicate = referenceDataService.isCodeDuplicate(codeDescriptionDTO.getCode(), TABLE_NAME);
			if (duplicate) {
				context.execute("PF('duplicateCode').show();");
				return;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public abstract void isCodeMandatory();

	public abstract void isDescriptionMandatory();

	public abstract void setTableName();

	public abstract void setFunctionName();

	public abstract void setCodeLable();

	public abstract void setDescriptionLable();

	public CodeDescriptionDTO getCodeDescriptionDTO() {
		return codeDescriptionDTO;
	}

	public void setCodeDescriptionDTO(CodeDescriptionDTO codeDescriptionDTO) {
		this.codeDescriptionDTO = codeDescriptionDTO;
	}

	@SuppressWarnings("deprecation")
	public void saveRecord() {

		RequestContext context = RequestContext.getCurrentInstance();

		boolean valid = checkEmptyFields();
		if (!valid) {
			RequestContext context1 = RequestContext.getCurrentInstance();
			context1.execute("PF('allFieldsMandatory').show();");

		} else {
			String status = codeDescriptionDTO.getStatus();
			if (status.equalsIgnoreCase("A")) {
				codeDescriptionDTO.setStatusDescription("ACTIVE");
			} else if (status.equalsIgnoreCase("I")) {
				codeDescriptionDTO.setStatusDescription("INACTIVE");
			} else {
				codeDescriptionDTO.setStatusDescription(codeDescriptionDTO.getStatus());
			}
			int response = referenceDataService.saveRecord(codeDescriptionDTO, TABLE_NAME,
					sessionBackingBean.getLoginUser());
			if (response < 0) {
				context.execute("PF('saveError').show();");
				return;
			} else {
				if (response == 1) {
					context.execute("PF('duplicateCode').show();");
					return;
				} else {
					if (response == 2) {
						context.execute("PF('duplicateDesc').show();");
						return;
					} else {
						context.execute("PF('saveSuccess').show();");
						if (codeDescriptionDTO.isSearchedRecord() && !codeDescriptionDTO.isFreshRecord()) { // edit mode
																											// for query
																											// record
							for (CodeDescriptionDTO obj : searchedData) {
								if (obj.getCode().equalsIgnoreCase(codeDescriptionDTO.getCode())) {
									obj.setDescription_english(codeDescriptionDTO.getDescription_english());
									obj.setDescription_sinhala(codeDescriptionDTO.getDescription_sinhala());
									obj.setDescription_tamil(codeDescriptionDTO.getDescription_tamil());
									obj.setStatus(codeDescriptionDTO.getStatus());
									obj.setStatusDescription(codeDescriptionDTO.getStatusDescription());
								}
							}
							codeDescriptionDTO = new CodeDescriptionDTO();
							// findAllRecords();
						} else {
							if (codeDescriptionDTO.isFreshRecord()) { // edit mode for new record

								for (CodeDescriptionDTO obj : searchedData) {
									if (obj.getCode().equalsIgnoreCase(codeDescriptionDTO.getCode())) {
										obj.setDescription_english(codeDescriptionDTO.getDescription_english());
									}
								}
								codeDescriptionDTO = new CodeDescriptionDTO();
							} else {
								codeDescriptionDTO.setSearchedRecord(true); // new insert
								codeDescriptionDTO.setFreshRecord(true);
								searchedData.add(codeDescriptionDTO);
								codeDescriptionDTO = new CodeDescriptionDTO();
							}

						}

					}
				}
			}
		}
	}

	public void searchAction() {
		searchedData = referenceDataService.searchRecords(codeDescriptionDTO, TABLE_NAME);
	}

	@SuppressWarnings("deprecation")
	public void deleteAction() {
		codeDescriptionDTO = new CodeDescriptionDTO();
		RequestContext context = RequestContext.getCurrentInstance();
		if (deletecodeDescriptionDTO != null) {

			int result = referenceDataService.deleteRecord(deletecodeDescriptionDTO, TABLE_NAME);
			if (result < 0) {
				context.execute("PF('deleteError').show();");
				deletecodeDescriptionDTO = null;

				return;
			} else {
				context.execute("PF('deletesuccess').show();");

				searchedData.remove(deletecodeDescriptionDTO);
				deletecodeDescriptionDTO = null;

				return;
			}
		} else {
			context.execute("PF('deleteError').show();");
			deletecodeDescriptionDTO = null;
			return;
		}
	}

	public void editAction(CodeDescriptionDTO editedcodeDescriptionDTO) {

		codeDescriptionDTO.setCode(editedcodeDescriptionDTO.getCode());
		codeDescriptionDTO.setDescription_english(editedcodeDescriptionDTO.getDescription_english());
		codeDescriptionDTO.setDescription_sinhala(editedcodeDescriptionDTO.getDescription_sinhala());
		codeDescriptionDTO.setDescription_tamil(editedcodeDescriptionDTO.getDescription_tamil());
		codeDescriptionDTO.setStatus(editedcodeDescriptionDTO.getStatus());
		codeDescriptionDTO.setSearchedRecord(true);
	}

	public boolean checkEmptyFields() {
		boolean nullfound = false;
		if (codeMandatory) {
			if (codeDescriptionDTO.getCode() == null || codeDescriptionDTO.getCode().trim().equalsIgnoreCase("")) {
				nullfound = true;
			}
		}
		if (descriptionMandatory) {
			if (codeDescriptionDTO.getDescription_english() == null
					|| codeDescriptionDTO.getDescription_english().trim().equalsIgnoreCase("")) {
				nullfound = true;
			}
			if (codeDescriptionDTO.getDescription_sinhala() == null
					|| codeDescriptionDTO.getDescription_sinhala().trim().equalsIgnoreCase("")) {
				nullfound = true;
			}
			if (codeDescriptionDTO.getDescription_tamil() == null
					|| codeDescriptionDTO.getDescription_tamil().trim().equalsIgnoreCase("")) {
				nullfound = true;
			}
		}
		if (codeDescriptionDTO.getStatus() == null || codeDescriptionDTO.getStatus().trim().equalsIgnoreCase("")) {
			nullfound = true;
		}

		if (nullfound) {
			return false;
		} else if ((!codeDescriptionDTO.getDescription_english().equals(null)
				|| !codeDescriptionDTO.getDescription_english().trim().equalsIgnoreCase(""))

				&& (!codeDescriptionDTO.getDescription_sinhala().equals(null)
						|| !codeDescriptionDTO.getDescription_sinhala().trim().equalsIgnoreCase(""))

				&& (!codeDescriptionDTO.getDescription_tamil().equals(null)
						|| !codeDescriptionDTO.getDescription_tamil().trim().equalsIgnoreCase(""))) {

			codeDescriptionDTO.setCode(codeDescriptionDTO.getCode().toUpperCase());
			codeDescriptionDTO.setDescription_english(codeDescriptionDTO.getDescription_english().toUpperCase());
			codeDescriptionDTO.setDescription_sinhala(codeDescriptionDTO.getDescription_sinhala().toUpperCase());
			codeDescriptionDTO.setDescription_tamil(codeDescriptionDTO.getDescription_tamil().toUpperCase());

			return true;
		}

		return true;

	}

	public void clearForm() {
		codeDescriptionDTO = new CodeDescriptionDTO();
		searchedData = new ArrayList<CodeDescriptionDTO>();
		deletecodeDescriptionDTO = null;
		prevDesEng = null;
		prevDesSin = null;
		prevDesTam = null;
	}

	public ReferenceDataService getReferenceDataService() {
		return referenceDataService;
	}

	public void setReferenceDataService(ReferenceDataService referenceDataService) {
		this.referenceDataService = referenceDataService;
	}

	public String getFunctionHeader() {
		return functionHeader;
	}

	public void setFunctionHeader(String functionHeader) {
		this.functionHeader = bundle.getString(functionHeader);
	}

	public String getTABLE_NAME() {
		return TABLE_NAME;
	}

	public void setTABLE_NAME(String tABLE_NAME) {
		TABLE_NAME = tABLE_NAME;
	}

	public String getCodeLabel() {
		return codeLabel;
	}

	public void setCodeLabel(String codeLabel) {
		this.codeLabel = bundle.getString(codeLabel);

	}

	public String getDescription_english_Label() {
		return description_english_Label;
	}

	public void setDescription_english_Label(String description_english_Label) {
		this.description_english_Label = bundle.getString(description_english_Label);
	}

	public String getDescription_sinhala_Label() {
		return description_sinhala_Label;
	}

	public void setDescription_sinhala_Label(String description_sinhala_Label) {
		this.description_sinhala_Label = bundle.getString(description_sinhala_Label);
	}

	public String getDescription_tamil_Label() {
		return description_tamil_Label;
	}

	public void setDescription_tamil_Label(String description_tamil_Label) {
		this.description_tamil_Label = bundle.getString(description_tamil_Label);
	}

	public void setCodeMandatory(boolean codeMandatory) {
		this.codeMandatory = codeMandatory;
	}

	public void setDescriptionMandatory(boolean descriptionMandatory) {
		this.descriptionMandatory = descriptionMandatory;
	}

	public String getSaveButtonLabel() {
		return saveButtonLabel;
	}

	public void setSaveButtonLabel(String saveButtonLabel) {
		this.saveButtonLabel = saveButtonLabel;
	}

	public String getClearButtonLabel() {
		return clearButtonLabel;
	}

	public void setClearButtonLabel(String clearButtonLabel) {
		this.clearButtonLabel = clearButtonLabel;
	}

	public String getSearchButtonLabel() {
		return searchButtonLabel;
	}

	public void setSearchButtonLabel(String searchButtonLabel) {
		this.searchButtonLabel = searchButtonLabel;
	}

	public List<CodeDescriptionDTO> getSearchedData() {
		return searchedData;
	}

	public void setSearchedData(List<CodeDescriptionDTO> searchedData) {
		this.searchedData = searchedData;
	}

	public CodeDescriptionDTO getDeletecodeDescriptionDTO() {
		return deletecodeDescriptionDTO;
	}

	public void setDeletecodeDescriptionDTO(CodeDescriptionDTO deletecodeDescriptionDTO) {
		this.deletecodeDescriptionDTO = deletecodeDescriptionDTO;
	}

	public void findAllRecords() {
		searchedData = referenceDataService.findAllRecords(TABLE_NAME);
	}

	public Date getTodayMaxDate() {
		return todayMaxDate;
	}

	public void setTodayMaxDate(Date todayMaxDate) {
		this.todayMaxDate = todayMaxDate;
	}

	public String getPrevDesEng() {
		return prevDesEng;
	}

	public void setPrevDesEng(String prevDesEng) {
		this.prevDesEng = prevDesEng;
	}

	public String getPrevDesSin() {
		return prevDesSin;
	}

	public void setPrevDesSin(String prevDesSin) {
		this.prevDesSin = prevDesSin;
	}

	public String getPrevDesTam() {
		return prevDesTam;
	}

	public void setPrevDesTam(String prevDesTam) {
		this.prevDesTam = prevDesTam;
	}

}
