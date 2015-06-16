package com.yo1000.vis.controller.api.v1;

import com.yo1000.vis.model.data.Query;
import com.yo1000.vis.model.data.Widget;
import com.yo1000.vis.model.service.ChartService;
import com.yo1000.vis.model.service.WidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by yoichi.kikuchi on 15/06/07.
 */
@RestController
@RequestMapping("api/v1/widget")
public class RestWidgetController {
    @Autowired
    private WidgetService widgetService;

    @Autowired
    private ChartService chartService;

    @RequestMapping(value = "messages", method = RequestMethod.GET)
    public List<Map<String, Object>> getMessages() {
        Map.Entry<Widget, Query> widgetQueryEntry = this.getWidgetService()
                .getMessageWidgets().entrySet().iterator().next();

        return this.getChartService().getItems(widgetQueryEntry.getValue().getKey());
    }

    protected WidgetService getWidgetService() {
        return widgetService;
    }

    protected ChartService getChartService() {
        return chartService;
    }
}
