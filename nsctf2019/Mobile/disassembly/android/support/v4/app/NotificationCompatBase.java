// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import android.os.Bundle;
import android.app.PendingIntent;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class NotificationCompatBase
{
    public abstract static class Action
    {
        public abstract PendingIntent getActionIntent();
        
        public abstract boolean getAllowGeneratedReplies();
        
        public abstract RemoteInputCompatBase.RemoteInput[] getDataOnlyRemoteInputs();
        
        public abstract Bundle getExtras();
        
        public abstract int getIcon();
        
        public abstract RemoteInputCompatBase.RemoteInput[] getRemoteInputs();
        
        public abstract CharSequence getTitle();
        
        public interface Factory
        {
            Action build(final int p0, final CharSequence p1, final PendingIntent p2, final Bundle p3, final RemoteInputCompatBase.RemoteInput[] p4, final RemoteInputCompatBase.RemoteInput[] p5, final boolean p6);
            
            Action[] newArray(final int p0);
        }
    }
    
    public abstract static class UnreadConversation
    {
        abstract long getLatestTimestamp();
        
        abstract String[] getMessages();
        
        abstract String getParticipant();
        
        abstract String[] getParticipants();
        
        abstract PendingIntent getReadPendingIntent();
        
        abstract RemoteInputCompatBase.RemoteInput getRemoteInput();
        
        abstract PendingIntent getReplyPendingIntent();
        
        public interface Factory
        {
            UnreadConversation build(final String[] p0, final RemoteInputCompatBase.RemoteInput p1, final PendingIntent p2, final PendingIntent p3, final String[] p4, final long p5);
        }
    }
}
