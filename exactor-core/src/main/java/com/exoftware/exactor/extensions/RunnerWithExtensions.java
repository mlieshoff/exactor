package com.exoftware.exactor.extensions;

import com.exoftware.exactor.Runner;
import com.exoftware.exactor.listener.SimpleListener;

import java.io.FileNotFoundException;

public class RunnerWithExtensions {
    /**
     * Main entry point. Expected argument is the name of a script file or a directory containing script files.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        try {
            Runner runner = new Runner(args[0]);
            runner.addListener(new SimpleListener());
            runner.addListener(new SquareBracketParameterSubstitutionExtension());
            runner.addListener(new PauserExtension());
            runner.run();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
