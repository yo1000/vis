package com.yo1000.vis.controller;

import com.yo1000.vis.model.service.RequestHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by yoichi.kikuchi on 15/06/02.
 */
@Controller
@RequestMapping("cache")
public class CacheController {
    @Autowired
    private RequestHistoryService requestHistoryService;

    @RequestMapping("target")
    public String getTarget(Model model) {
        model.addAttribute("title", "Cache target");
        model.addAttribute("histories", this.getRequestHistoryService().getHistories());

        return "cache/target";
    }

    protected RequestHistoryService getRequestHistoryService() {
        return requestHistoryService;
    }
}
