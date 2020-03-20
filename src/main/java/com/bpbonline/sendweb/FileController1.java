/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bpbonline.sendweb;

import com.avbravo.jmoordbutils.JsfUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author avbravo
 */
@Named
@ViewScoped
public class FileController1 implements Serializable {

    private String directory = JsfUtil.userHome() + JsfUtil.fileSeparator() + "fiscalprinter" + JsfUtil.fileSeparator() + "license" + JsfUtil.fileSeparator();

    public String send() {

        try {
            //--Files to send 
            File fileEnc = new File(directory + "license.enc");
            File fileIvEnc = new File(directory + "licenseiv.enc");
            File fileDes = new File(directory + "license.des");

            //---InputStream
            InputStream fileInStream = new FileInputStream(fileEnc);
            InputStream fileInStreamIvEnc = new FileInputStream(fileIvEnc);
            InputStream fileInStreamDes = new FileInputStream(fileDes);

            //--contentDisposition
            String contentDispositionFileEnc = "attachment; filename=\"" + fileEnc.getName() + "\"";
            String contentDispositionFileIvEnc = "attachment; filename=\"" + fileIvEnc.getName() + "\"";
            String contentDispositionFileDes = "attachment; filename=\"" + fileDes.getName() + "\"";

            Client client = ClientBuilder.newClient();
            Response response = client.target("http://localhost:8080/serverfiles/resources/file").path("upload").request()
                    .header("Content-Disposition", contentDispositionFileEnc)
                    .post(Entity.entity(fileInStream, MediaType.APPLICATION_OCTET_STREAM));
            if (response.getStatus() == 200) {
                JsfUtil.infoDialog(fileEnc.getName(), "Se envio el archivo exitosamente license.enc");
                /**
                 *
                 * Envia el archivo encriptado
                 */
                Client clientIvEnc = ClientBuilder.newClient();
                Response responseIvEnc = clientIvEnc.target("http://localhost:8080/serverfiles/resources/file").path("upload").request()
                        .header("Content-Disposition", contentDispositionFileIvEnc)
                        .post(Entity.entity(fileInStreamIvEnc, MediaType.APPLICATION_OCTET_STREAM));
                if (responseIvEnc.getStatus() == 200) {
                    JsfUtil.infoDialog(fileIvEnc.getName(), "Se envio el archivo exitosamente licenseiv.enc");

                    /**
                     *
                     * Envia el archivo encriptado
                     */
                    Client clientDes = ClientBuilder.newClient();
                    Response responseDes = clientDes.target("http://localhost:8080/serverfiles/resources/file").path("upload").request()
                            .header("Content-Disposition", contentDispositionFileDes)
                            .post(Entity.entity(fileInStreamDes, MediaType.APPLICATION_OCTET_STREAM));
                    if (responseIvEnc.getStatus() == 200) {
                        JsfUtil.infoDialog(fileIvEnc.getName(), "Se envio el archivo exitosamente license.des");

                    } else {
                        JsfUtil.infoDialog(fileIvEnc.getName(), "No se envio el archivo. Status:" + responseDes.getStatus());
                    }

                } else {
                    JsfUtil.infoDialog(fileIvEnc.getName(), "No se envio el archivo. Status:" + responseIvEnc.getStatus());
                }

            } else {
                JsfUtil.infoDialog(fileEnc.getName(), "No se envio el archivo. Status:" + response.getStatus());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            JsfUtil.errorDialog("send)=", e.getLocalizedMessage());
        }
        return "";
    }
    
    
    
    public Response sendFile(String url, String contentDisposition,InputStream fileInStream){
        Response response= null ;
            try {
                  Client clientDes = ClientBuilder.newClient();
                  response = clientDes.target(url).path("upload").request()
                            .header("Content-Disposition", contentDisposition)
                            .post(Entity.entity(fileInStream, MediaType.APPLICATION_OCTET_STREAM));
           
            } catch (Exception e) {
     
            JsfUtil.errorDialog("sendFile()", e.getLocalizedMessage());
        }
        return response;
    
    }
}
