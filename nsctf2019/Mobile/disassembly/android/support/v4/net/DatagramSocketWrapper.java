// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.net;

import java.io.OutputStream;
import java.io.InputStream;
import java.net.SocketAddress;
import java.net.InetAddress;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketImpl;
import java.io.FileDescriptor;
import java.net.DatagramSocket;
import java.net.Socket;

class DatagramSocketWrapper extends Socket
{
    public DatagramSocketWrapper(final DatagramSocket datagramSocket, final FileDescriptor fileDescriptor) throws SocketException {
        super(new DatagramSocketImplWrapper(datagramSocket, fileDescriptor));
    }
    
    private static class DatagramSocketImplWrapper extends SocketImpl
    {
        public DatagramSocketImplWrapper(final DatagramSocket datagramSocket, final FileDescriptor fd) {
            this.localport = datagramSocket.getLocalPort();
            this.fd = fd;
        }
        
        @Override
        protected void accept(final SocketImpl socketImpl) throws IOException {
            throw new UnsupportedOperationException();
        }
        
        @Override
        protected int available() throws IOException {
            throw new UnsupportedOperationException();
        }
        
        @Override
        protected void bind(final InetAddress inetAddress, final int n) throws IOException {
            throw new UnsupportedOperationException();
        }
        
        @Override
        protected void close() throws IOException {
            throw new UnsupportedOperationException();
        }
        
        @Override
        protected void connect(final String s, final int n) throws IOException {
            throw new UnsupportedOperationException();
        }
        
        @Override
        protected void connect(final InetAddress inetAddress, final int n) throws IOException {
            throw new UnsupportedOperationException();
        }
        
        @Override
        protected void connect(final SocketAddress socketAddress, final int n) throws IOException {
            throw new UnsupportedOperationException();
        }
        
        @Override
        protected void create(final boolean b) throws IOException {
            throw new UnsupportedOperationException();
        }
        
        @Override
        protected InputStream getInputStream() throws IOException {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Object getOption(final int n) throws SocketException {
            throw new UnsupportedOperationException();
        }
        
        @Override
        protected OutputStream getOutputStream() throws IOException {
            throw new UnsupportedOperationException();
        }
        
        @Override
        protected void listen(final int n) throws IOException {
            throw new UnsupportedOperationException();
        }
        
        @Override
        protected void sendUrgentData(final int n) throws IOException {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void setOption(final int n, final Object o) throws SocketException {
            throw new UnsupportedOperationException();
        }
    }
}
