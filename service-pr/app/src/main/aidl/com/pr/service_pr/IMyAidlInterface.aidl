// IMyAidlInterface.aidl
package com.pr.service_pr;

// Declare any non-default types here with import statements

interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     // 원시형이 아닌 데이터에는 in out으로 방향을 핈수로 정의


    int add(int n1, int n2);
}
