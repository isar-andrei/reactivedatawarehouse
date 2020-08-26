package com.ai.dwsprintreactive.performance;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class InsertData {

    @Test
    public void insertIntoActivity() {
        Flux<String> dataStream = Flux.just("AA", "BB", "CC", "DD");
        dataStream.name("my-test-name").tag("key1", "value1").metrics()
                .subscribe(p ->
                           {
                               System.out.println("Hello " + p);
                               try {
                                   Thread.sleep(1000);
                               } catch (InterruptedException e) {
                                   e.printStackTrace();
                               }
                           });
    }
}
