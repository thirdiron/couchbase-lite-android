//
// Copyright (c) 2016 Couchbase, Inc. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
// except in compliance with the License. You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software distributed under the
// License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
// either express or implied. See the License for the specific language governing permissions
// and limitations under the License.
//
package com.couchbase.lite;

import com.couchbase.lite.util.Log;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by hideki on 6/14/16.
 */
public class TimeSeriesTest extends LiteTestCaseWithDB {
    public final static String TAG = TimeSeriesTest.class.getName();

    TimeSeries ts;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ts = new TimeSeries(database, "tstest");
    }

    public void test1_TimeSeriesCollector() throws CouchbaseLiteException {
        generateEventsSync();
        int i = 0;
        QueryEnumerator e = database.createAllDocumentsQuery().run();
        for(QueryRow row : e){
            Date t0 = (Date)row.getDocumentProperties().get("t0");
            assertNotNull(t0);
            List events = (List)row.getDocumentProperties().get("events");
            Log.v(TAG, "Doc %s: %d events starting at %s", row.getDocumentId(), events.size(), t0);
        }
    }


    private void generateEvents() {
        Random rand = new Random();
        Log.v(TAG, "Generating events...");
        for (int i = 0; i < 10000; i++) {
            try {
                Thread.sleep(0, 100 * 1000);//100microsecond (100000 nanosecond)
            } catch (Exception e) {
            }
            Map<String, Object> e = new HashMap<>();
            e.put("i", i);
            e.put("random", rand.nextLong());
            ts.addEvent(e);
        }
    }

    private void generateEventsSync() {
        generateEvents();
        ts.flush();
        assertNull(ts.getLastError());
        Log.v(TAG, "...Done generating events");
    }
}
