package com.georgestudenko.inventoryapp;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.stetho.Stetho;
import com.georgestudenko.inventoryapp.data.InventoryAdapter;
import com.georgestudenko.inventoryapp.data.InventoryContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ListView mListView;
    private InventoryAdapter mAdapter;
    private TextView mEmptyListTextView;
    private LinearLayout mHeaders;
    private final int LOADER_ID = 200;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);

        mListView = (ListView) findViewById(R.id.listView);
        mEmptyListTextView = (TextView) findViewById(R.id.emptyList);
        mListView.setEmptyView(mEmptyListTextView);
        mHeaders = (LinearLayout) findViewById(R.id.headers) ;

        mAdapter = new InventoryAdapter(this, null,true);
        mListView.setAdapter(mAdapter);

        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),EditorActivity.class);
                startActivity(intent);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(),EditorActivity.class);
                intent.setData(ContentUris.withAppendedId(InventoryContract.InventoryEntry.CONTENT_URI,id));
                startActivity(intent);
            }
        });

        getSupportLoaderManager().initLoader(LOADER_ID,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return  new CursorLoader(this, InventoryContract.InventoryEntry.CONTENT_URI,
                null,null,null, InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        mListView.setAdapter(mAdapter);
        if(data.getCount()>0){
            mHeaders.setVisibility(View.VISIBLE);
        }else
        {
            mHeaders.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
