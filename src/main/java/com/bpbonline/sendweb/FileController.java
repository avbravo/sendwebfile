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
import javax.faces.bean.ViewScoped;
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
public class FileController implements Serializable{
    
    
    public String send(){
  
             try {
//			File file = new File("D:/Documents/JavaJ2EE.doc");
			File file = new File("/home/avbravo/Descargas/license.json");
			InputStream fileInStream = new FileInputStream(file);
                        
			String contentDisposition = "attachment; filename=\"" + file.getName() + "\"";
			Client client = ClientBuilder.newClient();
			Response response = client.target("http://localhost:8080/serverfiles/resources/file").path("upload").request()
					.header("Content-Disposition", contentDisposition)
					.post(Entity.entity(fileInStream, MediaType.APPLICATION_OCTET_STREAM));
			System.out.println(response.readEntity(String.class));
                       JsfUtil.infoDialog("enviado", "Se envio el archivo exitosamente");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
                        JsfUtil.errorDialog("send)=", e.getLocalizedMessage());
		}
        return "";
    }
}
