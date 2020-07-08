/*
 * #%L
 * SEquence DAtaset builder PfamScan plugin
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
package org.sing_group.seda.pfam;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.sing_group.seda.datatype.Sequence;

public class PfamScanBatch {

  private static final int DELAY_STATUS_RETRY = 15;

  private PfamScanRequestConfiguration config;
  private List<Sequence> sequences;

  public PfamScanBatch(PfamScanRequestConfiguration config, List<Sequence> sequences) {
    this.config = config;
    this.sequences = sequences;
  }

  public CompletableFuture<PfamScanBatchResult> query() {
    return CompletableFuture.supplyAsync(this::processSequences);
  }

  private PfamScanBatchResult processSequences() {
    Map<Sequence, PfamScanAnnotations> annotations = new ConcurrentHashMap<Sequence, PfamScanAnnotations>();
    List<Sequence> errors = new LinkedList<>();
    List<Thread> threads = new LinkedList<Thread>();

    for (Sequence sequence : sequences) {
      Thread t =
        new Thread(new Runnable() {

          @Override
          public void run() {
            PfamScanRequest request = new PfamScanRequest(sequence, config);

            try {
              request.query();

              while (request.getStatus().equals(PfamScanStatus.RUNNING)) {
                TimeUnit.SECONDS.sleep(DELAY_STATUS_RETRY);
              }

              if (request.getStatus().equals(PfamScanStatus.FINISHED)) {
                annotations.put(sequence, request.getResult());
              } else {
                throw new RuntimeException(
                  "Error analyzing " + sequence.getName() + ". Status: " + request.getStatus() + ". Request ID: "
                    + request.getRequestId().get()
                );
              }
            } catch (IOException | InterruptedException e) {
              e.printStackTrace();
              errors.add(sequence);
            }
          }
        });
      threads.add(t);
      t.start();

      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    threads.forEach(t -> {
      try {
        t.join();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    });

    return new PfamScanBatchResult(annotations, errors);
  }
}
