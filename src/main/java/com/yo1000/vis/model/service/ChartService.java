package com.yo1000.vis.model.service;

import com.yo1000.vis.model.data.Query;
import com.yo1000.vis.model.repository.chart.ChartRepository;
import com.yo1000.vis.model.repository.system.QueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yoichi.kikuchi on 15/06/09.
 */
@Service
public class ChartService {
    @Autowired
    private ChartRepository chartRepository;

    @Autowired
    private QueryRepository queryRepository;

    private QueryCache cache = new QueryCache();

    public List<Map<String, Object>> getItems(String key) {
        QuerySignature signature = new QuerySignature("raw", key);

        if (this.getCache().exists(signature)) {
            return this.getCache().get(signature);
        }

        Query query = this.getQueryRepository().findByKey(key);
        List<Map<String, Object>> items = this.getChartRepository().getItems(query.getSql());

        this.getCache().set(signature, items);
        return items;
    }

    public List<Map<String, Object>> getItemsForSummary(String key) {
        QuerySignature signature = new QuerySignature("summary", key);

        if (this.getCache().exists(signature)) {
            return this.getCache().get(signature);
        }

        Query query = this.getQueryRepository().findByKey(key);
        List<Map<String, Object>> items = this.getChartRepository().getItems(query.getSql());

        for (int i = 1; i < items.size(); i++) {
            for (Map.Entry<String, Object> data : items.get(i).entrySet()) {
                if (Number.class.isAssignableFrom(data.getValue().getClass())) {
                    data.setValue(((Number) data.getValue()).doubleValue() + ((Number) items.get(i - 1).get(data.getKey())).doubleValue());
                }
            }
        }

        this.getCache().set(signature, items);
        return items;
    }

    public List<List<Object>> getItemsForSnowCover(String key, Date start, Date end) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR, calendar.getActualMinimum(Calendar.HOUR));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
        Date startDate = calendar.getTime();

        calendar.setTime(end);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR, calendar.getActualMaximum(Calendar.HOUR));
        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));
        Date endDate = calendar.getTime();

        QuerySignature signature = new QuerySignature("snowcover", key, startDate, endDate);

        if (this.getCache().exists(signature)) {
            return this.getCache().get(signature);
        }

        Query query = this.getQueryRepository().findByKey(key);

        List<List<Object>> items = new ArrayList<List<Object>>();
        Date work = new Date(startDate.getTime());

        while (!work.after(endDate)) {
            items.add(this.getChartRepository().getItemsForSnowCover(query.getSql(), work));

            calendar.setTime(work);
            calendar.add(Calendar.MONTH, 1);
            work = calendar.getTime();
        }

        int maxSize = 0;

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).size() > maxSize) {
                maxSize = items.get(i).size();
            }
        }

        for (List<Object> item : items) {
            while (item.size() < maxSize) {
                item.add(1, 0);
            }
        }

        this.getCache().set(signature, items);
        return items;
    }

    protected ChartRepository getChartRepository() {
        return chartRepository;
    }

    protected QueryRepository getQueryRepository() {
        return queryRepository;
    }

    protected QueryCache getCache() {
        return cache;
    }

    public static class QuerySignature {
        private Object[] args;

        public QuerySignature(Object... args) {
            this.setArgs(args);
        }

        @Override
        public int hashCode() {
            int code = 5711;

            for (Object arg : this.getArgs()) {
                code ^= (arg == null) ? 0 : arg.hashCode();
            }

            return code;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof QuerySignature)) {
                return false;
            }

            QuerySignature signature = (QuerySignature) obj;

            if (signature.getArgs().length != this.getArgs().length) {
                return false;
            }

            for (int i = 0; i <  signature.getArgs().length; i++) {
                Object o1 = signature.getArgs()[i];
                Object o2 = this.getArgs()[i];

                if (o1 == o2) {
                    continue;
                }

                if (!o1.equals(o2)) {
                    return false;
                }
            }

            return true;
        }

        @Override
        public String toString() {
            return "QuerySignature{" +
                    "args=" + Arrays.toString(args) +
                    '}';
        }

        protected Object[] getArgs() {
            return args;
        }

        protected void setArgs(Object[] args) {
            this.args = args;
        }
    }

    public static class QueryCache {
        private Map<QuerySignature, Object> cache = new ConcurrentHashMap<QuerySignature, Object>();
        private long interval = 1000L * 60L * 60L * 8L;
        private long expires = System.currentTimeMillis() + this.getInterval();

        public boolean exists(QuerySignature signature) {
            return this.get(signature) != null;
        }

        public <T> T set(QuerySignature signature, T item) {
            this.getCache().put(signature, item);
            this.updateExpires();

            return item;
        }

        public <T> T get(QuerySignature signature) {
            if (System.currentTimeMillis() > this.getExpires()) {
                return null;
            }

            if (!this.getCache().containsKey(signature)) {
                return null;
            }

            return (T) this.getCache().get(signature);
        }

        protected void updateExpires() {
            if (System.currentTimeMillis() > this.getExpires()) {
                this.getCache().clear();
                this.setExpires(System.currentTimeMillis() + this.getInterval());
            }
        }

        protected Map<QuerySignature, Object> getCache() {
            return cache;
        }

        protected long getInterval() {
            return interval;
        }

        protected void setInterval(long interval) {
            this.interval = interval;
        }

        protected long getExpires() {
            return expires;
        }

        protected void setExpires(long expires) {
            this.expires = expires;
        }
    }
}
