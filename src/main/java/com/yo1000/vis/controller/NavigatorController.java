package com.yo1000.vis.controller;

import com.yo1000.vis.model.data.Navigator;
import com.yo1000.vis.model.service.NavigatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by yoichi.kikuchi on 15/06/02.
 */
@Controller
@RequestMapping("navigator")
public class NavigatorController {
    @Autowired
    private NavigatorService navigatorService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getIndex(Model model) {
        model.addAttribute("title", "Navigator");
        model.addAttribute("navGroups", this.getNavigatorService().getNavigatorGroups());

        return "navigator/index";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String postIndex(Navigator navigator) {
        this.getNavigatorService().setNavigator(navigator);

        return "redirect:navigator";
    }

    protected NavigatorService getNavigatorService() {
        return navigatorService;
    }
}
