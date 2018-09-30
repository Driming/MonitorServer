package com.hc.util.websocket.feedback;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.hc.dao.CollectionMonitorDao;
import com.hc.entity.collection.task.CollectionTask;
import com.hc.entity.collection.task.record.CollectionTaskUpdateRecord;
import com.hc.entity.control.user.User;
import com.hc.util.StringUtils;
import com.hc.util.context.ContextCore;

public class WebSocketFeedbackFlag {
    private static final Map<Integer, TaskModify> feedback = new ConcurrentHashMap<Integer, TaskModify>();
    private static CollectionMonitorDao collectionStatusDao = null;

    static {
        collectionStatusDao = (CollectionMonitorDao) ContextCore.getBean(CollectionMonitorDao.class);
    }

    public static Integer put(int type, User user, CollectionTask ct) {
        synchronized (feedback) {
            Integer key;
            do {
                key = StringUtils.createId(8);
                CollectionTaskUpdateRecord existRecord =
                        collectionStatusDao.selectCollectionTaskUpdateRecord(key);
                if (existRecord == null && key > 0) {
                    break;
                }
            } while (true);
            TaskModify tm = new TaskModify(type, user, ct);
            feedback.put(key, tm);
            return key;
        }
    }

    public static TaskModify get(Integer key) {
        return feedback.get(key);
    }

    public static TaskModify remove(Integer key) {
        synchronized (feedback) {
            return feedback.remove(key);
        }
    }

    public static class TaskModify {
        // 1,创建任务
        // 2,修改任务状态
        // 3,修改任务配置
        private int type;
        private User user;
        private CollectionTask ct;

        public TaskModify(int type, User user, CollectionTask ct) {
            super();
            this.type = type;
            this.user = user;
            this.ct = ct;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public CollectionTask getCt() {
            return ct;
        }

        public void setCt(CollectionTask ct) {
            this.ct = ct;
        }

    }
}
