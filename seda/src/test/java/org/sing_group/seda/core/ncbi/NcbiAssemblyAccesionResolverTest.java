/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2024 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.core.ncbi;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

public class NcbiAssemblyAccesionResolverTest {

    private WireMockServer wireMockServer;
    private NcbiAssemblyAccesionResolver resolver;

    @Before
    public void setUp() {
        // Start WireMock server on a specific port
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
        WireMock.configureFor("localhost", 8080);
        resolver = new NcbiAssemblyAccesionResolver(10000) {
            @Override
            protected URL assemblyUrl(String accession) throws MalformedURLException {
                return new URL("http://localhost:8080/assembly/" + accession);
            }
        };
    }

    @After
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testResolveAccession1() throws IOException {
        String accession = "GCF_000151265.2";
        Path path = Paths.get("src/test/resources/ncbi/GCF_000151265.2.html");
        String responseHtml = new String(Files.readAllBytes(path));

        stubFor(get(urlPathEqualTo("/assembly/" + accession))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/html")
                        .withBody(responseHtml)));

        assertAccession(resolver.resolve(accession), accession, "Micromonas pusilla", "564608");
    }

    @Test
    public void testResolveAccession2() throws IOException {
        String accession = "GCF_000001405.40";
        Path path = Paths.get("src/test/resources/ncbi/GCF_000001405.40.html");
        String responseHtml = new String(Files.readAllBytes(path));

        stubFor(get(urlPathEqualTo("/assembly/" + accession))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/html")
                        .withBody(responseHtml)));

        assertAccession(resolver.resolve(accession), accession, "Homo sapiens", "9606");
    }

    private void assertAccession(
            Optional<NcbiAssemblyAccession> result,
            String accession,
            String organismName,
            String taxId
    ) {
        assertTrue(result.isPresent());
        NcbiAssemblyAccession acc = result.get();
        assertEquals(accession, acc.getAccession());
        assertEquals(organismName, acc.getOrganismName());
        assertTrue(acc.getTaxonomyUrl().toString().contains(taxId));
    }
}
