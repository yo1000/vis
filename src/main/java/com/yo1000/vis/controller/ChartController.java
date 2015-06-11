package com.yo1000.vis.controller;

import com.yo1000.vis.model.data.Query;
import com.yo1000.vis.model.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by yoichi.kikuchi on 15/06/09.
 */
@Controller
@RequestMapping("chart")
public class ChartController {
    @Autowired
    private QueryService queryService;

    @RequestMapping("{key:^[0-9a-zA-Z_\\-]+$}")
    public String getIndex(@PathVariable String key, Model model) {
        Query query = this.getQueryService().getQueryByKey(key);

        model.addAttribute("title", query.getName());
        model.addAttribute("key", query.getKey());
        model.addAttribute("name", query.getName());

        return "chart/" + query.getView();
    }

    @RequestMapping("{key:^[0-9a-zA-Z_\\-]+$}/{start}")
    public String getIndex(@PathVariable String key, @PathVariable String start, Model model) {
        Query query = this.getQueryService().getQueryByKey(key);

        model.addAttribute("title", query.getName());
        model.addAttribute("key", query.getKey());
        model.addAttribute("name", query.getName());
        model.addAttribute("start", start);

        return "chart/" + query.getView();
    }

    @RequestMapping("{key:^[0-9a-zA-Z_\\-]+$}/{start}/{end}")
    public String getIndex(@PathVariable String key, @PathVariable String start,
                           @PathVariable String end, Model model) {
        Query query = this.getQueryService().getQueryByKey(key);

        model.addAttribute("title", query.getName());
        model.addAttribute("key", query.getKey());
        model.addAttribute("name", query.getName());
        model.addAttribute("start", start);
        model.addAttribute("end", end);

        return "chart/" + query.getView();
    }

    protected QueryService getQueryService() {
        return queryService;
    }
}
