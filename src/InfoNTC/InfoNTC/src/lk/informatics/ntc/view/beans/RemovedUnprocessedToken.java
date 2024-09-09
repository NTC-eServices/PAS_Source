package lk.informatics.ntc.view.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import lk.informatics.ntc.model.dto.TokenDTO;
import lk.informatics.ntc.model.service.AdminService;
import lk.informatics.ntc.view.util.SpringApplicationContex;

@ManagedBean(name = "unprocessTokenBackingBean")
@ViewScoped

public class RemovedUnprocessedToken {
	// Services
	private AdminService adminService;
	// DTOs
	private List<TokenDTO> tokenList = new ArrayList<TokenDTO>();
	private List<TokenDTO> renewalTokenList = new ArrayList<TokenDTO>();
	private TokenDTO strSelectedTokenSeq;

	private String successMessage;
	private String errorMessage;

	@PostConstruct
	public void init() {
		LoadValues();
	}

	// Methods
	public void LoadValues() {
		adminService = (AdminService) SpringApplicationContex.getBean("adminService");
		setTokenList(adminService.getUnporcessedTokenDet());
		setRenewalTokenList(adminService.getRenewalTokenDet());
	}

	public void deleteAction() {

		boolean isDeleted = adminService.deleteToken(strSelectedTokenSeq.getSeq());

		if (isDeleted == true) {
			setSuccessMessage("Deleted successfully");
			RequestContext.getCurrentInstance().execute("PF('successMessage').show()");

			LoadValues();

		} else {
			setErrorMessage("Delete unsuccessfully");
			RequestContext.getCurrentInstance().execute("PF('errorMessage').show()");
		}
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public List<TokenDTO> getTokenList() {
		return tokenList;
	}

	public void setTokenList(List<TokenDTO> tokenList) {
		this.tokenList = tokenList;
	}

	public TokenDTO getStrSelectedTokenSeq() {
		return strSelectedTokenSeq;
	}

	public void setStrSelectedTokenSeq(TokenDTO strSelectedTokenSeq) {
		this.strSelectedTokenSeq = strSelectedTokenSeq;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<TokenDTO> getRenewalTokenList() {
		return renewalTokenList;
	}

	public void setRenewalTokenList(List<TokenDTO> renewalTokenList) {
		this.renewalTokenList = renewalTokenList;
	}

}
