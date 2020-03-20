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
public class FileController implements Serializable {

    private String directory = JsfUtil.userHome() + JsfUtil.fileSeparator() + "fiscalprinter" + JsfUtil.fileSeparator() + "license" + JsfUtil.fileSeparator();

    public String send() {

        try {
            //--Files to send 
          Response response  =sendFileToMicroservices(directory, "license.enc", "http://localhost:8080/serverfiles/resources/file", "upload");
        Response responseivEnc =   sendFileToMicroservices(directory, "licenseiv.enc", "http://localhost:8080/serverfiles/resources/file", "upload");
       Response responseDes   =  sendFileToMicroservices(directory, "license.des", "http://localhost:8080/serverfiles/resources/file", "upload");
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
    public String sendOld() {

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

    public Response sendFileToMicroservices(String directory, String filename, String url, String pathResouresMicroservices) {
        Response response = null;
        try {
            File fileToSend = new File(directory + filename);
            //---InputStream
            InputStream fileInStream = new FileInputStream(fileToSend);
            
             //--contentDisposition
            String contentDisposition = "attachment; filename=\"" + fileToSend.getName() + "\"";

            Client clientDes = ClientBuilder.newClient();
            response = clientDes.target(url).path(pathResouresMicroservices).request()
                    .header("Content-Disposition", contentDisposition)
                    .post(Entity.entity(fileInStream, MediaType.APPLICATION_OCTET_STREAM));

        } catch (Exception e) {

            JsfUtil.errorDialog("sendFileToMicroservices()", e.getLocalizedMessage());
        }
        return response;

    }
}
