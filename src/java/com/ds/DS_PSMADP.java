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
public class DS_PSMADP
{
    private String dep_id;
    private String dep_name;
    
    public DS_PSMADP(){}
    
    public DS_PSMADP(String dep_id, String dep_name)
    {
        this.dep_id = dep_id;
        this.dep_name = dep_name;
    }
    
    public String getDepid()
    {
        return dep_id;
    }
    
    public void setDepid(String dep_id)
    {
        this.dep_id = dep_id;
    }
    
    public String getDepname()
    {
        return dep_name;
    }
    
    public void setDepname(String dep_name)
    {
        this.dep_name = dep_name;
    }
}