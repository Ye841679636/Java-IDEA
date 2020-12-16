package com.newpoint.workstation;
import com.ib.client.EClientSocket;
import com.ib.client.EJavaSignal;
import com.ib.client.EReader;
import com.ib.client.EReaderSignal;
import com.newpoint.util.LockManager;

public class NPTradeSession {
    //set the connection and customer
    private final String TWS_HOST = "127.0.0.1";
    private final int TWS_PORT = 7497;
    private final int CLIENT_ID = 0;

    private boolean isAccountReady = false;
    private boolean isOrderReady = false;

    private final NPEWrapper wrapper;
    private final EReaderSignal signal;
    private final EClientSocket client;

    public String toString() {
        return "NPTradeSession{" +
                "TWS_HOST='" + TWS_HOST + '\'' +
                ", TWS_PORT=" + TWS_PORT +
                ", CLIENT_ID=" + CLIENT_ID +
                '}';
    }

    public NPTradeSession() {
        wrapper = new NPEWrapper(this);
        signal = new EJavaSignal();
        client = new EClientSocket(wrapper, signal);
    }

    //start a TWS session
    public void start() {
        //create a socket connection to the TWS session
        client.eConnect(getTWS_HOST(),getTWS_PORT(),getCLIENT_ID());

        final EReader reader = new EReader(client, signal);
        reader.start();

        //An additional thread is created in this program design to empty the messaging queue
        new Thread(() -> {
            while (client.isConnected()) {
                signal.waitForSignal();
                try {
                    reader.processMsgs();
                } catch (Exception e) {
                    System.out.println("Trade Session Exception: "+e.getMessage());
                }
            }
        }).start();

        if (client.isConnected()) {
            initialize();
        }
    }

    //initialize after a connection is obtained
    public void initialize() {
        synchronized (LockManager.getInstance().getAccountLock()) {
            while (!isAccountReady) {
                try {
                    LockManager.getInstance().getAccountLock().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        synchronized (LockManager.getInstance().getOrderReadyLock()) {
            while (!isOrderReady) {
                try {
                    LockManager.getInstance().getOrderReadyLock().wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    //close the TWS session
    public void close() {
        client.eDisconnect();
    }

    public EClientSocket getClient() {
        return client;
    }

    public String getTWS_HOST() {
        return TWS_HOST;
    }

    public int getTWS_PORT() {
        return TWS_PORT;
    }

    public int getCLIENT_ID() {
        return CLIENT_ID;
    }

    public void setOrderReady(boolean orderReady) {
        isOrderReady = orderReady;
    }

    public boolean isReady() {
        return isAccountReady;
    }

    public boolean isAccountReady() {
        return isAccountReady;
    }

    public void setAccountReady(boolean accountReady) {
        isAccountReady = accountReady;
    }

}
