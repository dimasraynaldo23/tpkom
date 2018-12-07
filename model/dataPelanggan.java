package com.gawe.tpkom.Model;

import java.io.Serializable;

public class dataPelanggan implements Serializable {

    private String kdSales;
    private String nmrMobi;
    private String namaPlg;
    private String nmrPlg;
    private String nmrAlt;
    private String relasiPlg;
    private String almtInstl;
    private String ptknAlmt;
    private String ktp_url;

    public dataPelanggan(){

    }

    public String getKdSales() {
        return kdSales;
    }

    public void setKdSales(String kdSales) {
        this.kdSales = kdSales;
    }

    public String getNmrMobi() {
        return nmrMobi;
    }

    public void setNmrMobi(String nmrMobi) {
        this.nmrMobi = nmrMobi;
    }

    public String getNamaPlg() {
        return namaPlg;
    }

    public void setNamaPlg(String namaPlg) {
        this.namaPlg = namaPlg;
    }

    public String getNmrPlg() {
        return nmrPlg;
    }

    public void setNmrPlg(String nmrPlg) {
        this.nmrPlg = nmrPlg;
    }

    public String getNmrAlt() {
        return nmrAlt;
    }

    public void setNmrAlt(String nmrAlt) {
        this.nmrAlt = nmrAlt;
    }

    public String getRelasiPlg() {
        return relasiPlg;
    }

    public void setRelasiPlg(String relasiPlg) {
        this.relasiPlg = relasiPlg;
    }

    public String getAlmtInstl() {
        return almtInstl;
    }

    public void setAlmtInstl(String almtInstl) {
        this.almtInstl = almtInstl;
    }

    public String getPtknAlmt() {
        return ptknAlmt;
    }

    public void setPtknAlmt(String ptknAlmt) {
        this.ptknAlmt = ptknAlmt;
    }

    public String getKtp_url(String ktp_url){ return ktp_url;}

    public void setKtp_url() {this.ktp_url=ktp_url;}

    @Override
    public String toString(){
        return ""+kdSales+"\n"+
                ""+nmrMobi+"\n"+
                ""+namaPlg+"\n"+
                ""+nmrPlg+"\n"+
                ""+nmrAlt+"\n"+
                ""+relasiPlg+"\n"+
                ""+almtInstl+"\n"+
                ""+ptknAlmt;
    }

    public dataPelanggan (String kdSales, String nmrMobi,String namaPlg, String nmrPlg,
                          String nmrAlt, String relasiPlg, String almtInstl, String ptknAlmt){
        this.kdSales=kdSales;
        this.nmrMobi=nmrMobi;
        this.namaPlg=namaPlg;
        this.nmrPlg=nmrPlg;
        this.nmrAlt=nmrAlt;
        this.relasiPlg=relasiPlg;
        this.almtInstl=almtInstl;
        this.ptknAlmt=ptknAlmt;
    }

    public dataPelanggan(String ktp_url){
        this.ktp_url=ktp_url;
    }
}
