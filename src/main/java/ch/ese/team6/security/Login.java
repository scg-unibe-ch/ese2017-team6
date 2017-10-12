package ch.ese.team6.security;

public class Login {
	
	private String username;
	private String password;
	
	public Login(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public Login() {}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
}
