package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.dao.APRequestDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/aprequest")
public class APRequestController {

    private APRequestDAO apRequestDao;

    @Autowired
    public void setApRequestDao(APRequestDAO apRequestDao) {
        this.apRequestDao = apRequestDao;
    }

    @RequestMapping("/list")
    public String listRequests(Model model) {
        model.addAttribute("requests", apRequestDao.getAPRequests());

        return "aprequest/list";
    }
}