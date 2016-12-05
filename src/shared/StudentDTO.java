package shared;

/**
 * Skabelon på en studerende
 */
public class StudentDTO extends UserDTO {

	private static final long serialVersionUID = 1L;
	
	private String study;

	// Variabler skal defineres efterfølgende via public setters
	public StudentDTO() {
		super();
	}

	// Variabler defineres under initiering
	public StudentDTO(int id, String mail, String password, String type, String study) {
		super(id, mail, password, type);
		this.study = study;
	}

	public String getStudy() {
		return study;
	}

	public void setStudy(String study) {
		this.study = study;
	}

	/**
	 * En tekstbeskrivelse af en studerende
	 * @return String repræsentationen på en studerende
	 */
	@Override
	public String toString() {
		return "StudentDTO [getId()=" + getId() + ", getCbsMail()=" + getCbsMail() + ", getPassword()=" + getPassword()
				+ ", getType()=" + getType() + "]" + ", getStudy()=" + getStudy() + "]";
	}
}