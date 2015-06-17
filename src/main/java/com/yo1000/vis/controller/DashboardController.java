package com.yo1000.vis.controller;

import com.yo1000.vis.model.data.Widget;
import com.yo1000.vis.model.service.WidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by yoichi.kikuchi on 15/06/02.
 */
@Controller
@RequestMapping("dashboard")
public class DashboardController {
    @Autowired
    private WidgetService widgetService;

    @RequestMapping("")
    public String getIndex(Model model) {
        model.addAttribute("title", "Dashboard");
        model.addAttribute("widgetQueryMap", this.getWidgetService().getChartWidgets());

        return "dashboard/index";
    }

    @RequestMapping("preference")
    public String getPreference(Model model) {
        model.addAttribute("title", "Dashboard preference");
        model.addAttribute("widgetQueryMap", this.getWidgetService().getChartWidgets());
        model.addAttribute("messageEntry", this.getWidgetService()
                .getMessageWidgets().entrySet().iterator().next());

        return "dashboard/preference";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String postIndex(Widget widget) {
        this.getWidgetService().set(widget);

        return "redirect:/dashboard/preference";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String deleteIndex(Widget widget) {
        this.getWidgetService().remove(widget.getId());

        return "redirect:/dashboard/preference";
    }

    protected WidgetService getWidgetService() {
        return widgetService;
    }
}
