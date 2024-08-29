package com.wgplaner.password_recovery;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIControllers {
  @GetMapping("/forgot-password")
  public String forgotPassword() {
    return "forgot-password";
  }

}
