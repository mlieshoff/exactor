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

/**
 *
 * @author Michael Lieshoff
 */
public abstract class Parallelity implements Runnable {
    private volatile boolean running = false;
    private volatile int turns = 0;
    private int maxTurns;
    private long wait;
    private long pause;
    private String name;

    protected Parallelity(String name, int maxTurns, long wait, long pause) {
        this.name = name;
        this.maxTurns = maxTurns;
        this.wait = wait;
        this.pause = pause;
    }

    @Override
    public final void run() {
        running = true;
        try {
            Thread.sleep(wait);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
        try {
            while (running) {
                runIntern();
                turns ++;
                Gatling.count(this);
                try {
                    Thread.sleep(pause);
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
                if (maxTurns > 0 && turns > maxTurns) {
                    running = false;
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    protected abstract void runIntern() throws Exception;

    public final void stop() {
        running = false;
    }

    public final boolean isRunning() {
        return running;
    }

    public String getName() {
        return name;
    }

    public int getTurns() {
        return turns;
    }
}
