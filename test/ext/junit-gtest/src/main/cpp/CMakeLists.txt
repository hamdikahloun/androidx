cmake_minimum_required(VERSION 3.22.1)

project(junit-gtest LANGUAGES CXX)

find_package(googletest REQUIRED CONFIG)

add_library(nativehelper
        INTERFACE
        )

target_include_directories(nativehelper
        INTERFACE
        ./
        )

add_library(junit-gtest
        STATIC
        gtest_wrapper.cpp
        )

target_link_libraries(junit-gtest
        PRIVATE
        googletest::gtest
        nativehelper
        PUBLIC
        -uJava_androidx_test_ext_junitgtest_GtestRunner_initialize
        -uJava_androidx_test_ext_junitgtest_GtestRunner_run
        -uJava_androidx_test_ext_junitgtest_GtestRunner_addTest
        )
