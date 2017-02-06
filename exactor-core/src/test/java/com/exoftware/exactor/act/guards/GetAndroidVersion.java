package com.exoftware.exactor.act.guards;

import com.exoftware.exactor.command.utility.Verb;

public class GetAndroidVersion implements Verb {

    @Override
    public Object execute() {
        return 2.5;
    }

}