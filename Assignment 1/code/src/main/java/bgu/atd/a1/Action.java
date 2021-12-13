package bgu.atd.a1;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * an abstract class that represents an action that may be executed using the
 * {@link ActorThreadPool}
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add to this class can
 * only be private!
 *
 * @param <R> the action result type
 */
public abstract class Action<R> {

    protected Promise<R> promise;
    protected String actionName;
    private ActorThreadPool threadPool;
    private callback callback;
    protected String actorId;
    protected PrivateState actorState;


    /**
     * start handling the action - note that this method is protected, a thread
     * cannot call it directly.
     */
    protected abstract void start() throws IllegalAccessException;
    

    /**
    *
    * start/continue handling the action
    *
    * this method should be called in order to start this action
    * or continue its execution in the case where it has been already started.
    *
    * IMPORTANT: this method is package protected, i.e., only classes inside
    * the same package can access it - you should *not* change it to
    * public/private/protected
    *
    */
   /*package*/ final void handle(ActorThreadPool pool, String actorId, PrivateState actorState) throws IllegalAccessException {
       if (this.callback != null) // promise resolved
           this.callback.call();

       else { // promise value is not resolved
           this.threadPool = pool;
           this.actorId = actorId;
           this.actorState = actorState;
           this.actorState.addRecord(this.actionName);
           this.start();
       }
   }
    
    
    /**
     * add a callback to be executed once *all* the given actions results are
     * resolved
     * 
     * Implementors note: make sure that the callback is running only once when
     * all the given actions completed.
     *
     * @param actions
     * @param callback the callback to execute once all the results are resolved
     */
    protected final void then(@NotNull Collection<? extends Action<?>> actions, callback callback) {
        AtomicInteger size = new AtomicInteger(actions.size());
        for (Action<?> action : actions) {
            action.promise.subscribe( () -> {
                int oldVal;
                int newVal;
                do {
                    oldVal = size.get();
                    newVal = oldVal - 1;
                } while (!size.compareAndSet(oldVal, newVal));
                if (newVal == 0){
                    this.callback = callback;
                    sendMessage(this, this.actorId, this.actorState);
                }
            });
        }
    }


    /**
     * resolve the internal result - should be called by the action derivative
     * once it is done.
     *
     * @param result - the action calculated result
     */
    protected final void complete(R result) {
       	this.promise.resolve(result);
    }
    
    /**
     * @return action's promise (result)
     */
    public final Promise<R> getResult() {
    	return this.promise;
    }
    
    /**
     * send an action to another actor
     * 
     * @param action
     * 				the action
     * @param actorId
     * 				actor's id
     * @param actorState
	 * 				actor's private state (actor's information)
     */
	public void sendMessage(Action<?> action, String actorId, PrivateState actorState){
        this.threadPool.submit(action, actorId, actorState);
	}
	
	/**
	 * set action's name
	 * @param actionName
	 */
	public void setActionName(String actionName){
        this.actionName = actionName;
	}
	
	/**
	 * @return action's name
	 */
	public String getActionName(){
        return this.actionName;
    }
}
