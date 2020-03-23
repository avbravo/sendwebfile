/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bpbonline.sendweb;

import com.avbravo.jmoordbutils.JsfUtil;
import com.avbravo.jmoordbutils.jaxrs.Microservices;
import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.ws.rs.core.Response;

/**
 *
 * @author avbravo
 */
@Named
@ViewScoped
public class FileController implements Serializable {

    private String directory = JsfUtil.userHome() + JsfUtil.fileSeparator() + "fiscalprinter" + JsfUtil.fileSeparator() + "license" + JsfUtil.fileSeparator();

    
    // <editor-fold defaultstate="collapsed" desc="String send()">
    public String send() {

        try {
            //--Files to send 
          Response response  =Microservices.sendFileWithJaxRsHeaders(directory, "license.enc", "http://localhost:8080/serverfiles/resources/file", "upload"
                  ,"folder","license");
        Response responseivEnc =  Microservices.sendFileWithJaxRsHeaders(directory, "licenseiv.enc", "http://localhost:8080/serverfiles/resources/file", "upload"
                ,"folder","license");
       Response responseDes   =  Microservices.sendFileWithJaxRsHeaders(directory, "license.des", "http://localhost:8080/serverfiles/resources/file", "upload"
              ,"folder" ,"license");
               
           String mensajeExistoso="";
           String mensajeFallido="";
            if (response.getStatus() == 200 && responseivEnc.getStatus() == 200 &&  responseDes.getStatus() == 200)  {
              JsfUtil.infoDialog("Envio existoso"," Se enviaron exitosamente los archivos: licemse.emc, licenseiv.enc, license.des");
            } else{
                         if (response.getStatus() == 200){
                             mensajeExistoso+=" license.enc";
                         }else{
                             mensajeFallido=" license.enc";
                         }
                         if (responseivEnc.getStatus() == 200){
                             mensajeExistoso+=" licenseiv.enc";
                         }else{
                             mensajeFallido+=" licenseiv.enc";
                         }
                         
                         if (responseDes.getStatus() == 200){
                             mensajeExistoso+=" license.des";
                         }else{
                             mensajeFallido+=" license.desc";
                         }
                        JsfUtil.infoDialog("Proceso terminado","Exitosos:"+mensajeExistoso +  "    Fallidps: "+mensajeFallido);     
                         
            }
    

        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.errorDialog("send)=", e.getLocalizedMessage());
        }
        return "";
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="String sendZip()">
    public String sendZip() {

        try {
            //--Files to send 
          Response response  =Microservices.sendFileWithJaxRs(directory, "authorizedlicense.zip", "http://localhost:8080/serverfiles/resources/zip", "upload");
     
               
            System.out.println("response.getStatus() "+response.getStatus());
            System.out.println("response.getStatusInfo() "+response.getStatusInfo());
            if (response.getStatus() == 200 )  {
              JsfUtil.infoDialog("Envio existoso"," Se enviaron exitosamente el archivo authorizedlicense.zip");
            } else{
                       
                       JsfUtil.warningDialog("Envio errado","  No Se envio el archivo authorizedlicense.zip");
                         
            }
    

        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.errorDialog("sendZip()", e.getLocalizedMessage());
        }
        return "";
    }
    // <editor-fold defaultstate="collapsed" desc="method()">
 
}
