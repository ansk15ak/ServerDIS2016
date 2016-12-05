package shared;

/**
 * Skabelon på en administrator
 */
public class AdminDTO extends UserDTO {

	private static final long serialVersionUID = 1L;

	// Variabler skal defineres efterfølgende via public setters
	public AdminDTO() {
		super();
	}

	// Variabler defineres under initiering
	public AdminDTO(int id, String cbsMail, String password, String type) {
		super(id, cbsMail, password, type);
	}

	/**
	 * En tekstbeskrivelse af en administrator
	 * @return String repræsentationen på en administrator
	 */
	@Override
	public String toString() {
		return "AdminDTO [getId()=" + getId() +
				", getCbsMail()=" + getCbsMail() +
				", getPassword()=" + getPassword() +
				", getType()=" + getType() + "]";
	}
}
