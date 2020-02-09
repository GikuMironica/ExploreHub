package bookingComponent;

import bookingComponent.PaymentStrategy;

public class PaymentStrategyContext {

    private PaymentStrategy strategy;

    /**
     * Context decides which strategy to use based on input
     */
    public PaymentStrategyContext(PaymentStrategy strategy){
        this.strategy = strategy;
    }

    public boolean executeStrategy(){return strategy.pay();}
}
