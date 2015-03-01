/*
 * Copyright (©) 2015 Jeff Harris <jefftharris@gmail.com>
 * All rights reserved. Use of the code is allowed under the
 * Artistic License 2.0 terms, as specified in the LICENSE file
 * distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */
package com.jefftharris.passwdsafe.sync.owncloud;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jefftharris.passwdsafe.lib.PasswdSafeUtil;
import com.jefftharris.passwdsafe.lib.Utils;
import com.jefftharris.passwdsafe.sync.R;

/**
 *  Fragment to show ownCloud password files
 */
public class OwncloudFilesFragment extends ListFragment
{
    /** Listener interface for the owning activity */
    public interface Listener
    {
        /** Callback to handle the result of listinf files */
        public interface ListFilesCb
        {
            public void handleFiles(List<OwncloudProviderFile> files);
        }

        /** List files for a given path */
        void listFiles(String path, ListFilesCb cb);

        /** Change directory to the given path */
        void changeDir(String path);
    }

    private static final String TAG = "OwncloudFilesFragment";

    private String itsPath;
    private Listener itsListener;
    private ArrayAdapter<OwncloudProviderFile> itsFilesAdapter;

    /** Create a new instance of the fragment */
    public static OwncloudFilesFragment newInstance(String path)
    {
        OwncloudFilesFragment frag = new OwncloudFilesFragment();
        Bundle args = new Bundle();
        args.putString("path", path);
        frag.setArguments(args);
        return frag;
    }


    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
     */
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        itsListener = (Listener)activity;
    }


    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        itsPath = getArguments().getString("path");
    }


    /* (non-Javadoc)
     * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = super.onCreateView(inflater, container,
                                           savedInstanceState);
        // TODO: show current dir
        return rootView;
    }


    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        setEmptyText("No files!!!!");

        itsFilesAdapter = new FilesAdapter(getActivity());
        setListAdapter(itsFilesAdapter);
    }


    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onResume()
     */
    @Override
    public void onResume()
    {
        super.onResume();
        reload();
    }


    /* (non-Javadoc)
     * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        OwncloudProviderFile file = itsFilesAdapter.getItem(position);
        if (file == null) {
            return;
        }

        if (OwncloudProviderFile.isFolder(file.getRemoteFile())) {
            itsListener.changeDir(file.getRemoteId());
            // TODO: chdir to parent
        } else {
            // TODO: select file
        }
    }


    /** Reload the files shown by the fragment */
    public void reload()
    {
        // TODO: reload menu option
        itsListener.listFiles(itsPath, new Listener.ListFilesCb()
        {
            @Override
            public void handleFiles(List<OwncloudProviderFile> files)
            {
                itsFilesAdapter.clear();
                for (OwncloudProviderFile file: files) {
                    PasswdSafeUtil.dbginfo(
                            TAG, "list file: %s",
                            OwncloudProviderFile.fileToString(file.getRemoteFile()));
                    itsFilesAdapter.add(file);
                }
            }
        });
    }


    /** Adapter for files shown in the list */
    private static class FilesAdapter extends ArrayAdapter<OwncloudProviderFile>
    {
        private final LayoutInflater itsInflater;

        /** Constructor */
        public FilesAdapter(Activity act)
        {
            super(act, R.layout.listview_sync_file_item);
            itsInflater = act.getLayoutInflater();
        }


        /* (non-Javadoc)
         * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder views;
            if (convertView == null) {
                convertView = itsInflater.inflate(
                        R.layout.listview_sync_file_item, parent, false);
                views = new ViewHolder(convertView);
                convertView.setTag(views);
            } else {
                views = (ViewHolder)convertView.getTag();
            }

            OwncloudProviderFile file = getItem(position);
            views.itsText.setText(file.getTitle());

            if (OwncloudProviderFile.isFolder(file.getRemoteFile())) {
                views.itsModDate.setVisibility(View.GONE);
                views.itsIcon.setImageResource(R.drawable.folder_rev);
            } else {
                views.itsModDate.setVisibility(View.VISIBLE);
                views.itsModDate.setText(Utils.formatDate(file.getModTime(),
                                                          getContext()));
                views.itsIcon.setImageResource(R.drawable.login_rev);
            }

            return convertView;
        }

        /** View holder for fields in a list item */
        private static class ViewHolder
        {
            public final TextView itsText;
            public final TextView itsModDate;
            public final ImageView itsIcon;

            /** Constructor */
            public ViewHolder(View view)
            {
                itsText = (TextView)view.findViewById(R.id.text);
                itsModDate = (TextView)view.findViewById(R.id.mod_date);
                itsIcon = (ImageView)view.findViewById(R.id.icon);
            }
        }
    }
}