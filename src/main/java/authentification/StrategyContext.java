package authentification;


/**
 * Context class for the login strategy pattern, The client code picks a concrete strategy and passes it to
 * the context.
 *
 * @author Gheorghe Mironica
 */
public class StrategyContext {

   private Strategy strategy;

    /**
     * Context decides which strategy to use based on input,
     */
   public StrategyContext(Strategy strategy){
       this.strategy = strategy;
   }

    /**
     * delegates some work to the strategy object instead of implementing multiple versions of the
     * algorithm on its own.
     * @param username {@link String}
     * @param password {@link String}
     */
   public void executeStrategy(String username, String password){
       strategy.getAccount(username, password);
   }

}
