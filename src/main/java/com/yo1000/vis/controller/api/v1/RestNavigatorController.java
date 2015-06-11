package com.yo1000.vis.controller.api.v1;

import com.yo1000.vis.model.data.NavigatorGroup;
import com.yo1000.vis.model.service.NavigatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by yoichi.kikuchi on 15/06/07.
 */
@RestController
@RequestMapping("api/v1/navigator")
public class RestNavigatorController {
    @Autowired
    private NavigatorService navigatorService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<NavigatorGroup> getItems() {
        return this.getNavigatorService().getTrimmedNavigatorGroups();
    }

    protected NavigatorService getNavigatorService() {
        return navigatorService;
    }
}
