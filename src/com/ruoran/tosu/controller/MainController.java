package com.ruoran.tosu.controller;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.ruoran.tosu.model.Task;
import com.ruoran.tosu.dao.TaskDaoImpl;
 
@Controller
public class MainController {

    private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());

    @Autowired
    TaskDaoImpl taskDao;
    
    public void setTaskDao(TaskDaoImpl taskDaoImpl) {
        this.taskDao = taskDaoImpl;
    }
    
    @RequestMapping(value="/", method = RequestMethod.GET)
    public String getIndex(ModelMap model) {
 
        model.addAttribute("message", "Hello from GAE");
        return "index";
    }
    
    @RequestMapping(value="/users", method = RequestMethod.GET)
    public String getHi(ModelMap model) {
        model.addAttribute("message", taskDao.satHi());
        return "index";
    }
    
    @RequestMapping(value="/tasks", method = RequestMethod.GET)
    public String getTasks(ModelMap model, HttpServletRequest req) {
        
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        if (user != null) {
            String logoutURL = userService.createLogoutURL("/");
            model.addAttribute("username", user.getNickname());
            model.addAttribute("logoutURL", logoutURL);
        } else {
            String loginURL = userService.createLoginURL(req.getRequestURI());
            return "redirect:" + loginURL;
        }
        
        List<Task> tasks = this.taskDao.selectAll();
//        LOGGER.warning(String.format("Last Created : %s", tasks.get(tasks.size()-1).getContent()));
        
        model.addAttribute("tasks", tasks);
        Task t = new Task();
        model.addAttribute("taskform", t);
        return "tasks";
    }
    
    @RequestMapping(value="/tasks/{key}/start", method = RequestMethod.GET)
    @ResponseBody
    public String startTask(@PathVariable String key, ModelMap model) {
        int secLeft = taskDao.start(key);
        
        // TODO return json 
        if (secLeft != -1)  {
            return String.valueOf(secLeft);
        } else {
            return "ERROR";
        }
    }
    
    @RequestMapping(value="/tasks/{key}/pause", method = RequestMethod.GET)
    @ResponseBody
    public String pauseTask(@PathVariable String key, ModelMap model) {
        int secPassed = taskDao.pause(key);
        
        // TODO return json 
        if (secPassed != -1)  {
            return String.valueOf(secPassed);
        } else {
            return "ERROR";
        }
    }
    
    public void endTask() {
        // TODO when list tasks. go through those, mark startDate to null if expired
    }
    
    @RequestMapping(value="/tasks/{key}/detail", method = RequestMethod.GET)
    public String getTaskDetail(@PathVariable String key, ModelMap model) {
        model.addAttribute("task", this.taskDao.get(key));
        return "detail";
    }
    
    @RequestMapping(value = "/tasks/create", method = RequestMethod.POST)
    public String newTask(@ModelAttribute Task task, RedirectAttributes redirectAttributes) {
        
        String content = task.getContent();
        int timeUnitPlaned = task.getTimeUnitPlaned();
        
//        if (this.taskDao.create(content, timeUnitPlaned) != null) {
//            return "OK"; 
//        } else {
//            return "ERROR";
//        }
        
        if (this.taskDao.create(content, timeUnitPlaned) != null) {
            LOGGER.warning(String.format("Created : %s", content));
        } else {
            LOGGER.warning("ERROR");
        }
        
        return "redirect:/tasks";
    }
    
    @RequestMapping(value = "/tasks/{key}/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteTask(@PathVariable String key, HttpServletResponse response) {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        if (this.taskDao.delete(key))  {
            return "OK";
        } else {
            return "ERROR";
        }
    }
}