package com.mentalhealthapp.model;
import com.mentalhealthapp.model.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private int aid;
    private int seid;
    private int cid;
    private LocalDate date;
    private LocalTime time;
    private String status;
    private String medicine;

    
    public Appointment() {}
    
    public Appointment(int seid, int cid, LocalDate date, LocalTime time, String status) {
        this.seid = seid;
        this.cid = cid;
        this.date = date;
        this.time = time;
        this.status = status;
    }
    
    public Appointment(int aid, int seid, int cid, LocalDate date, LocalTime time, String status) {
        this.aid = aid;
        this.seid = seid;
        this.cid = cid;
        this.date = date;
        this.time = time;
        this.status = status;
    }
    
    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }
    
    public int getAid() {
        return aid;
    }
    
    public void setAid(int aid) {
        this.aid = aid;
    }
    
    public int getSeid() {
        return seid;
    }
    
    public void setSeid(int seid) {
        this.seid = seid;
    }
    
    public int getCid() {
        return cid;
    }
    
    public void setCid(int cid) {
        this.cid = cid;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public LocalTime getTime() {
        return time;
    }
    
    public void setTime(LocalTime time) {
        this.time = time;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "Appointment{" +
                "aid=" + aid +
                ", seid=" + seid +
                ", cid=" + cid +
                ", date=" + date +
                ", time=" + time +
                ", status='" + status + '\'' +
                '}';
    }
}
