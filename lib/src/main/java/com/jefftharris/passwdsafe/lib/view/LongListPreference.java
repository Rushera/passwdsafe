/*
 * Copyright (©) 2014 Jeff Harris <jefftharris@gmail.com>
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package com.jefftharris.passwdsafe.lib.view;

import android.content.Context;
import android.preference.ListPreference;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * The LongListPreference class is a ListPreference with support for a
 * multi-line title
 */
public class LongListPreference extends ListPreference
{
    /// Constructor
    public LongListPreference(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    /// Constructor
    public LongListPreference(Context context)
    {
        super(context);
    }

    /* (non-Javadoc)
     * @see android.preference.Preference#onBindView(android.view.View)
     */
    @Override
    protected void onBindView(@NonNull View view)
    {
        super.onBindView(view);
        TextView title = (TextView)view.findViewById(android.R.id.title);
        title.setSingleLine(false);
    }
}
