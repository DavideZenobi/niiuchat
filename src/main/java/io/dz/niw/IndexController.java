package io.dz.niw;

import io.dz.niw.domain.tables.pojos.Messages;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "")
public class IndexController {

  @GetMapping(path = {"/", "/index", "/index.html"})
  public String getIndex() {
    Messages messages = new Messages();
    return "index";
  }

}
