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
public class DS_TCSTCC
{
    private String tcc_id;
    private String tcc_name;
    
    public DS_TCSTCC(){}
    
    public DS_TCSTCC(String tcc_id, String tcc_name)
    {
        this.tcc_id = tcc_id;
        this.tcc_name = tcc_name;
    }
    
    public String getTccid()
    {
        return tcc_id;
    }
    
    public void setTccid(String tcc_id)
    {
        this.tcc_id = tcc_id;
    }
    
    public String getTccname()
    {
        return tcc_name;
    }
    
    public void setTccname(String tcc_name)
    {
        this.tcc_name = tcc_name;
    }
}
