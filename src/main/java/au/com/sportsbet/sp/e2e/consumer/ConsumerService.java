package au.com.sportsbet.sp.e2e.consumer;

public interface ConsumerService<H, M> {

    void consume(H headers, M message);

}
