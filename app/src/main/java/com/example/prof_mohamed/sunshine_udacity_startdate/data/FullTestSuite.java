package com.example.prof_mohamed.sunshine_udacity_startdate.data;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Created by Prof-Mohamed on 7/28/2016.
 */

public class FullTestSuite extends TestSuite {

    public static Test Suite(){
        return new TestSuiteBuilder(FullTestSuite.class)
                .includeAllPackagesUnderHere().build();
    }

    public FullTestSuite(){super();}
}
