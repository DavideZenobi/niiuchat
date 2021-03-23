package io.dz.niiuchat.view;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "")
public class HomeController {

  @GetMapping(path = "/home", produces = MediaType.TEXT_HTML_VALUE)
  public String getHome() {
    return "home";
  }

}
