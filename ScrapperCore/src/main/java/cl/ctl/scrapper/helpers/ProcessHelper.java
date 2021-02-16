package cl.ctl.scrapper.helpers;

import java.time.LocalDate;

/**
 * Created by des01c7 on 18-12-20.
 */
public class ProcessHelper {

    private static final ProcessHelper instance = new ProcessHelper();

    private LocalDate processDate  = LocalDate.now().minusDays(1);

    /**
     * Constructor privado para el Singleton del Factory.
     */
    private ProcessHelper() {
        processDate  = LocalDate.now().minusDays(1);
    }

    public static ProcessHelper getInstance() {
        return instance;
    }

    public LocalDate getProcessDate() {
        return processDate;
    }

    public void setProcessDate(LocalDate processDate) {
        this.processDate = processDate;
    }
}
