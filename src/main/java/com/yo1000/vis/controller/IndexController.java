package com.yo1000.vis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by yoichi.kikuchi on 15/06/02.
 */
@Controller
@RequestMapping("")
public class IndexController {
    @RequestMapping("")
    public String getIndex(Model model) {
        return "redirect:/dashboard";
    }
}
