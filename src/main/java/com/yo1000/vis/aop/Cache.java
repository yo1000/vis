package com.yo1000.vis.aop;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yoichi.kikuchi on 2015/06/25.
 */
public @interface Cache {
    public static class Signature {
        private Object[] args;

        public Signature(Object... args) {
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
            if (!(obj instanceof Signature)) {
                return false;
            }

            Signature signature = (Signature) obj;

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

    public static class Store {
        private Map<Signature, Object> cache = new ConcurrentHashMap<Signature, Object>();
        private long interval = 1000L * 60L * 60L * 8L;
        private long expires = System.currentTimeMillis() + this.getInterval();

        public boolean exists(Signature signature) {
            return this.get(signature) != null;
        }

        public <T> T set(Signature signature, T item) {
            this.getCache().put(signature, item);
            this.updateExpires();

            return item;
        }

        public <T> T get(Signature signature) {
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

        protected Map<Signature, Object> getCache() {
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
