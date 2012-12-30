/*
 * Copyright (©) 2012 Jeff Harris <jefftharris@gmail.com>
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package com.jefftharris.passwdsafe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/** Receiver for the startup event */
public class StartupReceiver extends BroadcastReceiver
{
    private static final String TAG = "StartupReceiver";

    /* (non-Javadoc)
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent)
    {
        PasswdSafeApp.dbginfo(TAG, "StartupReceiver");
        // The app is created in order to launch the receiver which in turn
        // starts the notification manager
    }
}