package bgu.atd.a1;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * represents an actor thread pool - to understand what this class does please
 * refer to your assignment.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class ActorThreadPool {

	private int nthreads;
	private Map<String, PrivateState> actors;
	private Map<String, Queue<Action>> actionsQueues;
	private ExecutorService executorService;
	private LinkedBlockingQueue<String> availableActors;
	private Queue<String> unavailableActors;


	/**
	 * creates an {@link ActorThreadPool} which has nthreads. Note, threads
	 * should not get started until calling to the {@link #start()} method.
	 *
	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail...
	 *
	 * @param nthreads
	 *            the number of threads that should be started by this thread
	 *            pool
	 */
	public ActorThreadPool(int nthreads) {
		this.nthreads = nthreads;
		this.actors = new HashMap<>();
		this.actionsQueues = new HashMap<>();
		this.executorService = Executors.newFixedThreadPool(nthreads);
		this.availableActors = new LinkedBlockingQueue<>();
		this.unavailableActors = new ConcurrentLinkedDeque<>();
	}

	/**
	 * getter for actors
	 * @return actors
	 */
	public Map<String, PrivateState> getActors(){
		return this.actors;
	}
	
	/**
	 * getter for actor's private state
	 * @param actorId actor's id
	 * @return actor's private state
	 */
	public PrivateState getPrivateState(String actorId){
		return this.actors.get(actorId);
	}


	/**
	 * submits an action into an actor to be executed by a thread belongs to
	 * this thread pool
	 *
	 * @param action
	 *            the action to execute
	 * @param actorId
	 *            corresponding actor's id
	 * @param actorState
	 *            actor's private state (actor's information)
	 */
	public void submit(Action<?> action, String actorId, PrivateState actorState) {
		synchronized (actorState) {
			if (!this.actors.containsKey(actorId)) {
				this.actors.put(actorId, actorState);
				this.actionsQueues.put(actorId, new ConcurrentLinkedDeque<>());
			}
		}
		this.actionsQueues.get(actorId).add(action);
		synchronized (this.availableActors) {
			if (!this.unavailableActors.contains(actorId) && !this.availableActors.contains(actorId)) {
				this.availableActors.add(actorId);
				this.availableActors.notifyAll();
			}
		}
	}

	/**
	 * closes the thread pool - this method interrupts all the threads and waits
	 * for them to stop - it is returns *only* when there are no live threads in
	 * the queue.
	 *
	 * after calling this method - one should not use the queue anymore.
	 *
	 * @throws InterruptedException
	 *             if the thread that shut down the threads is interrupted
	 */
	public void shutdown() throws InterruptedException {
		this.executorService.shutdown();
	}

	/**
	 * start the threads belongs to this thread pool
	 */
	public void start() {
		for(int i = 0; i < this.nthreads; i++) {
			this.executorService.execute( () -> {
				while (!this.executorService.isShutdown()) {
					String actorName = null;
					PrivateState actorState = null;
					synchronized (this.availableActors) {
						try {
							while (availableActors.isEmpty()) {
								this.availableActors.wait();
							}
							actorName = this.availableActors.take();

							this.unavailableActors.add(actorName);
							actorState = actors.get(actorName);
						} catch (InterruptedException e) {
							break;
						}
					}
					if(!this.actionsQueues.get(actorName).isEmpty()) {
						try {
							this.actionsQueues.get(actorName).poll().handle(this, actorName, actorState);
						} catch (IllegalAccessException e) {
							break;
						}
					}
					synchronized (this.availableActors) {
						this.unavailableActors.remove(actorName);
						if (!this.actionsQueues.get(actorName).isEmpty()) {
							this.availableActors.add(actorName);
							this.availableActors.notifyAll();
						}
					}
				}
			});
		}
	}

}
