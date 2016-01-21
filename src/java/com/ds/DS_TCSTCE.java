/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ds;

/**
 *
 * @author G713
 */
public class DS_TCSTCE
{
    private String tce_emp;
    private String tce_nam;
    private String tce_btd;
    private String tce_all;
    private String tce_tkb;
    private String tce_tkt;
    
    public DS_TCSTCE(){}
    
    public DS_TCSTCE(String tce_emp, String tce_nam, String tce_btd, String tce_all, String tce_tkb, String tce_tkt)
    {
        this.tce_emp = tce_emp;
        this.tce_nam = tce_nam;
        this.tce_btd = tce_btd;
        this.tce_all = tce_all;
        this.tce_tkb = tce_tkb;
        this.tce_tkt = tce_tkt;
    }
    
    public String getEmp()
    {
        return tce_emp;
    }
    
    public void setEmp(String tce_emp)
    {
        this.tce_emp = tce_emp;
    }
    
    public String getNam()
    {
        return tce_nam;
    }
    
    public void setNam(String tce_nam)
    {
        this.tce_nam = tce_nam;
    }
    
    public String getBtd()
    {
        return tce_btd;
    }
    
    public void setBtd(String tce_Btd)
    {
        this.tce_btd = tce_btd;
    }
    
    public String getAll()
    {
        return tce_all;
    }
    
    public void setAll(String tce_all)
    {
        this.tce_all = tce_all;
    }
    
    public String getTkb()
    {
        return tce_tkb;
    }
    
    public void setTkb(String tce_tkb)
    {
        this.tce_tkb = tce_tkb;
    }
    
    public String getTkt()
    {
        return tce_tkt;
    }
    
    public void setTkt(String tce_tkt)
    {
        this.tce_tkt = tce_tkt;
    }
}
