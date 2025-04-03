package com.scalesec.vulnado;

import org.springframework.web.bind.annotation.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.beans.factory.annotation.*;

@RestController
@EnableAutoConfiguration
public class LoginController {
  @Value("${app.secret}")
  private String secret;

  @CrossOrigin(origins = "*") // Ensure enabling CORS is safe here
  @PostMapping(value = "/login", produces = "application/json", consumes = "application/json")
  LoginResponse login(@RequestBody LoginRequest input) {
    User user = User.fetch(input.username);
    if (Postgres.md5(input.password).equals(user.hashedPassword)) {
      return new LoginResponse(user.token(secret));
    } else {
      throw new Unauthorized("Access Denied");
    }
  }
}

class LoginRequest implements Serializable {
  private String username; // Made non-public
  private String password; // Made non-public
  public String getUsername() { return username; }
}
  public void setUsername(String username) { this.username = username; }

  public String getPassword() { return password; }
class LoginResponse implements Serializable {
  public void setPassword(String password) { this.password = password; }
  private String token; // Made non-public
  public String getToken() { return token; }
  public LoginResponse(String msg) { this.token = msg; }
  public void setToken(String token) { this.token = token; }
}

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class Unauthorized extends RuntimeException {
  public Unauthorized(String exception) {
    super(exception);
  }
}
