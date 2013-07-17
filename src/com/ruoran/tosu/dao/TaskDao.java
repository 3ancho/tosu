package com.ruoran.tosu.dao;

import java.util.Date;
import java.util.List;

import com.ruoran.tosu.model.Task;

public interface TaskDao {
    String create(String content, Integer timeUnitPlaned);
    String update(Task task);
    Task get(String key);
    List<Task> selectAll();
    boolean delete(String key);
    
    int start(String key);
    int pause(String key);
    void reset(String key);
    void finish(String key);
    
    
    String satHi();
}
