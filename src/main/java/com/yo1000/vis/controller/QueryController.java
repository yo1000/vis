package com.yo1000.vis.controller;

import com.yo1000.vis.model.data.Query;
import com.yo1000.vis.model.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by yoichi.kikuchi on 15/06/02.
 */
@Controller
@RequestMapping("query")
public class QueryController {
    @Autowired
    private QueryService queryService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getList(Model model) {
        model.addAttribute("title", "Query list");
        model.addAttribute("queries", this.getQueryService().getQueries());

        return "query/list";
    }

    @RequestMapping(value = "{id:(?!^(?:register)$)^[a-zA-Z0-9]+$}", method = RequestMethod.GET)
    public String getItem(@PathVariable String id, Model model) {
        model.addAttribute("title", "Query detail");
        model.addAttribute("query", this.getQueryService().getQuery(id));

        return "query/detail";
    }

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String getRegister(Model model) {
        model.addAttribute("title", "Query register");
        model.addAttribute("queries", this.getQueryService().getQueries());

        return "query/register";
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String postRegister(Query query) {
        this.getQueryService().setQuery(query);

        return "redirect:/query";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String postIndex(Query query) {
        this.getQueryService().setQuery(query);

        return "redirect:/query";
    }

    protected QueryService getQueryService() {
        return queryService;
    }
}
