/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.sql.ResultSet;

/**
 *
 * @author Duc Dung Dan
 */
public interface DataInterface
{
    ResultSet getStatisticalData(String statisticalName);
    
}
