package simplelog;

import simplelog.SimpleLogger;


public class LoggingTest {

	private final static SimpleLogger log = new SimpleLogger(LoggingTest.class);

	public static void main(String[] args) {
		log.entry("main", args);
		
		Thread thread = new Thread("thread") {			
			public void run() {
				SimpleLogger log = new SimpleLogger(Runnable.class);
				log.entry("run");
				for (int i = 0; i < 10; i++) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						log.warnException(e);
					}
					log.infoObject("index", i);
				}
				log.exit("run");
			}
		};

		thread.start();
		
		log.info("info1");
		log.info("info2");
		log.debug("debug1");
		log.debugObject("ObjAA", new Object[] {"a", "b", new Object[] { "c", 'd' } } );
		log.debugObject("Stringarray", new String[] {"a", "b"} );
		log.debugObject("AA", new Object[] {"a", "b", new String[] {"c", "d"} } );
		log.debugObject("bytearray", new byte [] { 1,2,3, } );
		log.debugObject("chararray", new char[] { 'a','b','\n','\t' } );
		log.debugObject("intarray", new int[] { 1,2,3,4,5,6,7 } );
		log.debug("debug2");
		log.verbose("verbose1");
		try {
			for (int i = 0; i < 10; i++) {
				printOut(new Object[] {new Integer(i), new StringBuffer("StringBuffer"), new String[] { "Array" }, "String" });
			}
			throw new RuntimeException("test");

		} catch (Throwable e) {
			log.errorException(e);
		}

		log.error("ERROR1");
		log.exit("main");
	}

	private static void printOut(Object[] message) {
		log.entry("printOut", message);
		log.debugObject("Message", message);

		System.out.println("Message is "+message[0]+".");
//		for (int i = 0; i < message.length; i++) {
//			System.out.println(message[i]);
//		}
		log.exit("printOut", "result");
	}

}
