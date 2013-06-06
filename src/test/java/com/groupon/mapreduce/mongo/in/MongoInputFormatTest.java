/*
Copyright (c) 2013, Groupon, Inc.
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.

Neither the name of GROUPON nor the names of its contributors may be
used to endorse or promote products derived from this software without
specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.groupon.mapreduce.mongo.in;

import com.groupon.mapreduce.mongo.in.MongoInputFormat;
import com.groupon.mapreduce.mongo.in.MongoInputSplit;
import com.groupon.mapreduce.mongo.in.Record;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class MongoInputFormatTest {
    public static List<MongoInputSplit> getSplits() {
        Path path = new Path(NamespaceIndexTest.DB_FILE);

        FileSystem fs = NamespaceIndexTest.getFilesystem();

        MongoInputFormat.setCollection("testcoll1");
        MongoInputFormat.setDatabase("deepmr_test");
        MongoInputFormat.setMongoDirectory(new Path("src/test"));

        MongoInputFormat inputFormat = new MongoInputFormat();
        return inputFormat.getSplitsFromFile(fs, path);
    }

    @Test
    public void getSplitsTest() {
        List<MongoInputSplit> splits = getSplits();
        assertTrue(splits.size() == 7);
        FileSystem fs = NamespaceIndexTest.getFilesystem();

        for (MongoInputSplit split : splits) {
            for (Iterator<Record> i = split.getExtent().iterator(fs); i.hasNext(); ) {
                Record r = i.next();
                assertTrue(r.getId(fs) != null);
            }
        }
    }
}
