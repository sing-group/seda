/*
 * #%L
 * SEquence DAtaset builder BLAST plugin
 * %%
 * Copyright (C) 2017 - 2020 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.sing_group.seda.blast.ncbi;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class NcbiBlastHits extends LinkedList<NcbiBlastHit> {
  private static final long serialVersionUID = 1L;

  public static final NcbiBlastHits fromXml(String xml) throws SAXException, IOException {
    NcbiBlastHits toret = new NcbiBlastHits();

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder;
    try {
      documentBuilder = factory.newDocumentBuilder();
      Document document = documentBuilder.parse(new ByteArrayInputStream(xml.getBytes()));
      Element root = document.getDocumentElement();
      NodeList hits = root.getElementsByTagName("Hit");
      for (int temp = 0; temp < hits.getLength(); temp++) {
        Node nNode = hits.item(temp);

        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
          Element eElement = (Element) nNode;
          String accession = eElement.getElementsByTagName("Hit_accession").item(0).getTextContent();
          String description = eElement.getElementsByTagName("Hit_def").item(0).getTextContent();

          Node hitHsps = eElement.getElementsByTagName("Hit_hsps").item(0);
          if (hitHsps.getNodeType() == Node.ELEMENT_NODE) {
            NodeList hsps = ((Element) hitHsps).getElementsByTagName("Hsp");
            List<String> hspHseqs = new LinkedList<>();
            for (int i = 0; i < hsps.getLength(); i++) {
              Node hsp = hsps.item(i);
              if (hsp.getNodeType() == Node.ELEMENT_NODE) {
                Node hspHseq = ((Element) hsp).getElementsByTagName("Hsp_hseq").item(0);
                hspHseqs.add(hspHseq.getTextContent());
              }
            }
            toret.add(new NcbiBlastHit(accession, description, hspHseqs));
          }
        }
      }

      return toret;
    } catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    }
  }
}
