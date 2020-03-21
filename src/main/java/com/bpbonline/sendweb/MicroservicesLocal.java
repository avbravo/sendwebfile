/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bpbonline.sendweb;

import com.avbravo.jmoordbutils.JsfUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

/**
 *
 * @author avbravo
 */
public class MicroservicesLocal {
    
    // <editor-fold defaultstate="collapsed" desc="Response sendFileWithJaxRsHeaders(String directory, String filename, String url, String pathResouresMicroservices,String nameHeader,  String valueHeader)">
      public static Response sendFileWithJaxRsHeaders(String directory, String filename, String url, String pathResouresMicroservices,String nameParamHeader,  String valueParamHeader) {
        Response response = null;
        try {
            File fileToSend = new File(directory + filename);
            //---InputStream
            InputStream fileInStream = new FileInputStream(fileToSend);
            
             //--contentDisposition
            String contentDisposition = "attachment; filename=\"" + fileToSend.getName() + "\"";
          

            Client client = ClientBuilder.newClient();
            response = client.target(url).path(pathResouresMicroservices).request()
                    .header("Content-Disposition", contentDisposition)
                    .header(nameParamHeader, ";"+ nameParamHeader+"r=\""+ valueParamHeader+   "\"")
                    .post(Entity.entity(fileInStream, MediaType.APPLICATION_OCTET_STREAM));

        } catch (Exception e) {

            JsfUtil.errorDialog("sendFileWithJaxRsHeaders)", e.getLocalizedMessage());
        }
        return response;

    }
      
      // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Response sendFileWithJaxRs(String directory, String filename, String url, String pathResouresMicroservices))">
      public static Response sendFileWithJaxRs(String directory, String filename, String url, String pathResouresMicroservices) {
        Response response = null;
        try {
            File fileToSend = new File(directory + filename);
            //---InputStream
            InputStream fileInStream = new FileInputStream(fileToSend);
            
             //--contentDisposition
            String contentDisposition = "attachment; filename=\"" + fileToSend.getName() + "\"";

            Client client = ClientBuilder.newClient();
            response = client.target(url).path(pathResouresMicroservices).request()
                    .header("Content-Disposition", contentDisposition)
                    .post(Entity.entity(fileInStream, MediaType.APPLICATION_OCTET_STREAM));

        } catch (Exception e) {

            JsfUtil.errorDialog("sendFileToMicroservices()", e.getLocalizedMessage());
        }
        return response;

    }
      
      // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Response sendFileWithJaxRs(String directory, String filename, String url, String pathResouresMicroservices))">
      public static Response sendFileWithJaxRsAuthentification(String directory, String filename, String url, String pathResouresMicroservices,HttpAuthenticationFeature httpAuthenticationFeature) {
        Response response = null;
        try {
            File fileToSend = new File(directory + filename);
            //---InputStream
            InputStream fileInStream = new FileInputStream(fileToSend);
            
             //--contentDisposition
            String contentDisposition = "attachment; filename=\"" + fileToSend.getName() + "\"";

            Client client = ClientBuilder.newClient();
             client.register(httpAuthenticationFeature);
            response = client.target(url).path(pathResouresMicroservices).request()
                    .header("Content-Disposition", contentDisposition)
                    .post(Entity.entity(fileInStream, MediaType.APPLICATION_OCTET_STREAM));

        } catch (Exception e) {

            JsfUtil.errorDialog("sendFileToMicroservices()", e.getLocalizedMessage());
        }
        return response;

    }
      
      // </editor-fold>
}
