// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.net;

import android.os.ParcelFileDescriptor;
import android.support.annotation.RequiresApi;
import java.net.Socket;
import java.net.SocketException;
import java.net.DatagramSocket;
import android.net.TrafficStats;
import android.os.Build$VERSION;

public final class TrafficStatsCompat
{
    private static final TrafficStatsCompatBaseImpl IMPL;
    
    static {
        if (Build$VERSION.SDK_INT >= 24) {
            IMPL = (TrafficStatsCompatBaseImpl)new TrafficStatsCompatApi24Impl();
            return;
        }
        IMPL = new TrafficStatsCompatBaseImpl();
    }
    
    @Deprecated
    public static void clearThreadStatsTag() {
        TrafficStats.clearThreadStatsTag();
    }
    
    @Deprecated
    public static int getThreadStatsTag() {
        return TrafficStats.getThreadStatsTag();
    }
    
    @Deprecated
    public static void incrementOperationCount(final int n) {
        TrafficStats.incrementOperationCount(n);
    }
    
    @Deprecated
    public static void incrementOperationCount(final int n, final int n2) {
        TrafficStats.incrementOperationCount(n, n2);
    }
    
    @Deprecated
    public static void setThreadStatsTag(final int threadStatsTag) {
        TrafficStats.setThreadStatsTag(threadStatsTag);
    }
    
    public static void tagDatagramSocket(final DatagramSocket datagramSocket) throws SocketException {
        TrafficStatsCompat.IMPL.tagDatagramSocket(datagramSocket);
    }
    
    @Deprecated
    public static void tagSocket(final Socket socket) throws SocketException {
        TrafficStats.tagSocket(socket);
    }
    
    public static void untagDatagramSocket(final DatagramSocket datagramSocket) throws SocketException {
        TrafficStatsCompat.IMPL.untagDatagramSocket(datagramSocket);
    }
    
    @Deprecated
    public static void untagSocket(final Socket socket) throws SocketException {
        TrafficStats.untagSocket(socket);
    }
    
    @RequiresApi(24)
    static class TrafficStatsCompatApi24Impl extends TrafficStatsCompatBaseImpl
    {
        @Override
        public void tagDatagramSocket(final DatagramSocket datagramSocket) throws SocketException {
            TrafficStats.tagDatagramSocket(datagramSocket);
        }
        
        @Override
        public void untagDatagramSocket(final DatagramSocket datagramSocket) throws SocketException {
            TrafficStats.untagDatagramSocket(datagramSocket);
        }
    }
    
    static class TrafficStatsCompatBaseImpl
    {
        public void tagDatagramSocket(final DatagramSocket datagramSocket) throws SocketException {
            final ParcelFileDescriptor fromDatagramSocket = ParcelFileDescriptor.fromDatagramSocket(datagramSocket);
            TrafficStats.tagSocket((Socket)new DatagramSocketWrapper(datagramSocket, fromDatagramSocket.getFileDescriptor()));
            fromDatagramSocket.detachFd();
        }
        
        public void untagDatagramSocket(final DatagramSocket datagramSocket) throws SocketException {
            final ParcelFileDescriptor fromDatagramSocket = ParcelFileDescriptor.fromDatagramSocket(datagramSocket);
            TrafficStats.untagSocket((Socket)new DatagramSocketWrapper(datagramSocket, fromDatagramSocket.getFileDescriptor()));
            fromDatagramSocket.detachFd();
        }
    }
}
