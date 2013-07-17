package com.ruoran.tosu.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.joda.time.Instant;
import org.joda.time.Interval;
import org.springframework.stereotype.Service;

import com.google.appengine.api.datastore.DatastoreFailureException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.ruoran.tosu.controller.MainController;
import com.ruoran.tosu.model.Task;

@Service
public class TaskDaoImpl implements TaskDao{
    
    private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());

    @Override
    public String create(String content, Integer timeUnitPlaned) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService(); 
        Entity task = new Entity("Task");
        Date creationDate = new Date();
        
        task.setProperty("content", content);
        task.setProperty("timeUnitPlaned", timeUnitPlaned);
        task.setProperty("timeUnitUsed", 0);
        task.setProperty("creationDate", creationDate);
        task.setProperty("status", Task.IDLE);
        task.setProperty("secPassed", -1);
        task.setProperty("secLeft", -1);

        datastore.put(task);
        
        return KeyFactory.keyToString(task.getKey());
    }

    @Override
    public List<Task> selectAll() {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService(); 
        List<Task> list = new ArrayList<Task>();
        
        Query q = new Query("Task");
        PreparedQuery pq = datastore.prepare(q);
        
        Task t;
        for (Entity result : pq.asIterable()) {
            t = new Task();
            t.setContent((String) result.getProperty("content"));
            t.setTimeUnitPlaned((int) (long) result.getProperty("timeUnitPlaned"));
            t.setTimeUnitUsed((int) (long) result.getProperty("timeUnitUsed"));
            t.setKey(KeyFactory.keyToString(result.getKey()));
            list.add(t);
        }
        return list;
    }

    @Override
    public String satHi() {
        return "hihihihi";
    }

    @Override
    public boolean delete(String key) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService(); 
        try {
            datastore.delete(KeyFactory.stringToKey(key));
            return true;
        } catch (IllegalArgumentException e) {
            // TODO log error here
            return false;
        }
    }
    
    @Override
    public Task get(String key) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService(); 
        try {
            Entity entity = datastore.get(KeyFactory.stringToKey(key));
            Task t = new Task();
            t.setContent((String) entity.getProperty("content"));
            t.setSummary((String) entity.getProperty("summary"));
            t.setTimeUnitPlaned((int) (long) entity.getProperty("timeUnitPlaned"));
            t.setTimeUnitUsed((int) (long) entity.getProperty("timeUnitUsed"));
            t.setCreationDate((Date) entity.getProperty("creationDate"));
            t.setFinishDate((Date) entity.getProperty("finishDate"));
            t.setKey(key);
            
            return t;
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            // TODO log error here
            return null;
        }
    }

    @Override
    public int start(String key) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService(); 
        int secLeft = -1;
        try {
            Entity entity = datastore.get(KeyFactory.stringToKey(key));
            
            int status = (int)(long) entity.getProperty("status");
            int secPassed = (int)(long) entity.getProperty("secPassed");
                    
            // if task can not be started, secLeft = 0 
            if (status == Task.IDLE) {
                // task can be started
                Date startDate = new Date();
                entity.setProperty("startDate", startDate);
                entity.setProperty("status", Task.ACTIVE);
                secLeft = 25 * 60 - secPassed;

                datastore.put(entity);
            }
        } catch (IllegalArgumentException | DatastoreFailureException e) {
            // TODO log error here
        } catch (EntityNotFoundException e) {
            // TODO Auto-generated catch block
        }
        return secLeft;
    }

    @Override
    public int pause(String key) {
        // TODO resume need to know how many time left
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService(); 
        int secPassed = -1;
        try {
            Entity entity = datastore.get(KeyFactory.stringToKey(key));
            int status = (int)(long) entity.getProperty("status");
            
            if (status == Task.ACTIVE) {
                // return -1 if this block is not reached
                secPassed = (int)(long) entity.getProperty("secPassed");
                Date startDate = (Date) entity.getProperty("startDate");
                Interval interval = new Interval(startDate.getTime(), Instant.now().getMillis());
                secPassed += (int) (interval.toDurationMillis() / 1000);

                entity.setProperty("status", Task.IDLE);
                entity.setProperty("secPassed", secPassed);
                datastore.put(entity);
            }
        } catch (IllegalArgumentException | DatastoreFailureException e) {
            // TODO log error here
        } catch (EntityNotFoundException e) {
            // TODO Auto-generated catch block
        } 
        return secPassed;
    }

    /**
     * timeout will be checked by any get task action
     */
    @Override
    public void reset(String key) {
        try {
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService(); 
            Entity entity = datastore.get(KeyFactory.stringToKey(key));
            
            int status = (int)(long) entity.getProperty("status");
            
            if (status == Task.ACTIVE) {
                int secPassed = (int)(long) entity.getProperty("secPassed");
                Date startDate = (Date) entity.getProperty("startDate");
                Interval interval = new Interval(startDate.getTime(), Instant.now().getMillis());
                secPassed = (int) (interval.toDurationMillis() / 1000);
                if (secPassed > 25 * 60) {
                    // TODO log time out
                    entity.setProperty("status", Task.IDLE);
                    datastore.put(entity);
                }
            }
        } catch (EntityNotFoundException e) {
            // TODO Auto-generated catch block
        }
    }

    @Override
    public String update(Task task) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void finish(String key) {
        try {
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService(); 
            Entity entity = datastore.get(KeyFactory.stringToKey(key));
            
            int status = (int)(long) entity.getProperty("status");
            
            if (status == Task.ACTIVE) {
                int secPassed = (int)(long) entity.getProperty("secPassed");
                Date startDate = (Date) entity.getProperty("startDate");
                Interval interval = new Interval(startDate.getTime(), Instant.now().getMillis());
                secPassed += (int) (interval.toDurationMillis() / 1000);
                
                // if difference is within 30 seconds, valid operation
                if (Math.abs(secPassed - 25 * 60) < 30) {
                    // TODO log done ok 
                    entity.setProperty("status", Task.DONE);
                } else {
                    // TODO some thing wrong
                    entity.setProperty("status", Task.IDLE);
                }
                datastore.put(entity);
            }
        } catch (EntityNotFoundException e) {
            // TODO Auto-generated catch block
        }
    }
}
