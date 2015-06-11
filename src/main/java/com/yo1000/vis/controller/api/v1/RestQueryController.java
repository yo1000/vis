package com.yo1000.vis.controller.api.v1;

import com.yo1000.vis.model.data.Query;
import com.yo1000.vis.model.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yoichi.kikuchi on 15/06/07.
 */
@RestController
@RequestMapping("api/v1/query")
public class RestQueryController {
    @Autowired
    private QueryService queryService;

    @RequestMapping(value = "{id:(?!^(?:register)$)^[a-zA-Z0-9]+$}", method = RequestMethod.GET)
    public Query getItem(@PathVariable String id) {
        return this.getQueryService().getQuery(id);
    }

    protected QueryService getQueryService() {
        return queryService;
    }
}
