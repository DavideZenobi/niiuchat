package io.dz.niiuchat.view;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "")
public class ViewController {

  @GetMapping(path = {"/", "/index", "/index.html"}, produces = MediaType.TEXT_HTML_VALUE)
  public String getIndex() {
    return "index";
  }

  @GetMapping(path = "/home", produces = MediaType.TEXT_HTML_VALUE)
  public String getHome() {
    return "home";
  }

  @GetMapping(path = "/login", produces = MediaType.TEXT_HTML_VALUE)
  public String getLogin() {
    return "login";
  }

  @GetMapping(path = "/register", produces = MediaType.TEXT_HTML_VALUE)
  public String getRegister() { return "register"; }

}
