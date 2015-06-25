package com.yo1000.vis.controller.api.v1;

import com.yo1000.vis.model.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yoichi.kikuchi on 15/06/09.
 */
@RestController
@RequestMapping("api/v1/chart")
public class RestChartController {
    @Autowired
    private ChartService chartService;

    @RequestMapping("{key:^[0-9a-zA-Z_\\-]+$}")
    public List<Map<String, Object>> getItems(@PathVariable String key) {
        return this.getChartService().getItems(key);
    }

    @RequestMapping("{key:^[0-9a-zA-Z_\\-]+$}/summary")
    public List<Map<String, Object>> getItemsForSummary(@PathVariable String key) {
        return this.getChartService().getItemsForSummary(key);
    }

    @RequestMapping("{key:^[0-9a-zA-Z_\\-]+$}/snowcover")
    public List<List<Object>> getItemsForSnowCover(@PathVariable String key) {
        Date today = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.YEAR, -1);

        return this.getChartService().getItemsForSnowCover(key,
                this.normalizeStartDate(calendar.getTime()),
                this.normalizeEndDate(today));
    }

    @RequestMapping("{key:^[0-9a-zA-Z_\\-]+$}/snowcover/{start}")
    public List<List<Object>> getItemsForSnowCoverByDateRange(
            @PathVariable String key, @PathVariable Date start) {
        Date today = new Date(System.currentTimeMillis());

        return this.getChartService().getItemsForSnowCover(key,
                this.normalizeStartDate(start),
                this.normalizeEndDate(today));
    }

    @RequestMapping("{key:^[0-9a-zA-Z_\\-]+$}/snowcover/{start}/{end}")
    public List<List<Object>> getItemsForSnowCoverByDateRange(
            @PathVariable String key, @PathVariable Date start, @PathVariable Date end) {
        return this.getChartService().getItemsForSnowCover(key,
                this.normalizeStartDate(start),
                this.normalizeEndDate(end));
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }

    protected Date normalizeStartDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR, calendar.getActualMinimum(Calendar.HOUR));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
        return calendar.getTime();
    }

    protected Date normalizeEndDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR, calendar.getActualMaximum(Calendar.HOUR));
        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));
        return calendar.getTime();
    }

    protected ChartService getChartService() {
        return chartService;
    }
}
