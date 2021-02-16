package cl.ctl;

import cl.ctl.scrapper.controllers.Executor;
import cl.ctl.scrapper.helpers.LogHelper;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

/**
 * Created by des01c7 on 15-12-16.
 */

@ManagedBean(name = "homeBean")
@ViewScoped
public class HomeBean {

    private static int timeOut;

    Date date = new Date();
    Date theDate = null;
    Executor executor;
    LogHelper logHelper = LogHelper.getInstance();

    @PostConstruct
    public void init() {
        executor = new Executor();
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
        theDate = date;
    }

    public void process() {
        try {
            if(theDate == null) {
                executor.process();
            }
            else {
                executor.process(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LogHelper getLogHelper() {
        return logHelper;
    }

    public void setLogHelper(LogHelper logHelper) {
        this.logHelper = logHelper;
    }

}
