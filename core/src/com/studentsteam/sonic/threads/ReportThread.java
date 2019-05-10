package com.studentsteam.sonic.threads;

import com.badlogic.gdx.audio.Sound;
import com.studentsteam.sonic.tools.database.DatabaseReport;
import org.apache.log4j.Logger;

public class ReportThread implements Runnable {
    //Logs
    private final Logger log = Logger.getLogger(this.getClass());

    public Thread thrd; //Link thread
    //Состояния (сохранять в PDF или же в HTML)
    private State currentState;
    public enum State {HTML,PDF}
    //Звуки
    private Sound saveSound;
    public ReportThread(String name,State state,Sound saveSound){
        this.saveSound = saveSound;
        this.currentState = state;
        thrd = new Thread(this,name);
        //Приоритеты
        thrd.setPriority(Thread.MIN_PRIORITY);
    }
    public void start(){
        thrd.start(); //Launch thread
    }
    @Override
    public void run() {
        log.debug("Запуск потока");

        switch (currentState){
            case PDF:
                log.info("Сохранение отчета в PDF");
                DatabaseReport.printReport("report.jrxml","report.pdf");
                break;
            case HTML:
                log.info("Сохранение отчета в HTML");
                DatabaseReport.printReport("reportHtml.jrxml","report.html");
                break;
        }
        //Проигрываем звук после того как закончили создание отчета
        saveSound.play(0.3f);
        log.debug("Завершение потока");
    }
}
