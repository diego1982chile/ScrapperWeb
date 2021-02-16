package cl.ctl.scrapper.helpers;

import cl.ctl.scrapper.model.FileControl;
import cl.ctl.scrapper.model.Log;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Created by des01c7 on 18-12-20.
 */
public class LogHelper extends Handler {

    private static final LogHelper instance = new LogHelper();

    /** La lista de tagSMTK */
    private List<FileControl> fileControlList = new ArrayList<FileControl>();

    private List<Log> logs = new ArrayList<>();

    /** Mapa de tagSMTK por su nombre. */
    private ConcurrentHashMap<String, FileControl> fileControlMap = new ConcurrentHashMap<String,FileControl>();

    public List<FileControl> getFileControlList() {
        return fileControlList;
    }

    /**
     * Constructor privado para el Singleton del Factory.
     */
    private LogHelper() {
        /*
        this.fileControlList = new ArrayList<FileControl>();
        this.fileControlMap = new ConcurrentHashMap<String,FileControl>();
        */
    }

    public static LogHelper getInstance() {
        return instance;
    }

    /**
     * Este método es responsable de retornar el tipo de descripción llamado FSN.
     *
     * @return Retorna una instancia de FSN.
     */
    public FileControl findFileControlByName(String name) {

        if (fileControlMap.containsKey(name)) {
            return this.fileControlMap.get(name);
        }

        return null;
    }

    /**
     * Este método es responsable de asignar un nuevo conjunto de tagsSMTJ. Al hacerlo, es necesario actualizar
     * los mapas.
     */
    public void registerFileControl(FileControl fileControl) {
        /* Se actualiza el mapa por nombres */
        fileControlList.add(fileControl);
    }

    public FileControl findByFileName(String fileName) {
        for (FileControl fileControl : fileControlList) {
            if(fileControl.getFileName().equals(fileName)) {
                return fileControl;
            }
        }
        return null;
    }

    public void updateFileNames() {
        /* Se actualiza el mapa por nombres */
        for (FileControl fileControl : fileControlList) {
            fileControl.setFileName(fileControl.getFileName().replace(".zip",".csv"));
        }
    }

    public void log(Log log) {
        /* Se actualiza el mapa por nombres */
        logs.add(log);
    }

    public List<Log> getLogs() {
        return logs;
    }

    @Override
    public void publish(LogRecord record) {
        Instant instance = Instant.ofEpochMilli(record.getMillis());
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instance, ZoneId.systemDefault());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
        String string = localDateTime.format(formatter);

        logs.add(new Log(string, record.getSourceClassName(), record.getSourceMethodName(), record.getMessage()));

        if(record.getLevel().equals(Level.SEVERE)) {
            try {
                ErrorHelper.getInstance().sendMail();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }


}
