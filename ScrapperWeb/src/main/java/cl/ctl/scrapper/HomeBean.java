package cl.ctl.scrapper;

import cl.ctl.scrapper.controllers.Executor;
import cl.ctl.scrapper.helpers.LogHelper;
import org.omnifaces.util.Ajax;
import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by des01c7 on 15-12-16.
 */

@ManagedBean(name = "homeBean")
@ViewScoped
public class HomeBean {

    private static int timeOut;

    Date date = new Date();
    boolean dateSelected = false;
    Executor executor;
    LogHelper logHelper = LogHelper.getInstance();
    String process;

    int logAmount = 0;

    @PostConstruct
    public void init() {
        executor = new Executor();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void selectDate() {
        dateSelected = true;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public void process() {
        RequestContext reqCtx = RequestContext.getCurrentInstance();
        LogHelper.getInstance().getLogs().clear();

        try {
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            process = getProcessName(localDate);


            if(!dateSelected) {
                executor.process();
            }
            else {
                executor.process(localDate);
            }

            reqCtx.execute("PF('poll').stop();");
            reqCtx.getCurrentInstance().execute("cancel();");
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "El proceso se ha completado correctamente"));

        } catch (Exception e) {
            reqCtx.execute("PF('poll').stop();");
            reqCtx.getCurrentInstance().execute("cancel();");
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Ocurrió un error durante el proceso"));
            e.printStackTrace();
        }

    }

    public LogHelper getLogHelper() {
        return logHelper;
    }

    public void setLogHelper(LogHelper logHelper) {
        this.logHelper = logHelper;
    }

    public void scroll() {
        if(LogHelper.getInstance().getLogs().size() > logAmount) {
            logAmount = LogHelper.getInstance().getLogs().size();
            //RequestContext.getCurrentInstance().execute("scroll();");
            RequestContext.getCurrentInstance().scrollTo("j_idt51:progressBarClient");
            Ajax.update("j_idt51:logs");
        }
    }

    public String getProcessName(LocalDate localDate) {
        String year = String.valueOf(localDate.getYear());
        String month = String.valueOf(localDate.getMonth());
        String day = String.valueOf(localDate.getDayOfMonth());

        if(localDate.getMonthValue() < 10) {
            month = "0" + month;
        }

        if(localDate.getDayOfMonth() < 10) {
            day = "0" + day;
        }

        return year + month + day;
    }

}
