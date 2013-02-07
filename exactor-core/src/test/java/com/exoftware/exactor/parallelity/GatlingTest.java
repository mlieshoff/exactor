/******************************************************************
 * Copyright (c) 2013, Exoftware
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 *   * Redistributions of source code must retain the above
 *     copyright notice, this list of conditions and the following
 *     disclaimer.
 *   * Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions and the following
 *     disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *   * Neither the name of the Exoftware, Exactor nor the names
 *     of its contributors may be used to endorse or promote
 *     products derived from this software without specific
 *     prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *****************************************************************/
package com.exoftware.exactor.parallelity;

import com.exoftware.util.Idioms;
import junit.framework.TestCase;

/**
 * This class is a test for class Gatling.
 *
 * @author Michael Lieshoff
 */
public class GatlingTest extends TestCase {

    public void tearDown() {
        Gatling.stop();
        waitUntilAllFinished();
        Gatling.unregisterAll();
    }

    public void testStartAndFinished() {
        Gatling.start(create("test", 1, 1, 1));
        waitUntilAllFinished();
        assertTrue(Gatling.finished());
    }

    private Parallelity create(String name, int maxTurns, long wait, long pause) {
        return new Parallelity(name, maxTurns, wait, pause) {
            @Override
            protected void runIntern() throws Exception {
                // nop
            }
        };
    }

    private void waitUntilAllFinished() {
        new Idioms.UntilTimeoutDo(){
            @Override
            public boolean action() {
                return Gatling.finished();
            }
        }.run(100);
    }

    public void testStartTwice() {
        Parallelity parallelity = create("test", 1, 1, 1);
        Gatling.start(parallelity);
        try {
            Gatling.start(parallelity);
            fail("exception expected!");
        } catch(IllegalArgumentException e) {
            // expected
        }
    }

    public void testStartAndStop() {
        Gatling.start(create("test", 10, 1, 1));
        Gatling.stop();
        waitUntilAllFinished();
        assertTrue(Gatling.finished());
    }

    public void testStats() throws InterruptedException {
        Gatling.start(create("test", 1, 1, 1));
        waitUntilAllFinished();
        String actual = Gatling.stats();
        StringBuilder expected = new StringBuilder();
        expected.append("GATLING EXECUTION DUMP\n");
        expected.append(String.format("%s -> %s\n", "test", "1"));
        assertEquals(expected.toString(), actual);
    }

}
